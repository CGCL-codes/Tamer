package com.jomijushi.fid.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "fid_proveedor", catalog = "fid", schema = "")
@PrimaryKeyJoinColumn(name = "fid_persona_empresa_id")
@DiscriminatorValue("proveedor")
public class FidProveedor extends FidPersonaEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "cantCompras")
    private Integer cantCompras;

    @Column(name = "fechaUltimaCompra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaCompra;

    @Column(name = "contacto")
    private String contacto;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "fidProveedor")
    private FidProveedorHasFidProductoMercaderia fidProveedorHasFidProductoMercaderia;

    @OneToMany(mappedBy = "fidProveedorFidPersonaEmpresaId")
    private List<FidPedidoCompra> fidPedidoCompraList;

    @JoinColumn(name = "fid_persona_empresa_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private FidPersonaEmpresa fidPersonaEmpresa;

    public FidProveedor() {
    }

    @Override
    public Integer getCantCompras() {
        return cantCompras;
    }

    @Override
    public void setCantCompras(Integer cantCompras) {
        this.cantCompras = cantCompras;
    }

    @Override
    public Date getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    @Override
    public void setFechaUltimaCompra(Date fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    @Override
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String getContacto() {
        return contacto;
    }

    @Override
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    @Override
    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public FidProveedorHasFidProductoMercaderia getFidProveedorHasFidProductoMercaderia() {
        return fidProveedorHasFidProductoMercaderia;
    }

    public void setFidProveedorHasFidProductoMercaderia(FidProveedorHasFidProductoMercaderia fidProveedorHasFidProductoMercaderia) {
        this.fidProveedorHasFidProductoMercaderia = fidProveedorHasFidProductoMercaderia;
    }

    public List<FidPedidoCompra> getFidPedidoCompraList() {
        return fidPedidoCompraList;
    }

    public void setFidPedidoCompraList(List<FidPedidoCompra> fidPedidoCompraList) {
        this.fidPedidoCompraList = fidPedidoCompraList;
    }

    public FidPersonaEmpresa getFidPersonaEmpresa() {
        return fidPersonaEmpresa;
    }

    public void setFidPersonaEmpresa(FidPersonaEmpresa fidPersonaEmpresa) {
        this.fidPersonaEmpresa = fidPersonaEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FidProveedor)) {
            return false;
        }
        FidProveedor other = (FidProveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jomijushi.fid.dominio.FidProveedor[fidPersonaEmpresaId=" + id + "]";
    }
}
