package org.pdfbox.util;

import org.pdfbox.cos.COSDictionary;

/**
 * This class will be used for bit flag operations.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.4 $
 */
public class BitFlagHelper {

    private BitFlagHelper() {
    }

    /**
     * Sets the given boolean value at bitPos in the flags.
     *
     * @param dic The dictionary to set the value into.
     * @param field The name of the field to set the value into.
     * @param bitFlag the bit position to set the value in.
     * @param value the value the bit position should have.
     */
    public static final void setFlag(COSDictionary dic, String field, int bitFlag, boolean value) {
        int currentFlags = dic.getInt(field, 0);
        if (value) {
            currentFlags = currentFlags | bitFlag;
        } else {
            currentFlags = currentFlags &= ~bitFlag;
        }
        dic.setInt(field, currentFlags);
    }

    /**
     * Gets the boolean value from the flags at the given bit
     * position.
     * 
     * @param dic The dictionary to get the field from.
     * @param field The name of the field to get the flag from.
     * @param bitFlag the bitPosition to get the value from.
     * 
     * @return true if the number at bitPos is '1'
     */
    public static final boolean getFlag(COSDictionary dic, String field, int bitFlag) {
        int ff = dic.getInt(field, 0);
        return (ff & bitFlag) == bitFlag;
    }
}
