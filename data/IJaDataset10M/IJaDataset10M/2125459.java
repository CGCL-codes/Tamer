package br.ujr.scorecard.model.reports.totalcontacontabil;

import java.math.BigDecimal;
import br.ujr.scorecard.util.Util;

public class TotalContaContabilValuePassivo implements Comparable<TotalContaContabilValuePassivo> {

    private TotalContaContabilValue parent;

    private String descricaoReferencia;

    private Long referencia;

    private String vencimento;

    private String valor;

    private String historico;

    private String tipoPassivo;

    private String descricaoParcela;

    public String getDescricaoParcela() {
        return descricaoParcela;
    }

    public void setDescricaoParcela(String descricaoParcela) {
        this.descricaoParcela = descricaoParcela;
    }

    public String getTipoPassivo() {
        return tipoPassivo;
    }

    public void setTipoPassivo(String tipoPassivo) {
        this.tipoPassivo = tipoPassivo;
    }

    public TotalContaContabilValuePassivo(TotalContaContabilValue parent) {
        this.parent = parent;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public int compareTo(TotalContaContabilValuePassivo o) {
        return this.getReferencia().compareTo(o.getReferencia());
    }

    public String getDescricaoReferencia() {
        return descricaoReferencia;
    }

    public void setDescricaoReferencia(String descricaoReferencia) {
        this.descricaoReferencia = descricaoReferencia;
    }

    public TotalContaContabilValue getParent() {
        return parent;
    }

    public void setParent(TotalContaContabilValue parent) {
        this.parent = parent;
    }

    public String getTotal() {
        BigDecimal valor = this.getParent().getValorPorMes().get(this.referencia);
        if (valor != null) return Util.formatCurrency(valor);
        return "";
    }
}
