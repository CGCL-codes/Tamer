package bean;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import converter.convVo;
import vo.ClienteVo;
import vo.ProveedorVo;

@Entity
@Table(name = "Clientes")
public class ClienteBean extends AbstractBean<ClienteVo> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3211370706087773073L;

    private Integer idCliente;

    private String razonSocial;

    private List<CondicionVentaBean> condicionesVenta;

    private List<DescuentoVentaBean> descuentosVenta;

    @Id
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente")
    public List<CondicionVentaBean> getCondicionesVenta() {
        return condicionesVenta;
    }

    public void setCondicionesVenta(List<CondicionVentaBean> condicionesVenta) {
        this.condicionesVenta = condicionesVenta;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCliente")
    public List<DescuentoVentaBean> getDescuentosVenta() {
        return descuentosVenta;
    }

    public void setDescuentosVenta(List<DescuentoVentaBean> descuentosVenta) {
        this.descuentosVenta = descuentosVenta;
    }

    @Transient
    public ClienteVo getVO() {
        ClienteVo vo = new ClienteVo();
        vo.setIdCliente(this.idCliente);
        vo.setRazonSocial(this.razonSocial);
        vo.setCondicionesVenta(convVo.getListaVoFromBean(this.getCondicionesVenta()));
        vo.setDescuentosVenta(convVo.getListaVoFromBean(this.getDescuentosVenta()));
        return vo;
    }

    @Transient
    public void setVO(ClienteVo vo) {
        this.idCliente = vo.getIdCliente();
        this.razonSocial = vo.getRazonSocial();
        this.condicionesVenta = convVo.getListaBeanFromVo(vo.getCondicionesVenta());
        this.descuentosVenta = convVo.getListaBeanFromVo(vo.getDescuentosVenta());
    }

    public ClienteVo loadVOForService() {
        ClienteVo vo = new ClienteVo();
        vo.setIdCliente(this.idCliente);
        vo.setRazonSocial(this.razonSocial);
        return vo;
    }
}
