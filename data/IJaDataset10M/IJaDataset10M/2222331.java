package br.edu.logatti.tcc.ddd.dominio.modelo.produto;

import br.edu.logatti.tcc.ddd.dominio.modelo.marca.Marca;
import br.edu.logatti.tcc.ddd.dominio.modelo.comum.EntidadeComum;
import br.edu.logatti.tcc.ddd.dominio.modelo.fornecedor.Fornecedor;
import br.edu.logatti.tcc.ddd.dominio.modelo.tipoproduto.TipoProduto;

public class Produto extends EntidadeComum {

    private Marca marca;

    private Fornecedor fornecedor;

    private TipoProduto tipoProduto;

    private Situacao situacao;

    private Embalagem embalagem;

    private Float precoUnitario;

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Fornecedor getFornecedor() {
        return this.fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    /**
     * @return the embalagem
     */
    public Embalagem getEmbalagem() {
        return embalagem;
    }

    /**
     * @param embalagem the embalagem to set
     */
    public void setEmbalagem(Embalagem embalagem) {
        this.embalagem = embalagem;
    }
}
