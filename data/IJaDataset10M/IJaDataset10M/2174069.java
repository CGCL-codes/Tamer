package br.edu.uncisal.farmacia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * @author Igor Cavalcante
 */
@Entity(name = "item_saida")
@SequenceGenerator(name = "SEQ_CLOG", sequenceName = "sq_item_saida")
public class ItemSaida extends Domain implements Serializable {

    private static final long serialVersionUID = -7770300923431094786L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CLOG")
    private Long id;

    private BigDecimal quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

    @ManyToOne
    private Item item;

    public ItemSaida() {
    }

    public ItemSaida(Long id, BigDecimal qtd) {
        setId(id);
        setQuantidade(qtd);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    @Override
    public String toString() {
        return "[id: " + id + " quantidade: " + quantidade + " item: " + item + "]";
    }
}
