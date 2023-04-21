package com.ciberiasoluciones.lidia.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author osalcedo
 */
@Entity
@Table(name = "perfiles")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Perfiles.findAll", query = "SELECT p FROM Perfiles p"), @NamedQuery(name = "Perfiles.findByCod", query = "SELECT p FROM Perfiles p WHERE p.cod = :cod"), @NamedQuery(name = "Perfiles.findByDescripcion", query = "SELECT p FROM Perfiles p WHERE p.descripcion = :descripcion") })
public class Perfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod")
    private Integer cod;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private int descripcion;

    public Perfiles() {
    }

    public Perfiles(Integer cod) {
        this.cod = cod;
    }

    public Perfiles(Integer cod, int descripcion) {
        this.cod = cod;
        this.descripcion = descripcion;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public int getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(int descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Perfiles)) {
            return false;
        }
        Perfiles other = (Perfiles) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ciberiasoluciones.lidia.modelo.Perfiles[ cod=" + cod + " ]";
    }
}
