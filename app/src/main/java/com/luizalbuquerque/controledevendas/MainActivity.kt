// MainActivity.kt
package com.luizalbuquerque.controledevendas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.luizalbuquerque.controledevendas.ui.theme.ControleDeVendasTheme

data class Venda(val produto: String, val quantidade: Int, val valor: Double)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControleDeVendasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TelaPrincipal()
                }
            }
        }
    }
}

@Composable
fun TelaPrincipal() {
    var produto by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }
    val listaVendas = remember { mutableStateListOf<Venda>() }

    val total = listaVendas.sumOf { it.quantidade * it.valor }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Controle de Vendas", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = produto,
            onValueChange = { produto = it },
            label = { Text("Produto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        if (erro.isNotBlank()) {
            Text(erro, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            onClick = {
                val qtd = quantidade.toIntOrNull() ?: 0
                val vlr = valor.replace(",", ".").toDoubleOrNull() ?: 0.0
                if (produto.isBlank() || qtd <= 0 || vlr <= 0.0) {
                    erro = "Preencha todos os campos corretamente."
                } else {
                    listaVendas.add(Venda(produto, qtd, vlr))
                    produto = ""
                    quantidade = ""
                    valor = ""
                    erro = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Salvar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listaVendas) { venda ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Produto: ${venda.produto}", style = MaterialTheme.typography.bodyLarge)
                        Text("Qtd: ${venda.quantidade} | Valor: R$ ${"%.2f".format(venda.valor)}")
                        Text("Subtotal: R$ ${"%.2f".format(venda.quantidade * venda.valor)}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Total: R$ ${"%.2f".format(total)}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
