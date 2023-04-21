package org.berlin.chem.gae;

/**
 * Cell Slot.
 */
public class DBModelSquirmCellSlot {

    private boolean has_occupant;

    private DBSquirmCell occupant;

    public DBModelSquirmCellSlot() {
        has_occupant = false;
    }

    public void makeOccupied(final DBSquirmCell occ) {
        if (has_occupant) {
        }
        has_occupant = true;
        occupant = occ;
    }

    public void makeEmpty() {
        has_occupant = false;
    }

    public boolean queryEmpty() {
        return !has_occupant;
    }

    public DBSquirmCell getOccupant() {
        if (!has_occupant) {
            throw new Error("SquirmCellSlot::getOccupant : no occupant!");
        }
        return occupant;
    }

    public String toString() {
        return has_occupant + "#" + occupant;
    }
}
