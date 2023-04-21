package pcgen.core;

/**
 * An interface for collections of equipment.
 * @author Thomas G. W. Epperly
 * @version $Revision: 1.15 $*
 */
interface EquipmentCollection {

    /**
	 * Get the number of children.
	 * @return child count
	 */
    int getChildCount();

    /**
	 * Return the name of the equipment collection.
	 *
	 * @return the name of the equipment collection.
	 */
    String getName();

    /**
	 * Set the parent of this collection.
	 * @param parent
	 */
    void setParent(EquipmentCollection parent);

    /**
	 * Get the parent of this collection.
	 * @return parent
	 */
    EquipmentCollection getParent();

    /**
	 * Return <code>true</code> if this collection ever can accept
	 * children.
	 * @return TRUR or FALSE
	 */
    boolean acceptsChildren();

    /**
	 * Return <code>int 1</code> if this collection can take this object., otherwise a response dependant on why it can't
	 * @param aPC
	 * @param obj
	 * @return int
	 */
    int canContain(PlayerCharacter aPC, Object obj);

    /**
	 * Insert a child in the i'th position.
	 * @param aPC
	 * @param child
	 */
    void insertChild(PlayerCharacter aPC, Object child);

    /**
	 * Remove the i'th child.
	 * @param aPC
	 * @param i
	 */
    void removeChild(PlayerCharacter aPC, int i);
}
