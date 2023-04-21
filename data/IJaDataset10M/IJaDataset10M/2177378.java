package org.quantumleaphealth.action;

import java.io.Serializable;
import org.quantumleaphealth.ontology.CharacteristicCode;

/**
 * Represents a facade into a map of characteristics.
 * This serializable JavaBean exposes the <code>value</code> 
 * field to the GUI.
 * @author Tom Bechtold
 * @version 2008-06-30
 */
public abstract class FacadeAbstractCharacteristic implements Serializable {

    /**
     * The code of the characteristic to represent, guaranteed to be non-<code>null</code>
     */
    protected final CharacteristicCode characteristicCode;

    /**
     * The codes of the characteristics that this characteristic may be set to, 
     * guaranteed to be non-<code>null</code>
     */
    protected final CharacteristicCode[] valueSet;

    /**
     * Validates and stores the parameters
     * @param characteristicCode the code of the characteristic to represent
     * @param valueSet the codes of the characteristics that this characteristic may be set to
     * @throws IllegalArgumentException if any parameter is <code>null</code> or <code>valueSet</code> is larger than <code>Long.SIZE</code>
     * @see Long#SIZE
     */
    protected FacadeAbstractCharacteristic(CharacteristicCode characteristicCode, CharacteristicCode[] valueSet) throws IllegalArgumentException {
        if ((characteristicCode == null) || (valueSet == null)) throw new IllegalArgumentException("characteristic or value set not specified");
        if (valueSet.length >= Long.SIZE - 1) throw new IllegalArgumentException("value set is too large: " + valueSet.length);
        this.characteristicCode = characteristicCode;
        this.valueSet = valueSet;
    }

    /**
     * Returns the number of elements in the value set
     * @return the number of elements in the value set
     * @see #valueSet
     */
    public int getCount() {
        return valueSet.length;
    }

    /**
     * @return the bit-wise representation of the one-based position within <code>valueSet</code>
     *         or <code>0l</code> if nothing is set
     */
    public abstract long getValue();

    /**
     * @param value the bit-wise representation of the one-based position within <code>valueSet</code>
     *        or <code>0l</code> to remove all values
     * @throws IllegalArgumentException if the one-based position is higher than the size of <code>valueSet</code>
     */
    public abstract void setValue(long value) throws IllegalArgumentException;

    /**
     * Version UID for serializable class
     */
    private static final long serialVersionUID = -7782529285862423669L;
}
