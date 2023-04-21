package model;

import util.LivrariaUtils;

/**
 * @author Eve
 * 
 */
public class Produto {

    private int idProduto;

    private String nomeProduto;

    private double preco;

    private int qtdeDisponivel;

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdeDisponivel() {
        return qtdeDisponivel;
    }

    public void setQtdeDisponivel(int qtdeDisponivel) {
        this.qtdeDisponivel = qtdeDisponivel;
    }

    public String retornaPreco() {
        return LivrariaUtils.formatarDinheiro(this.preco);
    }

    public String toString() {
        return this.nomeProduto;
    }
}
