package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the tbconjuge database table.
 * 
 */
@Entity
@Table(name = "tbconjuge")
public class Tbconjuge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 255)
    private String conjuge;

    @Column(length = 255)
    private String nome;

    @Column(length = 255)
    private String origem;

    @Column(length = 255)
    private String tipoorigem;

    public Tbconjuge() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConjuge() {
        return this.conjuge;
    }

    public void setConjuge(String conjuge) {
        this.conjuge = conjuge;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getOrigem() {
        return this.origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getTipoorigem() {
        return this.tipoorigem;
    }

    public void setTipoorigem(String tipoorigem) {
        this.tipoorigem = tipoorigem;
    }
}
