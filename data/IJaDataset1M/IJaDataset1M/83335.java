package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "CATEGORIA")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c"), @NamedQuery(name = "Categoria.findByCategoriaId", query = "SELECT c FROM Categoria c WHERE c.categoriaId = :categoriaId"), @NamedQuery(name = "Categoria.findByNombre", query = "SELECT c FROM Categoria c WHERE c.nombre = :nombre"), @NamedQuery(name = "Categoria.findByDescripcion", query = "SELECT c FROM Categoria c WHERE c.descripcion = :descripcion"), @NamedQuery(name = "Categoria.findByCategoriaFk", query = "SELECT c FROM Categoria c WHERE c.categoriaFk = :categoria") })
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORIA_ID")
    private Integer categoriaId;

    @Column(name = "NOMBRE")
    private String nombre;

    @Size(max = 45)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaFk")
    private List<Publicacion> publicacionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaFk")
    private List<Categoria> categoriaList;

    @JoinColumn(name = "CATEGORIA_FK", referencedColumnName = "CATEGORIA_ID")
    @ManyToOne(optional = true)
    private Categoria categoriaFk;

    public Categoria() {
        categoriaList = new ArrayList<Categoria>();
        publicacionList = new ArrayList<Publicacion>();
    }

    public Categoria(Integer categoriaId) {
        this();
        this.categoriaId = categoriaId;
    }

    public Categoria(Integer categoriaId, String nombre) {
        this();
        this.categoriaId = categoriaId;
        this.nombre = nombre;
    }

    public void agregarSubcategoria(Categoria subcategoria) {
        categoriaList.add(subcategoria);
        subcategoria.setCategoriaFk(this);
    }

    public Categoria removerSubcategoria(Categoria subcategoria) {
        categoriaList.remove(subcategoria);
        return subcategoria;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Publicacion> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<Publicacion> publicacionList) {
        this.publicacionList = publicacionList;
    }

    @XmlTransient
    public List<Categoria> getCategoriaList() {
        return categoriaList;
    }

    public void setCategoriaList(List<Categoria> categoriaList) {
        this.categoriaList = categoriaList;
    }

    public Categoria getCategoriaFk() {
        return categoriaFk;
    }

    public void setCategoriaFk(Categoria categoriaFk) {
        this.categoriaFk = categoriaFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriaId != null ? categoriaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.categoriaId == null && other.categoriaId != null) || (this.categoriaId != null && !this.categoriaId.equals(other.categoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
