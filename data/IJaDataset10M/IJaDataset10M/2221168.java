package com.jomijushi.fid.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "fid_bobina", catalog = "fid", schema = "")
@NamedQueries({ @NamedQuery(name = "FidBobina.findAll", query = "SELECT f FROM FidBobina f"), @NamedQuery(name = "FidBobina.findById", query = "SELECT f FROM FidBobina f WHERE f.id = :id"), @NamedQuery(name = "FidBobina.findByFechaIngreso", query = "SELECT f FROM FidBobina f WHERE f.fechaIngreso = :fechaIngreso"), @NamedQuery(name = "FidBobina.findByFechaSalida", query = "SELECT f FROM FidBobina f WHERE f.fechaSalida = :fechaSalida"), @NamedQuery(name = "FidBobina.findByAreaUsada", query = "SELECT f FROM FidBobina f WHERE f.areaUsada = :areaUsada"), @NamedQuery(name = "FidBobina.findByAreaRiesgo", query = "SELECT f FROM FidBobina f WHERE f.areaRiesgo = :areaRiesgo"), @NamedQuery(name = "FidBobina.findByCreatedAt", query = "SELECT f FROM FidBobina f WHERE f.createdAt = :createdAt"), @NamedQuery(name = "FidBobina.findByUpdatedAt", query = "SELECT f FROM FidBobina f WHERE f.updatedAt = :updatedAt") })
public class FidBobina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fechaIngreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @Column(name = "fechaSalida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;

    @Column(name = "areaUsada")
    private Double areaUsada;

    @Column(name = "areaRiesgo")
    private Double areaRiesgo;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @JoinColumn(name = "fid_tipo_impresion_id", referencedColumnName = "id")
    @ManyToOne
    private FidTipoImpresion fidTipoImpresionId;

    @JoinColumn(name = "fid_dimension_bobina_id", referencedColumnName = "id")
    @ManyToOne
    private FidDimensionBobina fidDimensionBobinaId;

    @OneToMany(mappedBy = "fidBobinaId")
    private List<FidServicioDefinidoImpresion> fidServicioDefinidoImpresionList;

    @OneToMany(mappedBy = "fidBobinaId")
    private List<FidBobinaUsada> fidBobinaUsadaList;

    public FidBobina() {
    }

    public FidBobina(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Double getAreaUsada() {
        return areaUsada;
    }

    public void setAreaUsada(Double areaUsada) {
        this.areaUsada = areaUsada;
    }

    public Double getAreaRiesgo() {
        return areaRiesgo;
    }

    public void setAreaRiesgo(Double areaRiesgo) {
        this.areaRiesgo = areaRiesgo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public FidTipoImpresion getFidTipoImpresionId() {
        return fidTipoImpresionId;
    }

    public void setFidTipoImpresionId(FidTipoImpresion fidTipoImpresionId) {
        this.fidTipoImpresionId = fidTipoImpresionId;
    }

    public FidDimensionBobina getFidDimensionBobinaId() {
        return fidDimensionBobinaId;
    }

    public void setFidDimensionBobinaId(FidDimensionBobina fidDimensionBobinaId) {
        this.fidDimensionBobinaId = fidDimensionBobinaId;
    }

    public List<FidServicioDefinidoImpresion> getFidServicioDefinidoImpresionList() {
        return fidServicioDefinidoImpresionList;
    }

    public void setFidServicioDefinidoImpresionList(List<FidServicioDefinidoImpresion> fidServicioDefinidoImpresionList) {
        this.fidServicioDefinidoImpresionList = fidServicioDefinidoImpresionList;
    }

    public List<FidBobinaUsada> getFidBobinaUsadaList() {
        return fidBobinaUsadaList;
    }

    public void setFidBobinaUsadaList(List<FidBobinaUsada> fidBobinaUsadaList) {
        this.fidBobinaUsadaList = fidBobinaUsadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FidBobina)) {
            return false;
        }
        FidBobina other = (FidBobina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jomijushi.fid.dominio.FidBobina[id=" + id + "]";
    }
}
