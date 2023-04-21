package com.cibertec.project.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the tb_tema database table.
 * 
 */
@Entity
@Table(name = "tb_tema")
public class TemaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codTema")
    private int intCodTema;

    @Column(name = "descTema")
    private String strDescTema;

    @Column(name = "nomTema")
    private String strNomTema;

    @OneToMany(mappedBy = "tbTema")
    private Set<ExamenEntity> tbExamens;

    @OneToMany(mappedBy = "tbTema")
    private Set<PreguntaEntity> tbPreguntas;

    public TemaEntity() {
    }

    public int getIntCodTema() {
        return this.intCodTema;
    }

    public void setIntCodTema(int intCodTema) {
        this.intCodTema = intCodTema;
    }

    public String getStrDescTema() {
        return this.strDescTema;
    }

    public void setStrDescTema(String strDescTema) {
        this.strDescTema = strDescTema;
    }

    public String getStrNomTema() {
        return this.strNomTema;
    }

    public void setStrNomTema(String strNomTema) {
        this.strNomTema = strNomTema;
    }

    public Set<ExamenEntity> getTbExamens() {
        return this.tbExamens;
    }

    public void setTbExamens(Set<ExamenEntity> tbExamens) {
        this.tbExamens = tbExamens;
    }

    public Set<PreguntaEntity> getTbPreguntas() {
        return this.tbPreguntas;
    }

    public void setTbPreguntas(Set<PreguntaEntity> tbPreguntas) {
        this.tbPreguntas = tbPreguntas;
    }
}
