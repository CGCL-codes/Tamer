package edu.estacio.siscope.bean;

import java.sql.Date;

public class PedidoSituacao {

    private Date dataSituacao;

    private String dataSituacaoFormatada;

    private Funcionario funcionario;

    private Situacao situacao;

    private Pedido pedido;

    private String observacao;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Date getDataSituacao() {
        return dataSituacao;
    }

    public void setDataSituacao(Date dataSituacao) {
        this.dataSituacao = dataSituacao;
    }

    public String getDataSituacaoFormatada() {
        return dataSituacaoFormatada;
    }

    public void setDataSituacaoFormatada(String dataSituacaoFormatada) {
        this.dataSituacaoFormatada = dataSituacaoFormatada;
    }
}
