package br.eteg.curso.java.bancojava.entidade;

import java.io.Serializable;
import java.util.Calendar;
import br.eteg.curso.java.bancojava.entidade.conta.ContratoContaBancaria;
import br.eteg.curso.java.bancojava.servico.util.DataUtil;
import br.eteg.curso.java.bancojava.servico.util.FormatadorUtil;

public class MovimentacaoBancaria implements Serializable {

    private Calendar data;

    private String descricao;

    private double valor;

    private ContratoContaBancaria conta;

    /**
	 * construtor da movimentacao bancaria
	 * @param data a data da movimentacao
	 * @param descricao a descricao
	 * @param valor o valor da movimentacao
	 * @param conta a conta que sofreu a 
	 * movimentacao
	 */
    public MovimentacaoBancaria(Calendar data, String descricao, double valor, ContratoContaBancaria conta) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.conta = conta;
    }

    public String obterDadosMovimentacaoFormatada() {
        return FormatadorUtil.formatarDataDescEValor(data, descricao, valor);
    }

    public String obterDadosMovimentacao() {
        StringBuilder sb = new StringBuilder();
        sb.append(DataUtil.getDataExtratoFormatada(data));
        sb.append(" - " + descricao + "               ");
        sb.append(String.valueOf(valor) + "\r\n");
        return sb.toString();
    }

    public String obterDadosMovimentacaoHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr><td>");
        sb.append(DataUtil.getDataExtratoFormatada(data)).append("</td><td>");
        sb.append(descricao).append("</td><td>");
        sb.append(String.valueOf(valor)).append("</td></tr>");
        return sb.toString();
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }
}
