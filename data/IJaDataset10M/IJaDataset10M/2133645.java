package openfield.entities.explotacion;

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
 * @author shader
 */
@Entity
@Table(name = "EX_EDIFICACIONES", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "Edificacion.findAll", query = "SELECT e FROM Edificacion e"), @NamedQuery(name = "Edificacion.findById", query = "SELECT e FROM Edificacion e WHERE e.id = :id"), @NamedQuery(name = "Edificacion.findByDescripcion", query = "SELECT e FROM Edificacion e WHERE e.descripcion LIKE :descripcion"), @NamedQuery(name = "Edificacion.findByUd", query = "SELECT e FROM Edificacion e WHERE e.ud = :ud"), @NamedQuery(name = "Edificacion.findByMagnitud", query = "SELECT e FROM Edificacion e WHERE e.magnitud = :magnitud"), @NamedQuery(name = "Edificacion.findByFecAlta", query = "SELECT e FROM Edificacion e WHERE e.fecAlta = :fecAlta"), @NamedQuery(name = "Edificacion.findByFecBaja", query = "SELECT e FROM Edificacion e WHERE e.fecBaja = :fecBaja"), @NamedQuery(name = "Edificacion.findByValorCompra", query = "SELECT e FROM Edificacion e WHERE e.valorCompra = :valorCompra"), @NamedQuery(name = "Edificacion.findByValorResidual", query = "SELECT e FROM Edificacion e WHERE e.valorResidual = :valorResidual") })
public class Edificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "UD", nullable = false)
    private double ud;

    @Basic(optional = false)
    @Column(name = "MAGNITUD", nullable = false)
    private short magnitud;

    @Basic(optional = false)
    @Column(name = "FEC_ALTA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecAlta;

    @Column(name = "FEC_BAJA")
    @Temporal(TemporalType.DATE)
    private Date fecBaja;

    @Basic(optional = false)
    @Column(name = "VALOR_COMPRA", nullable = false)
    private double valorCompra;

    @Column(name = "VALOR_RESIDUAL", precision = 52)
    private Double valorResidual;

    @Column(name = "NOTAS", length = 255)
    private String notas;

    @JoinColumn(name = "TIPO", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private TipoEdificacion tipo;

    public Edificacion() {
    }

    public Edificacion(Integer id) {
        this.id = id;
    }

    public Edificacion(Integer id, double ud, short magnitud, Date fecAlta, double valorCompra) {
        this.id = id;
        this.ud = ud;
        this.magnitud = magnitud;
        this.fecAlta = fecAlta;
        this.valorCompra = valorCompra;
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

    public double getUd() {
        return ud;
    }

    public void setUd(double ud) {
        this.ud = ud;
    }

    public short getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(short magnitud) {
        this.magnitud = magnitud;
    }

    public Date getFecAlta() {
        return fecAlta;
    }

    public void setFecAlta(Date fecAlta) {
        this.fecAlta = fecAlta;
    }

    public Date getFecBaja() {
        return fecBaja;
    }

    public void setFecBaja(Date fecBaja) {
        this.fecBaja = fecBaja;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public Double getValorResidual() {
        return valorResidual;
    }

    public void setValorResidual(Double valorResidual) {
        this.valorResidual = valorResidual;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public TipoEdificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEdificacion tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Edificacion)) {
            return false;
        }
        Edificacion other = (Edificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ado.Edificacion[id=" + id + "]";
    }
}
