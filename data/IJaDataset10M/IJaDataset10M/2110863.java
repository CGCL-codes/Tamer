package br.com.cps.distribuidora.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Rudy
 */
@Entity
@Table(name = "pedido", catalog = "mydb", schema = "")
@NamedQueries({ @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"), @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT p FROM Pedido p WHERE p.idPedido = :idPedido"), @NamedQuery(name = "Pedido.findByDtDesejada", query = "SELECT p FROM Pedido p WHERE p.dtDesejada = :dtDesejada"), @NamedQuery(name = "Pedido.findByDtPedido", query = "SELECT p FROM Pedido p WHERE p.dtPedido = :dtPedido"), @NamedQuery(name = "Pedido.findByVlFrete", query = "SELECT p FROM Pedido p WHERE p.vlFrete = :vlFrete"), @NamedQuery(name = "Pedido.findByVlPedido", query = "SELECT p FROM Pedido p WHERE p.vlPedido = :vlPedido"), @NamedQuery(name = "Pedido.findByVlTotal", query = "SELECT p FROM Pedido p WHERE p.vlTotal = :vlTotal"), @NamedQuery(name = "Pedido.findByDsStatus", query = "SELECT p FROM Pedido p WHERE p.dsStatus = :dsStatus"), @NamedQuery(name = "Pedido.findByFlOriginal", query = "SELECT p FROM Pedido p WHERE p.flOriginal = :flOriginal") })
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Basic(optional = false)
    @Column(name = "dt_desejada")
    @Temporal(TemporalType.DATE)
    private Date dtDesejada;

    @Basic(optional = false)
    @Column(name = "dt_pedido")
    @Temporal(TemporalType.DATE)
    private Date dtPedido;

    @Basic(optional = false)
    @Column(name = "vl_frete")
    private BigDecimal vlFrete;

    @Basic(optional = false)
    @Column(name = "vl_pedido")
    private BigDecimal vlPedido;

    @Basic(optional = false)
    @Column(name = "vl_total")
    private BigDecimal vlTotal;

    @Basic(optional = false)
    @Column(name = "ds_status")
    private String dsStatus;

    @Basic(optional = false)
    @Column(name = "fl_original")
    private boolean flOriginal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<Entrega> entregaCollection;

    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne(optional = false)
    private Cliente cliente;

    @JoinColumn(name = "id_endereco", referencedColumnName = "id_endereco")
    @ManyToOne(optional = false)
    private Endereco endereco;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<PedidoHasProduto> pedidoHasProdutoCollection;

    public Pedido() {
    }

    public Pedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Pedido(Integer idPedido, Date dtDesejada, Date dtPedido, BigDecimal vlFrete, BigDecimal vlPedido, BigDecimal vlTotal, String dsStatus, boolean flOriginal) {
        this.idPedido = idPedido;
        this.dtDesejada = dtDesejada;
        this.dtPedido = dtPedido;
        this.vlFrete = vlFrete;
        this.vlPedido = vlPedido;
        this.vlTotal = vlTotal;
        this.dsStatus = dsStatus;
        this.flOriginal = flOriginal;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getDtDesejada() {
        return dtDesejada;
    }

    public void setDtDesejada(Date dtDesejada) {
        this.dtDesejada = dtDesejada;
    }

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtPedido) {
        this.dtPedido = dtPedido;
    }

    public BigDecimal getVlFrete() {
        return vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
    }

    public BigDecimal getVlPedido() {
        return vlPedido;
    }

    public void setVlPedido(BigDecimal vlPedido) {
        this.vlPedido = vlPedido;
    }

    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }

    public boolean getFlOriginal() {
        return flOriginal;
    }

    public void setFlOriginal(boolean flOriginal) {
        this.flOriginal = flOriginal;
    }

    public Collection<Entrega> getEntregaCollection() {
        return entregaCollection;
    }

    public void setEntregaCollection(Collection<Entrega> entregaCollection) {
        this.entregaCollection = entregaCollection;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Collection<PedidoHasProduto> getPedidoHasProdutoCollection() {
        return pedidoHasProdutoCollection;
    }

    public void setPedidoHasProdutoCollection(Collection<PedidoHasProduto> pedidoHasProdutoCollection) {
        this.pedidoHasProdutoCollection = pedidoHasProdutoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cps.distribuidora.model.Pedido[idPedido=" + idPedido + "]";
    }
}
