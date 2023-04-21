package de.bea.domingo;

import java.io.Serializable;

/**
 * Base interface for all concrete notes interfaces.
 *
 * <p>The Base class defines methods that are common to all the classes.
 * User code should not directly access the Base class.</p>
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface DBase extends Serializable {

    /**
     * Returns a short description of an instance.
     *
     * @return short description of an instance
     */
    String toString();

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param   object the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the object
     *          argument; <code>false</code> otherwise.
     * @see     #hashCode()
     * @see     java.lang.Object#hashCode()
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    boolean equals(Object object);

    /**
     * Returns a hash code value for the object.
     *
     * @return  a hash code value for this object.
     * @see     #equals(java.lang.Object)
     * @see     java.lang.Object#hashCode()
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    int hashCode();
}
