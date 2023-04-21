package net.martinimix.beans.bind.support;

import com.bluemartini.dna.DNABasePair;
import com.bluemartini.dna.DNAInteger;

/**
 * Provides a currency base pair property editor.
 * 
 * @author Scott Rossillo
 *
 */
public class IntegerBasePairPropertyEditor implements DNABasePairPropertyEditor {

    /**
	 * Creates a new currency base pair property editor.
	 */
    public IntegerBasePairPropertyEditor() {
    }

    public DNABasePair getAsBasePair(Object value) {
        return new DNAInteger((Integer) value);
    }

    public Object setAsBasePair(DNABasePair value) {
        if (DNABasePair.TYPE_INTEGER != value.getType()) {
            throw new IllegalArgumentException("Given value must be of type integer [" + value + "]");
        }
        return (Integer) value.getValue();
    }
}
