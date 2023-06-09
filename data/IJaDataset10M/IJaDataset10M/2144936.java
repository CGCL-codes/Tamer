package br.com.wepa.webapps.orca.logica.modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Orcamento generated by hbm2java
 */
@Entity
@Table(name = "orcamento", catalog = "Orca", uniqueConstraints = {  })
public class Orcamento implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3888750389773460559L;

    private int idOrcamento;

    private Fornecedor fornecedor;

    private Usuario usuario;

    private Date dtOrcamento;

    private Boolean vencedor;

    private String observacao;

    private Double frete;

    private Date dtCadOrcamento;

    private Set<Produto> produtos = new HashSet<Produto>(0);

    /** default constructor */
    public Orcamento() {
    }

    /** minimal constructor */
    public Orcamento(int idOrcamento, Fornecedor fornecedor, Usuario usuario, Date dtCadOrcamento) {
        this.idOrcamento = idOrcamento;
        this.fornecedor = fornecedor;
        this.usuario = usuario;
        this.dtCadOrcamento = dtCadOrcamento;
    }

    /** full constructor */
    public Orcamento(int idOrcamento, Fornecedor fornecedor, Usuario usuario, Date dtOrcamento, Boolean vencedor, String observacao, Double frete, Date dtCadOrcamento, Set<Produto> produtos) {
        this.idOrcamento = idOrcamento;
        this.fornecedor = fornecedor;
        this.usuario = usuario;
        this.dtOrcamento = dtOrcamento;
        this.vencedor = vencedor;
        this.observacao = observacao;
        this.frete = frete;
        this.dtCadOrcamento = dtCadOrcamento;
        this.produtos = produtos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idOrcamento", unique = true, nullable = false, insertable = true, updatable = true)
    public int getIdOrcamento() {
        return this.idOrcamento;
    }

    public void setIdOrcamento(int idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "Fornecedor_idFornecedor", unique = false, nullable = false, insertable = true, updatable = true)
    public Fornecedor getFornecedor() {
        return this.fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuario_idUsuario", unique = false, nullable = true, insertable = true, updatable = true)
    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dtOrcamento", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
    public Date getDtOrcamento() {
        return this.dtOrcamento;
    }

    public void setDtOrcamento(Date dtOrcamento) {
        this.dtOrcamento = dtOrcamento;
    }

    @Column(name = "vencedor", unique = false, nullable = true, insertable = true, updatable = true, precision = 1, scale = 0)
    public Boolean getVencedor() {
        return this.vencedor;
    }

    public void setVencedor(Boolean vencedor) {
        this.vencedor = vencedor;
    }

    @Column(name = "observacao", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
    public String getObservacao() {
        return this.observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Column(name = "frete", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
    public Double getFrete() {
        return this.frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dtCadOrcamento", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
    public Date getDtCadOrcamento() {
        return this.dtCadOrcamento;
    }

    public void setDtCadOrcamento(Date dtCadOrcamento) {
        this.dtCadOrcamento = dtCadOrcamento;
    }

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "orcamento")
    public Set<Produto> getProdutos() {
        return this.produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }
}
