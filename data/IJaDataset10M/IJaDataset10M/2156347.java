package br.absolut.apresentacao.venda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.absolut.apresentacao.compra.DtoItem;

public class DtoVenda implements Serializable {

    private static final long serialVersionUID = -3314985629961278967L;

    private Long cod;

    private Date dtPedido;

    private Long codUsuario;

    private Long codCliente;

    private Long codPagamento;

    private String cpfCliente;

    private String nomeCliente;

    private String nomeUsuario;

    private String subTotal;

    private String desconto;

    private String total;

    private String qtdProdutos;

    private List<DtoItem> listaItem = new ArrayList<DtoItem>();

    public Long getCodPedido() {
        return cod;
    }

    public void setCodPedido(Long codPedido) {
        this.cod = codPedido;
    }

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtPedido) {
        this.dtPedido = dtPedido;
    }

    public Long getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Long codCliente) {
        this.codCliente = codCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQtdProdutos() {
        return qtdProdutos;
    }

    public void setQtdProdutos(String qtdProdutos) {
        this.qtdProdutos = qtdProdutos;
    }

    public List<DtoItem> getListaItem() {
        return listaItem;
    }

    public void setListaItem(List<DtoItem> listaItem) {
        this.listaItem = listaItem;
    }

    public Long getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Long codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Long getCodPagamento() {
        return codPagamento;
    }

    public void setCodPagamento(Long codPagamento) {
        this.codPagamento = codPagamento;
    }
}
