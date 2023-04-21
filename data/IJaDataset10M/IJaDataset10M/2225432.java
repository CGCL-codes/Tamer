package kadathMud.player;

import java.util.ArrayList;

/**
 * This class represents the inventory of a player.
 *
 * @author Joerg Opolka
 */
public class Inventory {

    /** The total weight of all items in the inventory. */
    private int weight;

    /** The maximal number of slots that can me used for storing items. */
    private int maximalSlots;

    /** The slots and their content. */
    private ArrayList slots;

    public Inventory(int maximalSlots) {
        this.weight = 0;
        this.slots = new ArrayList();
        this.maximalSlots = maximalSlots;
    }

    /**
     * Add 1 item to the inventory;
     *
     * @param id the id of the item to be added
     *
     * @return true if successful, false otherwise
     */
    public boolean add(int id) {
        return add(1, id);
    }

    /**
     * Adds a number of items to the inventory.
     *
     * @param numberOfItems how many items will be added
     * @param id the id of the items to be added
     *
     * @return true if successfull, false otherwise
     */
    public boolean add(int numberOfItems, int id) {
        if (getFreeSlots() > 0) {
            int[] items = new int[2];
            items[0] = id;
            items[1] = numberOfItems;
            slots.add(items);
            return true;
        } else return false;
    }

    public boolean remove() {
        return true;
    }

    public String[] getInventoryDescription() {
        return null;
    }

    public int getItem(String keyword) {
        return -1;
    }

    /**
     * Gets the number of unused slots.
     *
     * @return the number of free slots
     */
    public int getFreeSlots() {
        return (slots.size() - maximalSlots);
    }
}
