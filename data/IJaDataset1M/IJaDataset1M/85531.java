package bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import vo.ItemOrdenDeCompraCCVo;

@Entity
@Table(name = "ItemsOrdenesDeCompraCC")
public class ItemOrdenDeCompraCCBean extends AbstractBean<ItemOrdenDeCompraCCVo> {

    private static final long serialVersionUID = 1L;

    private int idItem;

    private RodamientoBean rodamiento;

    private float precioCompra;

    private int cantidadPedida;

    private String nombreLista;

    private CondicionCompraBean condicionCompra;

    private DescuentoCompraBean descuentoCompra;

    public ItemOrdenDeCompraCCBean() {
    }

    public ItemOrdenDeCompraCCBean(int idItem, RodamientoBean rodamiento, float precioCompra, int cantidadPedida, String nombreLista, CondicionCompraBean condicionCompra, DescuentoCompraBean descuentoCompra) {
        super();
        this.idItem = idItem;
        this.rodamiento = rodamiento;
        this.precioCompra = precioCompra;
        this.cantidadPedida = cantidadPedida;
        this.nombreLista = nombreLista;
        this.condicionCompra = condicionCompra;
        this.descuentoCompra = descuentoCompra;
    }

    public ItemOrdenDeCompraCCBean(RodamientoBean rodamiento, float precioCompra, int cantidadPedida, String nombreLista, CondicionCompraBean condicionCompra, DescuentoCompraBean descuentoCompra) {
        super();
        this.idItem = idItem;
        this.rodamiento = rodamiento;
        this.precioCompra = precioCompra;
        this.cantidadPedida = cantidadPedida;
        this.nombreLista = nombreLista;
        this.condicionCompra = condicionCompra;
        this.descuentoCompra = descuentoCompra;
    }

    @Id
    @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "OC_CC", allocationSize = 1, initialValue = 0)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    @ManyToOne
    @JoinColumn(name = "idRodamiento")
    public RodamientoBean getRodamiento() {
        return rodamiento;
    }

    public void setRodamiento(RodamientoBean rodamiento) {
        this.rodamiento = rodamiento;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(int cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    @ManyToOne
    @JoinColumn(name = "idCondicionCompra")
    public CondicionCompraBean getCondicionCompra() {
        return condicionCompra;
    }

    public void setCondicionCompra(CondicionCompraBean condicionCompra) {
        this.condicionCompra = condicionCompra;
    }

    @ManyToOne
    @JoinColumn(name = "idDescuentoCompra")
    public DescuentoCompraBean getDescuentoCompra() {
        return descuentoCompra;
    }

    public void setDescuentoCompra(DescuentoCompraBean descuentoCompra) {
        this.descuentoCompra = descuentoCompra;
    }

    @Transient
    public ItemOrdenDeCompraCCVo getVO() {
        ItemOrdenDeCompraCCVo vo = new ItemOrdenDeCompraCCVo();
        vo.setCantidadPedida(this.cantidadPedida);
        vo.setCondicionCompra(this.condicionCompra.getVO());
        vo.setDescuentoCompra(this.descuentoCompra.getVO());
        vo.setIdItem(this.idItem);
        vo.setNombreLista(this.nombreLista);
        vo.setPrecioCompra(this.precioCompra);
        vo.setRodamiento(this.rodamiento.getVO());
        return vo;
    }

    @Transient
    public void setVO(ItemOrdenDeCompraCCVo vo) {
        this.cantidadPedida = vo.getCantidadPedida();
        CondicionCompraBean condV = new CondicionCompraBean();
        condV.setVO(vo.getCondicionCompra());
        this.condicionCompra = condV;
        DescuentoCompraBean descV = new DescuentoCompraBean();
        descV.setVO(vo.getDescuentoCompra());
        this.descuentoCompra = descV;
        this.idItem = vo.getIdItem();
        this.nombreLista = vo.getNombreLista();
        this.precioCompra = vo.getPrecioCompra();
        RodamientoBean rod = new RodamientoBean();
        rod.setVO(vo.getRodamiento());
        this.rodamiento = rod;
    }
}
