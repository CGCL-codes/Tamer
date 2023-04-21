package openrpg2.common.dice;

/**
 * Base interface defining the api for all values used by the dice package
 * @author markt
 */
public interface DiceValue {

    /**
     * Determines whether or not this value is an integer
     * @return true if an integer, false otherwise
     */
    public boolean isInt();

    /**
     * Gets the number of elements in this value. 
     * @return the number of elements in this value ( 0 means that the value is atomic )
     */
    public int getSize();

    /**
     * Gets the base for this value (ie, all groups are evaluated to atomic values).
     * @return a base value that represents this value after it is fully evaluated
     */
    public DiceValue getBaseValue();
}
