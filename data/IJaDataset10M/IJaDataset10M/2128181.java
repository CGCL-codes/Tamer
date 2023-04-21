package fondefitco.Modelo;

/**
 * @author Kevin & Nel
 */
public class Detalleventa {

    private int codigo;

    private String valor;

    private String descuento;

    private int cantidad_producto;

    private String subtotal;

    private String iva;

    private String total;

    private String cod_producto;

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**     * @return the descuento
     */
    public String getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    /**
     * @return the cantidad_producto
     */
    public int getCantidad_producto() {
        return cantidad_producto;
    }

    /**
     * @param cantidad_producto the cantidad_producto to set
     */
    public void setCantidad_producto(int cantidad_producto) {
        this.cantidad_producto = cantidad_producto;
    }

    /**
     * @return the subtotal
     */
    public String getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the iva
     */
    public String getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(String iva) {
        this.iva = iva;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the productos_codigo
     */
    public String getCod_producto() {
        return cod_producto;
    }

    /**
     * @param productos_codigo the productos_codigo to set
     */
    public void setCod_producto(String cod_producto) {
        this.cod_producto = cod_producto;
    }
}
