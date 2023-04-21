package org.quantumleaphealth.action;

import java.io.Serializable;
import org.quantumleaphealth.ontology.ShortCharacteristicHolder;
import org.quantumleaphealth.ontology.CharacteristicCode;

/**
 * Represents a facade into a map of non-negative short characteristics.
 * This serializable JavaBean exposes the <code>value</code> 
 * string field to the GUI and keeps track whether it 
 * is a valid number.
 * @author Tom Bechtold
 * @version 2008-05-27
 */
public class FacadeShortCharacteristic extends FacadeShort implements Serializable {

    /**
     * The code of the characteristic to represent, guaranteed to be non-<code>null</code>
     */
    protected final CharacteristicCode characteristicCode;

    /**
     * Holds the short characteristics, guaranteed to be non-<code>null</code>
     */
    private ShortCharacteristicHolder shortCharacteristicHolder;

    /**
     * Validates and stores the parameter and sets the cached value
     * @param characteristicCode the code of the characteristic to represent
     * @param shortCharacteristicHolder holds the short characteristics
     * @throws IllegalArgumentException if any parameter is <code>null</code>
     */
    FacadeShortCharacteristic(CharacteristicCode characteristicCode, ShortCharacteristicHolder shortCharacteristicHolder) throws IllegalArgumentException {
        if (characteristicCode == null) throw new IllegalArgumentException("characteristic not specified");
        this.characteristicCode = characteristicCode;
        setShortCharacteristicHolder(shortCharacteristicHolder);
    }

    /**
     * Validates and stores the parameter and updates the cached value
     * @param shortCharacteristicHolder holds the short characteristics
     * @throws IllegalArgumentException if the parameter is <code>null</code>
     */
    void setShortCharacteristicHolder(ShortCharacteristicHolder shortCharacteristicHolder) throws IllegalArgumentException {
        if (shortCharacteristicHolder == null) throw new IllegalArgumentException("holder not specified");
        this.shortCharacteristicHolder = shortCharacteristicHolder;
        update();
    }

    /**
     * @return the underlying value
     * @see org.quantumleaphealth.action.FacadeShort#getValue()
     */
    @Override
    protected Short getValue() {
        return shortCharacteristicHolder.getShort(characteristicCode);
    }

    /**
     * @param value the underlying value
     * @see org.quantumleaphealth.action.FacadeShort#setValue(java.lang.Short)
     */
    @Override
    protected void setValue(Short value) {
        shortCharacteristicHolder.setShort(characteristicCode, value);
    }

    /**
     * Version UID for serialization
     */
    private static final long serialVersionUID = 4573207327106825337L;
}
