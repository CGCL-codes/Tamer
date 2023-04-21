package org.attacmadrid.sgss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ivan
 */
@Entity
@Table(name = "ACTIVIDAD")
@NamedQueries({ @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"), @NamedQuery(name = "Actividad.findById", query = "SELECT a FROM Actividad a WHERE a.id = :id"), @NamedQuery(name = "Actividad.findByDescripcion", query = "SELECT a FROM Actividad a WHERE a.descripcion = :descripcion"), @NamedQuery(name = "Actividad.findByFechaInicio", query = "SELECT a FROM Actividad a WHERE a.fechaInicio = :fechaInicio"), @NamedQuery(name = "Actividad.findByFechaFin", query = "SELECT a FROM Actividad a WHERE a.fechaFin = :fechaFin"), @NamedQuery(name = "Actividad.findByLugar", query = "SELECT a FROM Actividad a WHERE a.lugar = :lugar") })
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DESCRIPCION", length = 256)
    private String descripcion;

    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Column(name = "LUGAR", length = 256)
    private String lugar;

    @JoinTable(name = "PERSONA_ACTIVIDAD", joinColumns = { @JoinColumn(name = "ID_ACTIVIDAD", referencedColumnName = "ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID", nullable = false) })
    @ManyToMany
    private List<Person> personaCollection;

    public Actividad() {
    }

    public Actividad(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public List<Person> getPersonaCollection() {
        return personaCollection;
    }

    public void setPersonaCollection(List<Person> personaCollection) {
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
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.attacmadrid.sgss.entity.Actividad[id=" + id + "]";
    }
}
