package org.scopemvc.util;

/**
 * <P>
 *
 * Holds two Objects: a key and a value. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/25 13:53:07 $
 * @created 05 September 2002
 */
public final class KeyValue {

    private Object key;

    private Object value;

    /**
     * Constructor for the KeyValue object
     *
     * @param inKey The key
     * @param inValue The value
     */
    public KeyValue(Object inKey, Object inValue) {
        key = inKey;
        value = inValue;
    }

    /**
     * Gets the key
     *
     * @return The key value
     */
    public Object getKey() {
        return key;
    }

    /**
     * Gets the value
     *
     * @return The value value
     */
    public Object getValue() {
        return value;
    }
}
