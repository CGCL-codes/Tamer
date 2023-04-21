package com.inout.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pablo
 */
@Entity
@Table(name = "TARJETA")
@NamedQueries({ @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t"), @NamedQuery(name = "Tarjeta.findById", query = "SELECT t FROM Tarjeta t WHERE t.id = :id"), @NamedQuery(name = "Tarjeta.findByTipo", query = "SELECT t FROM Tarjeta t WHERE t.tipo = :tipo"), @NamedQuery(name = "Tarjeta.findByDescripcion", query = "SELECT t FROM Tarjeta t WHERE t.descripcion = :descripcion"), @NamedQuery(name = "Tarjeta.findByFechaEntrega", query = "SELECT t FROM Tarjeta t WHERE t.fechaEntrega = :fechaEntrega"), @NamedQuery(name = "Tarjeta.findByFechaDevolucion", query = "SELECT t FROM Tarjeta t WHERE t.fechaDevolucion = :fechaDevolucion"), @NamedQuery(name = "Tarjeta.findByActiva", query = "SELECT t FROM Tarjeta t WHERE t.activa = :activa") })
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;

    @Basic(optional = false)
    @Column(name = "TIPO")
    private short tipo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "FECHA_ENTREGA")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;

    @Column(name = "FECHA_DEVOLUCION")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;

    @Basic(optional = false)
    @Column(name = "ACTIVA")
    private boolean activa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarjeta")
    private Collection<Persona> personaCollection;

    public Tarjeta() {
    }

    public Tarjeta(String id) {
        this.id = id;
    }

    public Tarjeta(String id, short tipo, Date fechaEntrega, boolean activa) {
        this.id = id;
        this.tipo = tipo;
        this.fechaEntrega = fechaEntrega;
        this.activa = activa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Collection<Persona> getPersonaCollection() {
        return personaCollection;
    }

    public void setPersonaCollection(Collection<Persona> personaCollection) {
        this.personaCollection = personaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.inout.entities.Tarjeta[id=" + id + "]";
    }
}
