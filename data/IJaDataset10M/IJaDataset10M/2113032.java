package mensaje_objetos.almacen;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class ProductosDelProveedor
 * 
 * @author gonzalez
 */
@Entity
@Table(name = "productos_del_proveedor")
@NamedQueries({ @NamedQuery(name = "ProductosDelProveedor.findByCodigo", query = "SELECT p FROM ProductosDelProveedor p WHERE p.codigo = :codigo"), @NamedQuery(name = "ProductosDelProveedor.findByDescripcion", query = "SELECT p FROM ProductosDelProveedor p WHERE p.descripcion = :descripcion"), @NamedQuery(name = "ProductosDelProveedor.findByFecha", query = "SELECT p FROM ProductosDelProveedor p WHERE p.fecha = :fecha"), @NamedQuery(name = "ProductosDelProveedor.findByPrecio", query = "SELECT p FROM ProductosDelProveedor p WHERE p.precio = :precio"), @NamedQuery(name = "ProductosDelProveedor.findByNombre", query = "SELECT p FROM ProductosDelProveedor p WHERE p.nombre = :nombre"), @NamedQuery(name = "ProductosDelProveedor.findByBorrado", query = "SELECT p FROM ProductosDelProveedor p WHERE p.borrado = :borrado") })
public class ProductosDelProveedor implements Serializable {

    @Id
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "borrado")
    private Boolean borrado;

    /** Creates a new instance of ProductosDelProveedor */
    public ProductosDelProveedor() {
    }

    /**
     * Creates a new instance of ProductosDelProveedor with the specified values.
     * @param codigo the codigo of the ProductosDelProveedor
     */
    public ProductosDelProveedor(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Gets the codigo of this ProductosDelProveedor.
     * @return the codigo
     */
    public String getCodigo() {
        return this.codigo;
    }

    /**
     * Sets the codigo of this ProductosDelProveedor to the specified value.
     * @param codigo the new codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Gets the descripcion of this ProductosDelProveedor.
     * @return the descripcion
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Sets the descripcion of this ProductosDelProveedor to the specified value.
     * @param descripcion the new descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Gets the fecha of this ProductosDelProveedor.
     * @return the fecha
     */
    public Date getFecha() {
        return this.fecha;
    }

    /**
     * Sets the fecha of this ProductosDelProveedor to the specified value.
     * @param fecha the new fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Gets the precio of this ProductosDelProveedor.
     * @return the precio
     */
    public Double getPrecio() {
        return this.precio;
    }

    /**
     * Sets the precio of this ProductosDelProveedor to the specified value.
     * @param precio the new precio
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * Gets the nombre of this ProductosDelProveedor.
     * @return the nombre
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Sets the nombre of this ProductosDelProveedor to the specified value.
     * @param nombre the new nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the borrado of this ProductosDelProveedor.
     * @return the borrado
     */
    public Boolean getBorrado() {
        return this.borrado;
    }

    /**
     * Sets the borrado of this ProductosDelProveedor to the specified value.
     * @param borrado the new borrado
     */
    public void setBorrado(Boolean borrado) {
        this.borrado = borrado;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ProductosDelProveedor.  The result is 
     * <code>true</code> if and only if the argument is not null and is a ProductosDelProveedor object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductosDelProveedor)) {
            return false;
        }
        ProductosDelProveedor other = (ProductosDelProveedor) object;
        if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "mensaje_objetos.almacen.ProductosDelProveedor[codigo=" + codigo + "]";
    }
}
