package openfield.persistence.entities.explotacion;

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
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.TypeConverter;

/**
 *
 * @author Francis
 */
@Entity
@Table(name = "EX_COSTESFIJOS_PLANTACIONES", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "CosteFijoPlantacion.findAll", query = "SELECT f FROM CosteFijoPlantacion f"), @NamedQuery(name = "CosteFijoPlantacion.findById", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.id = :id"), @NamedQuery(name = "CosteFijoPlantacion.findByPlantacion", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.plantacion = :plantacion"), @NamedQuery(name = "CosteFijoPlantacion.findByTipo", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.tipoCosteFijo = :tipoCosteFijo"), @NamedQuery(name = "CosteFijoPlantacion.findByDescripcion", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.descripcion = :descripcion"), @NamedQuery(name = "CosteFijoPlantacion.findByFecha", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.fecha = :fecha"), @NamedQuery(name = "CosteFijoPlantacion.findByNFactura", query = "SELECT f FROM CosteFijoPlantacion f WHERE f.nFactura = :nFactura") })
public class CosteFijoPlantacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JoinColumn(name = "ID_PLANTACION", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Plantacion plantacion;

    @ObjectTypeConverter(name = "tipoCosteFijoPlant", objectType = TipoCosteFijoPlant.class, dataType = String.class, conversionValues = { @ConversionValue(objectValue = "AMORTIZACION", dataValue = "AMORTIZACIONES"), @ConversionValue(objectValue = "MANTENIMIENTO", dataValue = "CONSERVACION Y MANTENIMIENTO"), @ConversionValue(objectValue = "SEGUROS", dataValue = "SEGUROS"), @ConversionValue(objectValue = "OTROS_COSTES", dataValue = "OTROS COSTES") })
    @Column(name = "TIPO_COSTEFIJO", nullable = false)
    @Convert("tipoCosteFijoPlant")
    private TipoCosteFijoPlant tipoCosteFijo;

    @Basic(optional = false)
    @Column(name = "DESCRIPCION", length = 150)
    private String descripcion;

    @TypeConverter(name = "longToDate", objectType = Date.class, dataType = Long.class)
    @Column(name = "FECHA", nullable = false)
    @Convert("longToDate")
    private Date fecha;

    @Basic(optional = false)
    @Column(name = "CANTIDAD", nullable = false)
    private double cantidad;

    @Basic(optional = false)
    @Column(name = "NFACTURA", length = 150)
    private String nFactura;

    @Basic(optional = false)
    @Column(name = "NOTAS", length = 1000)
    private String notas;

    public enum TipoCosteFijoPlant {

        ARRENDAMIENTO("Arrendamiento"), AMORTIZACION("Amortización de Plantaciones"), SEGUROS("Seguros"), MANTENIMIENTO("Conservación y Mantenimiento"), OTROS_COSTES("Otros costes");

        private String nombre;

        TipoCosteFijoPlant(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public CosteFijoPlantacion() {
    }

    public CosteFijoPlantacion(Plantacion plantacion) {
        this.plantacion = plantacion;
    }

    public CosteFijoPlantacion(Plantacion plantacion, TipoCosteFijoPlant tipoCosteFijo, Date fecha) {
        this.plantacion = plantacion;
        this.tipoCosteFijo = tipoCosteFijo;
        this.fecha = fecha;
    }

    public CosteFijoPlantacion(Plantacion instalacion, TipoCosteFijoPlant tipoCosteFijo, String descripcion, Date fecha, String nFactura, String notas) {
        this.plantacion = instalacion;
        this.tipoCosteFijo = tipoCosteFijo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.nFactura = nFactura;
        this.notas = notas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoCosteFijoPlant getTipoCosteFijo() {
        return tipoCosteFijo;
    }

    public void setTipoCosteFijo(TipoCosteFijoPlant tipoCosteFijo) {
        this.tipoCosteFijo = tipoCosteFijo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Plantacion getPlantacion() {
        return plantacion;
    }

    public void setPlantacion(Plantacion plantacion) {
        this.plantacion = plantacion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getNFactura() {
        return nFactura;
    }

    public void setNFactura(String nFactura) {
        this.nFactura = nFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CosteFijoPlantacion)) {
            return false;
        }
        CosteFijoPlantacion other = (CosteFijoPlantacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "openfield.ado.CosteFijoPlantacions[id=" + id + "]";
    }
}
