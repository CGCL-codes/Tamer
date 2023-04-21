package edu.ues.jhard.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "cicloanyo")
@NamedQueries({ @NamedQuery(name = "Cicloanyo.findAll", query = "SELECT c FROM Cicloanyo c"), @NamedQuery(name = "Cicloanyo.findByIdcicloanyo", query = "SELECT c FROM Cicloanyo c WHERE c.idcicloanyo = :idcicloanyo"), @NamedQuery(name = "Cicloanyo.findByDescripcion", query = "SELECT c FROM Cicloanyo c WHERE c.descripcion = :descripcion") })
public class Cicloanyo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "idcicloanyo")
    private Integer idcicloanyo;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcicloanio")
    private List<Curso> cursoCollection;

    public Cicloanyo() {
    }

    public Cicloanyo(Integer idcicloanyo) {
        this.idcicloanyo = idcicloanyo;
    }

    public Cicloanyo(Integer idcicloanyo, String descripcion) {
        this.idcicloanyo = idcicloanyo;
        this.descripcion = descripcion;
    }

    public Integer getIdcicloanyo() {
        return idcicloanyo;
    }

    public void setIdcicloanyo(Integer idcicloanyo) {
        this.idcicloanyo = idcicloanyo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Curso> getCursoCollection() {
        return cursoCollection;
    }

    public void setCursoCollection(List<Curso> cursoCollection) {
        this.cursoCollection = cursoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcicloanyo != null ? idcicloanyo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cicloanyo)) {
            return false;
        }
        Cicloanyo other = (Cicloanyo) object;
        if ((this.idcicloanyo == null && other.idcicloanyo != null) || (this.idcicloanyo != null && !this.idcicloanyo.equals(other.idcicloanyo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.ues.jhard.jpa.Cicloanyo[idcicloanyo=" + idcicloanyo + "]";
    }
}
