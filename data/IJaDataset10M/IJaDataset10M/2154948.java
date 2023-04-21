package org.weras.portal.clientes.client.modulos.cliente;

import static org.weras.portal.clientes.client.comum.util.StringUtils.valueOf;
import java.util.ArrayList;
import java.util.List;
import org.weras.portal.clientes.client.comum.Conversor;
import org.weras.portal.clientes.client.comum.Formulario;
import org.weras.portal.clientes.client.modulos.comum.UtilClientes;
import org.weras.portal.clientes.client.modulos.comum.UtilItemNegociavel;
import org.weras.portal.clientes.client.modulos.comum.UtilPessoa;
import org.weras.portal.clientes.client.modulos.comum.UtilPessoaFisica;
import org.weras.portal.clientes.domain.app.Clientes;
import org.weras.portal.clientes.domain.app.Planos;
import org.weras.portal.clientes.domain.app.unibells.ClienteUnibells;
import org.weras.portal.clientes.domain.app.unibells.ClienteUnibellsTO;
import org.weras.portal.clientes.domain.catalogo.ItemNegociavel;
import org.weras.portal.clientes.domain.pessoa.Pessoa;
import org.weras.portal.clientes.domain.pessoa.fisica.PessoaFisica;

public class ConversorClientes implements Conversor<ClienteUnibellsTO, PesquisaCliente> {

    public String[][] gerarTabela(List<ClienteUnibellsTO> pessoas) {
        if (pessoas == null || pessoas.size() == 0) {
            return new String[0][0];
        }
        int tamanho = pessoas.size();
        String[][] resultado = new String[tamanho][34];
        for (int i = 0; i < tamanho; i++) {
            ClienteUnibellsTO dadosCliente = pessoas.get(i);
            ClienteUnibells cliente = dadosCliente.getCliente();
            PessoaFisica pessoaFisica = cliente.getPessoaFisica();
            Pessoa pessoa = pessoaFisica.getPessoa();
            int coluna = 0;
            resultado[i][coluna++] = valueOf(cliente.getId());
            coluna = UtilPessoa.povoarPessoa(resultado, i, pessoa, coluna);
            coluna = UtilPessoaFisica.povoarPessoaFisica(resultado, i, coluna, pessoaFisica, true, true, true);
            coluna = UtilItemNegociavel.povoarItemNegociavel(resultado, i, dadosCliente.getCliente().getPlano(), coluna, Planos.CAMPO_NOME);
            coluna = povoarPendencia(resultado, i, dadosCliente, coluna);
            coluna = UtilClientes.povoarResponsavel(resultado, i, cliente, coluna);
            resultado[i][coluna++] = valueOf(dadosCliente.getCliente().getDiaVencimento());
        }
        return resultado;
    }

    private int povoarPendencia(String[][] resultado, int linha, ClienteUnibellsTO clienteUnibells, int coluna) {
        if (clienteUnibells.isPendente()) {
            resultado[linha][coluna++] = Clientes.STATUS_PENDENTE_DE_REGULARIZAÇÃO;
        } else {
            resultado[linha][coluna++] = Clientes.STATUS_REGULARIZADO;
        }
        return coluna;
    }

    public ClienteUnibellsTO extrair(Formulario<ClienteUnibellsTO, PesquisaCliente> formulario) {
        Pessoa pessoa = UtilPessoa.extrairPessoa(formulario, false);
        PessoaFisica pessoaFisica = UtilPessoaFisica.extrairPessoaFisica(formulario);
        pessoaFisica.setPessoa(pessoa);
        ClienteUnibells cliente = UtilClientes.extrairCliente(formulario);
        cliente.setPessoaFisica(pessoaFisica);
        ClienteUnibellsTO dadosCliente = new ClienteUnibellsTO();
        ItemNegociavel plano = formulario.criarReferencia(Clientes.CAMPO_ID_PLANO, new ItemNegociavel());
        dadosCliente.setCliente(cliente);
        dadosCliente.getCliente().setPlano(plano);
        dadosCliente.getCliente().setDiaVencimento(formulario.getValorInteiro(Clientes.CAMPO_DIA_VENCIMENTO));
        FormularioCliente formularioCliente = (FormularioCliente) formulario;
        DependentesUI dependentesUI = formularioCliente.getDependentesUI();
        List<ClienteUnibells> dependentes = dependentesUI.getDados();
        cliente.setDependentes(new ArrayList<ClienteUnibells>());
        cliente.getDependentes().addAll(dependentes);
        return dadosCliente;
    }
}
