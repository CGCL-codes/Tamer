package org.gjt.universe.database;

/** This class is used to index Ships in the database. */
public final class ShipID extends Index {

    /** Constructor - makes a ShipID with index. */
    ShipID(int in) {
        super(in);
    }

    /** Returns string representation of this ID class. */
    public String toString() {
        StringBuffer str = new StringBuffer("HID: ");
        str.append(get());
        return str.toString();
    }
}
