package com.jomijushi.fid.dominio;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "fid_tipo_impresion", catalog = "fid", schema = "")
@NamedQueries({ @NamedQuery(name = "FidTipoImpresion.findAll", query = "SELECT f FROM FidTipoImpresion f"), @NamedQuery(name = "FidTipoImpresion.findById", query = "SELECT f FROM FidTipoImpresion f WHERE f.id = :id"), @NamedQuery(name = "FidTipoImpresion.findByNombre", query = "SELECT f FROM FidTipoImpresion f WHERE f.nombre = :nombre"), @NamedQuery(name = "FidTipoImpresion.findByCreatedAt", query = "SELECT f FROM FidTipoImpresion f WHERE f.createdAt = :createdAt"), @NamedQuery(name = "FidTipoImpresion.findByUpdatedAt", query = "SELECT f FROM FidTipoImpresion f WHERE f.updatedAt = :updatedAt") })
public class FidTipoImpresion implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "fidTipoImpresionId")
    private List<FidBobina> fidBobinaList;

    public FidTipoImpresion() {
    }

    public FidTipoImpresion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        String oldNombre = this.nombre;
        this.nombre = nombre;
        changeSupport.firePropertyChange("nombre", oldNombre, nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        String oldDescripcion = this.descripcion;
        this.descripcion = descripcion;
        changeSupport.firePropertyChange("descripcion", oldDescripcion, descripcion);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        Date oldCreatedAt = this.createdAt;
        this.createdAt = createdAt;
        changeSupport.firePropertyChange("createdAt", oldCreatedAt, createdAt);
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        Date oldUpdatedAt = this.updatedAt;
        this.updatedAt = updatedAt;
        changeSupport.firePropertyChange("updatedAt", oldUpdatedAt, updatedAt);
    }

    public List<FidBobina> getFidBobinaList() {
        return fidBobinaList;
    }

    public void setFidBobinaList(List<FidBobina> fidBobinaList) {
        this.fidBobinaList = fidBobinaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FidTipoImpresion)) {
            return false;
        }
        FidTipoImpresion other = (FidTipoImpresion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jomijushi.fid.dominio.FidTipoImpresion[id=" + id + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
