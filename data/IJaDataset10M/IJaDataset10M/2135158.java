package hu.openig.model;

/**
 * A concrete slot settings for an inventory item, e.g., cruisers and battleships.
 * @author akarnokd, 2011.04.05.
 */
public class InventorySlot {

    /**  The slots definition. */
    public EquipmentSlot slot;

    /** The current entity in the slot, may be null! */
    public ResearchType type;

    /** The item count. */
    public int count;

    /** The item's hitpoints. */
    public int hp;

    /** @return is the slot filled to max? */
    public boolean isFilled() {
        return count >= slot.max;
    }
}
