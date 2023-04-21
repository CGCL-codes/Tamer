package org.ugc.cnel.manabi.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "tipo_servicio", catalog = "", schema = "public")
@NamedQueries({ @NamedQuery(name = "TipoServicio.findAll", query = "SELECT t FROM TipoServicio t"), @NamedQuery(name = "TipoServicio.findById", query = "SELECT t FROM TipoServicio t WHERE t.id = :id"), @NamedQuery(name = "TipoServicio.findByDetalle", query = "SELECT t FROM TipoServicio t WHERE t.detalle = :detalle"), @NamedQuery(name = "TipoServicio.findByEstado", query = "SELECT t FROM TipoServicio t WHERE t.estado = :estado") })
public class TipoServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "detalle", nullable = false, length = 200)
    private String detalle;

    @Basic(optional = false)
    @Column(name = "estado", nullable = false)
    private boolean estado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoServicio")
    private Collection<Servicio> servicioCollection;

    public TipoServicio() {
    }

    public TipoServicio(Integer id) {
        this.id = id;
    }

    public TipoServicio(Integer id, String detalle, boolean estado) {
        this.id = id;
        this.detalle = detalle;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Collection<Servicio> getServicioCollection() {
        return servicioCollection;
    }

    public void setServicioCollection(Collection<Servicio> servicioCollection) {
        this.servicioCollection = servicioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoServicio)) {
            return false;
        }
        TipoServicio other = (TipoServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ugc.cnel.manabi.entidades.TipoServicio[id=" + id + "]";
    }
}
