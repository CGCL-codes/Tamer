package hu.openig.xold.model;

/**
 * Slot type where the user cannot edit the content equipment.
 * Used, for example, by fighters and space stations which have fixed
 * weapons.
 * @author karnokd
 *
 */
public class FixedSlot {

    /** The type of the slot. */
    public String type;

    /** The name of the technology in this slot. */
    String eqId;

    /** The actual technology in this slot. */
    public ResearchTech id;

    /** The number for this slot. */
    public int value;
}
