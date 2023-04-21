package br.uff.javaavancado.modelos;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author HelioKann
 */
@NamedQueries({ @NamedQuery(name = "Produto.getPorId", query = "select p from Produto p where p.id = ?"), @NamedQuery(name = "Produto.getProdutosPorCategoria", query = "select p from Produto p where p.categoria = ?"), @NamedQuery(name = "Produto.buscaProdutoPorNome", query = "select p from Produto p where upper(p.nome) like ?") })
@Entity
@Table(name = "java_produto")
@SequenceGenerator(name = "Sequencia", sequenceName = "java_produto_cod_seq")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Sequencia")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Categoria categoria;

    private String codigo;

    private String nome;

    private String descricao;

    private String urlFoto;

    private float preco;

    private long estoque;

    public Produto() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getEstoque() {
        return estoque;
    }

    public void setEstoque(long estoque) {
        this.estoque = estoque;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Produto)) return false;
        Produto p = ((Produto) obj);
        if (p.id == null || this.id == null) return false;
        return p.codigo.equals(this.codigo);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }
}
