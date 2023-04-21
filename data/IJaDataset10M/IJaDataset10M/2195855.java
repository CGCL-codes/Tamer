package ar.com.omnipresence.game.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ar.com.omnipresence.common.server.AbstractEntity;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "item_price_items")
public class ItemPriceItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sequences")
    private Integer id;

    @ManyToOne(optional = false)
    private Resource resource;

    private int quantity;

    public ItemPriceItem() {
        super();
    }

    public ItemPriceItem(Resource resource, int quantity) {
        super();
        this.resource = resource;
        this.quantity = quantity;
    }

    @Override
    protected Object getBusinessId() {
        return id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
