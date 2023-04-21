package edu.cibertec.alquiler.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the tb_categoria database table.
 * 
 */
@Entity
@Table(name = "tb_categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_categoria")
    private int codCategoria;

    private String categoria;

    @OneToMany(mappedBy = "tbCategoria")
    private Set<Video> tbVideos;

    public Categoria() {
    }

    public int getCodCategoria() {
        return this.codCategoria;
    }

    public void setCodCategoria(int codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Set<Video> getTbVideos() {
        return this.tbVideos;
    }

    public void setTbVideos(Set<Video> tbVideos) {
        this.tbVideos = tbVideos;
    }
}
