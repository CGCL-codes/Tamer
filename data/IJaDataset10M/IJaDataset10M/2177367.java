package entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;
import abstractEntities.AbstractInventory;

/**
 * Inventory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "inventory", catalog = "skillworld")
public class Inventory extends AbstractInventory implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 334805160937787623L;

    /** default constructor */
    public Inventory() {
    }

    /** minimal constructor */
    public Inventory(Integer actSize, Integer maxSize) {
        super(actSize, maxSize);
    }

    /** full constructor */
    public Inventory(Integer actSize, Integer maxSize, Set<Itemrecord> itemrecords, Set<User> users) {
        super(actSize, maxSize, itemrecords, users);
    }
}
