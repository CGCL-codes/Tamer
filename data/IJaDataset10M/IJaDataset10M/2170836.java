package mensaje_objetos.facturacion;

import java.math.BigDecimal;
import javax.persistence.*;
import mensaje_objetos.almacen.Producto;

/**
 *
 * @author carlos
 */
@Entity()
@Table(name = "productofactura")
@javax.persistence.SequenceGenerator(name = "SEQ", sequenceName = "productofactura_seq")
public class ProductoFactura implements java.io.Serializable {

    private Producto producto;

    private int cantidad;

    private BigDecimal precio;

    private BigDecimal subtotal;

    private int secuencia;

    public void setSecuencia(int sec) {
        secuencia = sec;
    }

    @Id
    @GeneratedValue(generator = "SEQ", strategy = GenerationType.AUTO)
    public int getSecuencia() {
        return secuencia;
    }

    @OneToOne()
    @JoinColumn(name = "codigo")
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
