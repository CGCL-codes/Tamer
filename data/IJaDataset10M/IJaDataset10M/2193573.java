package hu.openig.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A fleet's inventory item description.
 * @author akarnokd, 2011.04.05.
 */
public class FleetInventoryItem {

    /** The inventory type. */
    public ResearchType type;

    /** The count. */
    public int count;

    /** The current hitpoints. */
    public int hp;

    /** The current shield points. */
    public int shield;

    /** The fleet's inventory slots. */
    public final List<FleetInventorySlot> slots = new ArrayList<FleetInventorySlot>();
}
