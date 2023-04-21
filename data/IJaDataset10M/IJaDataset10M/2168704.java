package csel.model;

import java.util.EnumMap;
import csel.model.items.EquippableItem;
import csel.model.items.Weapon;

public class EquippedItems implements java.io.Serializable {

    /**
     * Change this if we change its structure.
     */
    private static final long serialVersionUID = 5L;

    private final EnumMap<BodyParts, EquippableItem> myEquippedItems;

    public EquippedItems() {
        myEquippedItems = new EnumMap<BodyParts, EquippableItem>(BodyParts.class);
    }

    public void add(EquippableItem ei) {
        myEquippedItems.put(ei.getGoesOn(), ei);
    }

    public EquippableItem remove(EquippableItem ei) {
        return myEquippedItems.remove(ei.getGoesOn());
    }

    public EquippableItem remove(BodyParts bp) {
        return myEquippedItems.remove(bp);
    }

    public EquippableItem getEquippedItem(BodyParts bp) {
        return (myEquippedItems.get(bp));
    }

    public EnumMap<BodyParts, EquippableItem> getAllEquippedItems() {
        return myEquippedItems.clone();
    }

    public Weapon getWeapon() {
        return ((Weapon) (myEquippedItems.get(BodyParts.Righthand)));
    }
}
