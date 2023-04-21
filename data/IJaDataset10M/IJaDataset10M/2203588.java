package vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenDeCompraOFVo implements AbstractVo {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int idOrdenCompra;

    private String nombreArchivo;

    private Date fecha;

    private String estado;

    private CotizacionVo cotizacion;

    private int nroSucursal;

    List<ItemOrdenDeCompraOFVo> items = new ArrayList<ItemOrdenDeCompraOFVo>();

    public OrdenDeCompraOFVo() {
    }

    public OrdenDeCompraOFVo(String estado, String nombreArchivo, CotizacionVo cotizacion) {
        super();
        this.estado = estado;
        this.fecha = new Date();
        this.nombreArchivo = nombreArchivo;
        this.cotizacion = cotizacion;
    }

    public OrdenDeCompraOFVo(int idOrdenCompra, Date fecha, String estado, CotizacionVo cotizacion) {
        super();
        this.idOrdenCompra = idOrdenCompra;
        this.fecha = fecha;
        this.estado = estado;
        this.cotizacion = cotizacion;
    }

    public int getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(int idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public CotizacionVo getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(CotizacionVo cotizacion) {
        this.cotizacion = cotizacion;
    }

    public List<ItemOrdenDeCompraOFVo> getItems() {
        return items;
    }

    public void setItems(List<ItemOrdenDeCompraOFVo> items) {
        this.items = items;
    }

    public void agregarItem(ItemOrdenDeCompraOFVo item) {
        items.add(item);
    }

    public int getNroSucursal() {
        return nroSucursal;
    }

    public void setNroSucursal(int nroSucursal) {
        this.nroSucursal = nroSucursal;
    }
}
