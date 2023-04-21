package gov.gemat.sejus.beans;

import java.util.Date;

public class NotaEmpenhoBean {

    private Integer idNotaEmpenho;

    private String numero;

    private Date dataExpedicao;

    private int prazoEntrega;

    private String licitacao;

    private Double valor;

    private String referencia;

    private Date dataEnvio;

    private String confirmadoCom;

    private Date dataConfirmacao;

    private String observacao;

    private String ordemBancaria;

    private Date dataPagamento;

    private String notaFiscal;

    private Integer idModalidade;

    private Integer idProcesso;

    private Integer idContato;

    public final Integer getIdNotaEmpenho() {
        return idNotaEmpenho;
    }

    public final void setIdNotaEmpenho(Integer idNotaEmpenho) {
        this.idNotaEmpenho = idNotaEmpenho;
    }

    public final String getNumero() {
        return numero;
    }

    public final void setNumero(String numero) {
        this.numero = numero;
    }

    public final Date getDataExpedicao() {
        return dataExpedicao;
    }

    public final void setDataExpedicao(Date dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
    }

    public final int getPrazoEntrega() {
        return prazoEntrega;
    }

    public final void setPrazoEntrega(int prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public final String getLicitacao() {
        return licitacao;
    }

    public final void setLicitacao(String licitacao) {
        this.licitacao = licitacao;
    }

    public final Double getValor() {
        return valor;
    }

    public final void setValor(Double valor) {
        this.valor = valor;
    }

    public final String getReferencia() {
        return referencia;
    }

    public final void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public final Date getDataEnvio() {
        return dataEnvio;
    }

    public final void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public final String getConfirmadoCom() {
        return confirmadoCom;
    }

    public final void setConfirmadoCom(String confirmadoCom) {
        this.confirmadoCom = confirmadoCom;
    }

    public final Date getDataConfirmaaoo() {
        return dataConfirmacao;
    }

    public final void setDataConfirmacao(Date dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }

    public final String getObservacao() {
        return observacao;
    }

    public final void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public final Integer getIdModalidade() {
        return idModalidade;
    }

    public final void setIdModalidade(Integer idModalidade) {
        this.idModalidade = idModalidade;
    }

    public final Integer getIdProcesso() {
        return idProcesso;
    }

    public final void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }

    public final Integer getIdContato() {
        return idContato;
    }

    public final void setIdContato(Integer idContato) {
        this.idContato = idContato;
    }

    public final String getOrdemBancaria() {
        return ordemBancaria;
    }

    public final void setOrdemBancaria(String ordemBancaria) {
        this.ordemBancaria = ordemBancaria;
    }

    public final Date getDataPagamento() {
        return dataPagamento;
    }

    public final void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public final String getNotaFiscal() {
        return notaFiscal;
    }

    public final void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}
