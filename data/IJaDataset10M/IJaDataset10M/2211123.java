package org.pdfbox.pdfviewer;

import org.pdfbox.cos.COSName;

/**
 * This is a simple class that will contain a key and a value.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.3 $
 */
public class MapEntry {

    private Object key;

    private Object value;

    /**
     * Get the key for this entry.
     *
     * @return The entry's key.
     */
    public Object getKey() {
        return key;
    }

    /**
     * This will set the key for this entry.
     *
     * @param k the new key for this entry.
     */
    public void setKey(Object k) {
        key = k;
    }

    /**
     * This will get the value for this entry.
     *
     * @return The value for this entry.
     */
    public Object getValue() {
        return value;
    }

    /**
     * This will set the value for this entry.
     *
     * @param val the new value for this entry.
     */
    public void setValue(Object val) {
        this.value = val;
    }

    /**
     * This will output a string representation of this class.
     *
     * @return A string representation of this class.
     */
    public String toString() {
        String retval = null;
        if (key instanceof COSName) {
            retval = ((COSName) key).getName();
        } else {
            retval = "" + key;
        }
        return retval;
    }
}
