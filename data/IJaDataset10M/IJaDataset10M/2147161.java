package ar.com.omnipresence.game.server;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ar.com.omnipresence.common.server.AbstractEntity;

/**
 * @author Martin Straus
 * 
 */
@Entity
@Table(name = "items")
public abstract class Item extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sequences")
    protected Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blueprint_id")
    private ItemBlueprint blueprint;

    @ManyToOne
    private Republic republic;

    private boolean hidden = false;

    protected Item() {
    }

    public ItemBlueprint getBlueprint() {
        return blueprint;
    }

    @Override
    protected Object getBusinessId() {
        return id;
    }

    void setBlueprint(ItemBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public abstract Item cloneItem();
}
