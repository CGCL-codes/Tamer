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
@Table(name = "EX_MAQUINARIA", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "Maquinaria.findAll", query = "SELECT e FROM Maquinaria e"), @NamedQuery(name = "Maquinaria.findById", query = "SELECT e FROM Maquinaria e WHERE e.id = :id"), @NamedQuery(name = "Maquinaria.findByMarca", query = "SELECT e FROM Maquinaria e WHERE e.marca LIKE :marca"), @NamedQuery(name = "Maquinaria.findByModelo", query = "SELECT e FROM Maquinaria e WHERE e.modelo LIKE :modelo"), @NamedQuery(name = "Maquinaria.findByVersion", query = "SELECT e FROM Maquinaria e WHERE e.version = :version"), @NamedQuery(name = "Maquinaria.findByCv", query = "SELECT e FROM Maquinaria e WHERE e.cv = :cv"), @NamedQuery(name = "Maquinaria.findByCilindrada", query = "SELECT e FROM Maquinaria e WHERE e.cilindrada = :cilindrada"), @NamedQuery(name = "Maquinaria.findByCosteHora", query = "SELECT e FROM Maquinaria e WHERE e.costeHora = :costeHora"), @NamedQuery(name = "Maquinaria.findByFecAlta", query = "SELECT e FROM Maquinaria e WHERE e.fecAlta = :fecAlta"), @NamedQuery(name = "Maquinaria.findByFecBaja", query = "SELECT e FROM Maquinaria e WHERE e.fecBaja = :fecBaja"), @NamedQuery(name = "Maquinaria.findByValorCompra", query = "SELECT e FROM Maquinaria e WHERE e.valorCompra = :valorCompra"), @NamedQuery(name = "Maquinaria.findByValorResidual", query = "SELECT e FROM Maquinaria e WHERE e.valorResidual = :valorResidual") })
public class Maquinaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "MARCA", nullable = false, length = 100)
    private String marca;

    @Column(name = "MODELO", length = 100)
    private String modelo;

    @Column(name = "VERSION", length = 100)
    private String version;

    @Column(name = "CV")
    private Integer cv;

    @Column(name = "CILINDRADA")
    private Integer cilindrada;

    @Basic(optional = false)
    @Column(name = "COSTE_HORA", nullable = false)
    private double costeHora;

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

    @JoinColumn(name = "TIPO", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private TipoMaquinaria tipo;

    public Maquinaria() {
    }

    public Maquinaria(Integer id) {
        this.id = id;
    }

    public Maquinaria(Integer id, String marca, double costeHora, Date fecAlta, double valorCompra) {
        this.id = id;
        this.marca = marca;
        this.costeHora = costeHora;
        this.fecAlta = fecAlta;
        this.valorCompra = valorCompra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getCv() {
        return cv;
    }

    public void setCv(Integer cv) {
        this.cv = cv;
    }

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }

    public double getCosteHora() {
        return costeHora;
    }

    public void setCosteHora(double costeHora) {
        this.costeHora = costeHora;
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

    public TipoMaquinaria getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaquinaria tipo) {
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
        if (!(object instanceof Maquinaria)) {
            return false;
        }
        Maquinaria other = (Maquinaria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ado.Maquinaria[id=" + id + "]";
    }
}
