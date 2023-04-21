package vo;

import java.util.Date;
import vo.AbstractVo;

public class ItemListaComparativaVo implements AbstractVo {

    private RodamientoVo rodamientoVo;

    private float precio;

    private String nombreListaPrecio;

    private int cantidadDisponible;

    private Date vigenciaDesde;

    private Date vigenciaHasta;

    private ProveedorVo proveedor;

    public String getNombreListaPrecio() {
        return nombreListaPrecio;
    }

    public void setNombreListaPrecio(String nombreListaPrecio) {
        this.nombreListaPrecio = nombreListaPrecio;
    }

    public ProveedorVo getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorVo proveedor) {
        this.proveedor = proveedor;
    }

    public DescuentoCompraVo getDescuentoCompra() {
        return descuentoCompra;
    }

    public void setDescuentoCompra(DescuentoCompraVo descuentoCompra) {
        this.descuentoCompra = descuentoCompra;
    }

    public CondicionCompraVo getCondicionCompra() {
        return condicionCompra;
    }

    public void setCondicionCompra(CondicionCompraVo condicionCompra) {
        this.condicionCompra = condicionCompra;
    }

    private DescuentoCompraVo descuentoCompra;

    private CondicionCompraVo condicionCompra;

    public ItemListaComparativaVo(float precio, String nombreLista, int cantidadDisponible, Date vigenciaDesde, Date vigenciaHasta) {
        super();
        this.precio = precio;
        this.nombreListaPrecio = nombreLista;
        this.cantidadDisponible = cantidadDisponible;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
    }

    public ItemListaComparativaVo(RodamientoVo rodamientoVo, float precio, String nombreLista, int cantidadDisponible, Date vigenciaDesde, Date vigenciaHasta) {
        super();
        this.rodamientoVo = rodamientoVo;
        this.precio = precio;
        this.nombreListaPrecio = nombreLista;
        this.cantidadDisponible = cantidadDisponible;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
    }

    public ItemListaComparativaVo() {
        super();
    }

    public RodamientoVo getRodamientoVo() {
        return rodamientoVo;
    }

    public void setRodamientoVo(RodamientoVo rodamientoVo) {
        this.rodamientoVo = rodamientoVo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }
}
