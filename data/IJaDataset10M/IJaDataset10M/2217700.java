package net.sourceforge.jpalm.mobiledb.field.type;

/**
 * A field type in a MobileDB record.
 * 
 * @param <T>
 *            the native type for this MobileDB type
 */
public interface Type<T> {

    /**
     * Gets the native object for this type.
     * 
     * @return the native object
     */
    T getValue();

    /**
     * Gets the MobileDB value for this type.
     * 
     * @return the MobileDB value
     */
    String toMobileDB();
}
