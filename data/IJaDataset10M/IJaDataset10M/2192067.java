package oracle.toplink.essentials.indirection;

/**
 * <b>Purpose</b>: Interface to allow lazy loading of an object's relationships from the database.
 *
 * @see ValueHolder
 * @see oracle.toplink.essentials.internal.indirection.DatabaseValueHolder
 */
public interface ValueHolderInterface {

    /** Can be used to have transparent indirection toString instantiate the objects. */
    public static boolean shouldToStringInstantiate = false;

    /**
     * PUBLIC:
     * Return the value.
     */
    public Object getValue();

    /**
     * PUBLIC:
     * Return whether the contents have been read from the database.
     * This is used periodically by the indirection policy to determine whether
     * to trigger the database read.
     */
    public boolean isInstantiated();

    /**
     * PUBLIC:
     * Set the value.
     */
    public void setValue(Object value);
}
