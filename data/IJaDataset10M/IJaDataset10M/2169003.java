package br.com.locadora.bean;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the locacoes database table.
 * 
 */
@Entity
@Table(name = "locacoes")
public class Locacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "seq_id_locacoes", sequenceName = "seq_id_locacoes", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_locacoes")
    @Column(name = "id_locacao")
    private Integer idLocacao;

    @Column(name = "data_entrega")
    private String dataEntrega;

    @Column(name = "data_locacao")
    private String dataLocacao;

    private Boolean devolvido;

    @Column(name = "preco_tocal")
    private double precoTocal;

    @Column(name = "preco_unit")
    private double precoUnit;

    @OneToMany(mappedBy = "locacoes")
    private List<ItensLocacao> itensLocacoes;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Locacao() {
    }

    public Integer getIdLocacao() {
        return this.idLocacao;
    }

    public void setIdLocacao(Integer idLocacao) {
        this.idLocacao = idLocacao;
    }

    public String getDataEntrega() {
        return this.dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getDataLocacao() {
        return this.dataLocacao;
    }

    public void setDataLocacao(String dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public Boolean getDevolvido() {
        return this.devolvido;
    }

    public void setDevolvido(Boolean devolvido) {
        this.devolvido = devolvido;
    }

    public double getPrecoTocal() {
        return this.precoTocal;
    }

    public void setPrecoTocal(double precoTocal) {
        this.precoTocal = precoTocal;
    }

    public double getPrecoUnit() {
        return this.precoUnit;
    }

    public void setPrecoUnit(double precoUnit) {
        this.precoUnit = precoUnit;
    }

    public List<ItensLocacao> getItensLocacoes() {
        return this.itensLocacoes;
    }

    public void setItensLocacaos(List<ItensLocacao> itensLocacoes) {
        this.itensLocacoes = itensLocacoes;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
