package ao.com.bna.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author dfancone
 * 
 */
@Entity
@Table(name = "Produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PRODUTO_ID_GENERATOR", sequenceName = "PRODUTO_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUTO_ID_GENERATOR")
    @Column(nullable = false, precision = 6)
    private Long id;

    @Column(length = 200)
    private String produto;

    @ManyToOne
    @JoinColumn(name = "idTipoProduto", referencedColumnName = "id", nullable = false)
    private TipoProduto tipoProduto = new TipoProduto();

    @ManyToOne
    @JoinColumn(name = "idModelo", referencedColumnName = "id", nullable = false)
    private Modelo modelo = new Modelo();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegisto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produto")
    private List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();

    /**
	 * 
	 */
    public Produto() {
        super();
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the produto
	 */
    public String getProduto() {
        return produto;
    }

    /**
	 * @param produto
	 *            the produto to set
	 */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /**
	 * @return the tipoProduto
	 */
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    /**
	 * @param tipoProduto
	 *            the tipoProduto to set
	 */
    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    /**
	 * @return the modelo
	 */
    public Modelo getModelo() {
        return modelo;
    }

    /**
	 * @param modelo
	 *            the modelo to set
	 */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    /**
	 * @return the itemPedidos
	 */
    public List<ItemPedido> getItemPedidos() {
        return itemPedidos;
    }

    /**
	 * @param itemPedidos
	 *            the itemPedidos to set
	 */
    public void setItemPedidos(List<ItemPedido> itemPedidos) {
        this.itemPedidos = itemPedidos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
	 * @return the dataRegisto
	 */
    public Date getDataRegisto() {
        return dataRegisto;
    }

    /**
	 * @param dataRegisto the dataRegisto to set
	 */
    public void setDataRegisto(Date dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    /**
	 * @return the dataAlteracao
	 */
    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    /**
	 * @param dataAlteracao the dataAlteracao to set
	 */
    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return tipoProduto.getSigla() + ": " + modelo.getMarca().getSigla() + ": " + modelo.getModelo() + ": " + produto;
    }
}
