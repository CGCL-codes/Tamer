package net.sf.javaage.character.inventory;

import java.util.List;
import java.util.Vector;

/**
 * Save all items in the inventar.
 * The class is internally synchronized.
 * @author Sebastian(SebiB90)
 * @author Gerrit Hohl (gerrit.hohl@users.sourceforge.net)
 * @version <b>1.0</b>
 */
public class Inventory {

    /** If no maximum weight is set. */
    public static final double NO_MAX_WEIGHT = -1;

    /** A list of all items, which are in the inventory. */
    private Vector items = new Vector();

    /** The weight of all items. */
    private double weightOfItems = 0;

    /**
	 * The items are not allowed to be heavier than the maxWeight.
	 * If maxWeight is a negativ integer, it does not give a weight limit.
	 */
    private double maxWeight = NO_MAX_WEIGHT;

    /**
	 * Creates an inventory with no maximum weight.
	 */
    public Inventory() {
        super();
    }

    /**
	 * Creates an inventory with a maximum weight.
	 * @param maxWeight The maximum weight.
	 */
    public Inventory(final double maxWeight) {
        super();
        this.maxWeight = maxWeight;
    }

    /**
	 * Sets the maximum weight of all items in the inventory.
	 * This method can be only called, if the inventory does not contain
	 * any items.
	 * @param maxWeight The maximum weight or <code>-1</code>, if no maximum weight is given.
	 * @throws IllegalStateException If the inventory containts items.
	 */
    public void setMaxWeight(final double maxWeight) {
        synchronized (this) {
            if (this.items.size() > 0) {
                throw new IllegalStateException("Inventory containts items.");
            }
            this.maxWeight = maxWeight;
        }
    }

    /**
	 * Returns the maximum weight of all items in the inventory.
	 * @return The maximum weight or <code>-1</code>, if no maximum weight is given.
	 */
    public double getMaxWeight() {
        return this.maxWeight;
    }

    /**
	 * Adds a new item to the inventory.
	 * If the weight of the item exceed the maximum weight of the inventory or
	 * the inventory contains already the item, the item won't be added to the
	 * inventory.
	 * @param item The item.
	 * @return <code>true</code>, if the item was sucessfully added to the inventory, <code>false</code> otherwise.
	 * @throws IllegalArgumentException If the item is <code>null</code>.
	 */
    public boolean addItem(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument newItem is null.");
        }
        synchronized (this) {
            if (this.items.contains(item)) {
                return false;
            }
            if (this.maxWeight >= 0) {
                if (this.weightOfItems + item.getWeight() <= this.maxWeight) {
                    this.items.add(item);
                    this.weightOfItems += item.getWeight();
                    return true;
                }
            } else {
                this.items.add(item);
                this.weightOfItems += item.getWeight();
                return true;
            }
        }
        return false;
    }

    /**
	 * Tests if the specified item is in this inventory.
	 * @param item The item.
	 * @return <code>true</code>, if the same item is in the inventory, <code>false</code> otherwise.
	 * @throws IllegalArgumentException If the item is <code>null</code>.
	 */
    public boolean containtsItem(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument newItem is null.");
        }
        synchronized (this) {
            return this.items.contains(item);
        }
    }

    /**
	 * Removes an item from the inventory.
	 * @param item The item.
	 * @return <code>true</code>, if the item was succesfully removed from the inventory, <code>false</code> otherwise.
	 */
    public boolean removeItem(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument newItem is null.");
        }
        synchronized (this) {
            if (this.items.removeElement(item)) {
                this.weightOfItems -= item.getWeight();
                return true;
            }
        }
        return false;
    }

    /**
	 * Removes all items from the inventory.
	 */
    public void removeAllItems() {
        synchronized (this) {
            this.items.clear();
            this.weightOfItems = 0;
        }
    }

    /**
	 * Returns a list of all items in the inventory.
	 * @return The list.
	 */
    public List getItems() {
        synchronized (this) {
            return (List) this.items.clone();
        }
    }
}
