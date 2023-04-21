package modelo;

/**
 * Modelo da entidade que representa o Cabeçalho Compra
 * @author Paulo Ilenga
 */
public class CompraItensModelo {

    private int id, idCompraHeader, idProduto, quatidade;

    private float imposto, preco;

    private String produto;

    private float total;

    public CompraItensModelo() {
    }

    public CompraItensModelo(int id, int idCompraHeader, int idProduto, int quatidade, float imposto, float preco) {
        this.id = id;
        this.idCompraHeader = idCompraHeader;
        this.idProduto = idProduto;
        this.quatidade = quatidade;
        this.imposto = imposto;
        this.preco = preco;
    }

    public CompraItensModelo(int id, int idCompraHeader, int idProduto, int quatidade, float imposto, float preco, String produto) {
        this.id = id;
        this.idCompraHeader = idCompraHeader;
        this.idProduto = idProduto;
        this.quatidade = quatidade;
        this.imposto = imposto;
        this.preco = preco;
        this.produto = produto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCompraHeader() {
        return idCompraHeader;
    }

    public void setIdCompraHeader(int idCompraHeader) {
        this.idCompraHeader = idCompraHeader;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public float getImposto() {
        return imposto;
    }

    public void setImposto(float imposto) {
        this.imposto = imposto;
    }

    public int getQuatidade() {
        return quatidade;
    }

    public void setQuatidade(int quatidade) {
        this.quatidade = quatidade;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public float getTotal() {
        total = preco * quatidade;
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
