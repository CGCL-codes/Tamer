package org.helianto.inventory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Represents inventory movement.
 * 
 * <p>
 * Collaborate in use cases like:</p>
 * <ol>
 * <li>receipt, parts move from supplier to warehouse,</li>
 * <li>inspection, parts move from order (wip) to finished or rework,</li>
 * <li>invoicing, from finished to customer.
 * </ol>
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name = "inv_transaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("T")
public class InventoryTransaction implements Serializable {

    public static <T extends InventoryTransaction> T inventoryTransactionFactory(Class<T> clazz, Inventory moveFrom, Inventory moveTo, BigDecimal quantity) {
        T inventoryTransaction = null;
        try {
            inventoryTransaction = clazz.newInstance();
            inventoryTransaction.move(moveFrom, moveTo, quantity);
            return inventoryTransaction;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to intantiate class" + clazz, e);
        }
    }

    private static final long serialVersionUID = 1L;

    private int id;

    private Integer version;

    private Set<Movement> movements = new HashSet<Movement>();

    /**
	 * Default constructor.
	 */
    public InventoryTransaction() {
        super();
    }

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Version.
     */
    @Version
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
	 * Set of movements.
	 */
    @OneToMany(mappedBy = "inventoryTransaction", cascade = { CascadeType.ALL })
    public Set<Movement> getMovements() {
        return movements;
    }

    public void setMovements(Set<Movement> movements) {
        this.movements = movements;
    }

    /**
	 * Move a quantity into an inventory.
	 * 
	 * @param inventory
	 * @param quantity
	 */
    public InventoryTransaction move(Inventory inventory, BigDecimal quantity) {
        Movement movement = new Movement();
        movement.setInventory(inventory);
        movement.setInventoryTransaction(this);
        if (getMovements().add(movement)) {
            movement.setQuantity(quantity);
            return this;
        }
        throw new IllegalArgumentException("Duplicate movement in transaction " + this + " affecting " + inventory);
    }

    /**
	 * Move a quantity from one inventory to other.
	 * 
	 * @param from
	 * @param to
	 * @param quantity
	 */
    public InventoryTransaction move(Inventory from, Inventory to, BigDecimal quantity) {
        return move(from, quantity).move(to, quantity.negate());
    }
}
