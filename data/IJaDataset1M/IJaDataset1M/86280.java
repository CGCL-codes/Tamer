package entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Pedro
 */
@Entity
@Table(name = "transaccion", catalog = "benjames_banc", schema = "")
@NamedQueries({ @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t"), @NamedQuery(name = "Transaccion.findByIdtransaccion", query = "SELECT t FROM Transaccion t WHERE t.idtransaccion = :idtransaccion"), @NamedQuery(name = "Transaccion.findByFecha", query = "SELECT t FROM Transaccion t WHERE t.fecha = :fecha"), @NamedQuery(name = "Transaccion.findByHora", query = "SELECT t FROM Transaccion t WHERE t.hora = :hora"), @NamedQuery(name = "Transaccion.findByValor", query = "SELECT t FROM Transaccion t WHERE t.valor = :valor") })
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDTRANSACCION", nullable = false)
    private Integer idtransaccion;

    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;

    @Column(name = "VALOR", precision = 12)
    private Float valor;

    @JoinColumn(name = "tipotransaccion_IDTIPOTRANSACCION", referencedColumnName = "IDTIPOTRANSACCION", nullable = false)
    @ManyToOne(optional = false)
    private Tipotransaccion tipotransaccion;

    @JoinColumn(name = "producto_IDPRODUCTO", referencedColumnName = "IDPRODUCTO", nullable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public Transaccion() {
    }

    public Transaccion(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Integer getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdtransaccion(Integer idtransaccion) {
        this.idtransaccion = idtransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Tipotransaccion getTipotransaccion() {
        return tipotransaccion;
    }

    public void setTipotransaccion(Tipotransaccion tipotransaccion) {
        this.tipotransaccion = tipotransaccion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtransaccion != null ? idtransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idtransaccion == null && other.idtransaccion != null) || (this.idtransaccion != null && !this.idtransaccion.equals(other.idtransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Transaccion[idtransaccion=" + idtransaccion + "]";
    }
}
