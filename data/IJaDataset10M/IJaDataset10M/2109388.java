package br.edu.uncisal.farmacia.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * Agrupamentos para classificação dos itens do almoxarifado.
 * 
 * @see Item
 * @author Augusto Oliveira
 * @author Igor Cavalcante
 */
@Entity
@SequenceGenerator(name = "SEQ_CLOG", sequenceName = "sq_unidade")
public class Unidade extends Domain implements Serializable {

    private static final long serialVersionUID = 3092732071550363887L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CLOG")
    private Long id;

    @Column(length = 100)
    private String nome;

    @ManyToOne
    private Orgao orgao;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }

    @Override
    public String toString() {
        return "[id: " + id + " nome: " + nome + " orgão: " + (orgao != null ? orgao.toString() : null) + "]";
    }
}
