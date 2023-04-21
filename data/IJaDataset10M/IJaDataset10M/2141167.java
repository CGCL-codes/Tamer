package com.jomijushi.fid.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "fid_comprobante_compra", catalog = "fid", schema = "")
@NamedQueries({ @NamedQuery(name = "FidComprobanteCompra.findAll", query = "SELECT f FROM FidComprobanteCompra f"), @NamedQuery(name = "FidComprobanteCompra.findById", query = "SELECT f FROM FidComprobanteCompra f WHERE f.id = :id"), @NamedQuery(name = "FidComprobanteCompra.findByFecha", query = "SELECT f FROM FidComprobanteCompra f WHERE f.fecha = :fecha"), @NamedQuery(name = "FidComprobanteCompra.findByNumComprobante", query = "SELECT f FROM FidComprobanteCompra f WHERE f.numComprobante = :numComprobante"), @NamedQuery(name = "FidComprobanteCompra.findByIgv", query = "SELECT f FROM FidComprobanteCompra f WHERE f.igv = :igv"), @NamedQuery(name = "FidComprobanteCompra.findByEstado", query = "SELECT f FROM FidComprobanteCompra f WHERE f.estado = :estado"), @NamedQuery(name = "FidComprobanteCompra.findByCreatedAt", query = "SELECT f FROM FidComprobanteCompra f WHERE f.createdAt = :createdAt"), @NamedQuery(name = "FidComprobanteCompra.findByUpdatedAt", query = "SELECT f FROM FidComprobanteCompra f WHERE f.updatedAt = :updatedAt") })
public class FidComprobanteCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "numComprobante")
    private Integer numComprobante;

    @Column(name = "igv")
    private Double igv;

    @Column(name = "estado")
    private String estado;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fidComprobanteCompra")
    private List<FidPedidoCompraComprobanteCompra> fidPedidoCompraComprobanteCompraList;

    @JoinColumn(name = "fid_tipo_comprobante_id", referencedColumnName = "id")
    @ManyToOne
    private FidTipoComprobante fidTipoComprobanteId;

    public FidComprobanteCompra() {
    }

    public FidComprobanteCompra(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(Integer numComprobante) {
        this.numComprobante = numComprobante;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public List<FidPedidoCompraComprobanteCompra> getFidPedidoCompraComprobanteCompraList() {
        return fidPedidoCompraComprobanteCompraList;
    }

    public void setFidPedidoCompraComprobanteCompraList(List<FidPedidoCompraComprobanteCompra> fidPedidoCompraComprobanteCompraList) {
        this.fidPedidoCompraComprobanteCompraList = fidPedidoCompraComprobanteCompraList;
    }

    public FidTipoComprobante getFidTipoComprobanteId() {
        return fidTipoComprobanteId;
    }

    public void setFidTipoComprobanteId(FidTipoComprobante fidTipoComprobanteId) {
        this.fidTipoComprobanteId = fidTipoComprobanteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FidComprobanteCompra)) {
            return false;
        }
        FidComprobanteCompra other = (FidComprobanteCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jomijushi.fid.dominio.FidComprobanteCompra[id=" + id + "]";
    }
}
