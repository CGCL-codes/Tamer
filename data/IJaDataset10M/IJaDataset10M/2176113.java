package org.dmd.dmc.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcHashedAttribute;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeIntegerToBoolean class provides hashed attribute support for mapping Integers to Booleans.
 */
@SuppressWarnings("serial")
public abstract class DmcTypeIntegerToBoolean extends DmcHashedAttribute<IntegerToBoolean> implements Serializable {

    public DmcTypeIntegerToBoolean() {
    }

    public DmcTypeIntegerToBoolean(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public IntegerToBoolean typeCheck(Object value) throws DmcValueException {
        IntegerToBoolean rc = null;
        if (value instanceof IntegerToBoolean) {
            rc = (IntegerToBoolean) value;
        } else if (value instanceof Integer) {
            rc = new IntegerToBoolean((Integer) value);
        } else if (value instanceof String) {
            String v = (String) value;
            Integer intValue = null;
            int space = v.indexOf(" ");
            if (space == -1) {
                throw (new DmcValueException("Expecting integer followed by a boolean for IntegerToBoolean"));
            }
            String intPart = v.substring(0, space);
            try {
                intValue = Integer.valueOf(intPart);
            } catch (NumberFormatException e) {
                throw (new DmcValueException("Invalid Integer value: " + intPart));
            }
            if ((space + 1) == v.length()) {
                throw (new DmcValueException("Missing boolean value for IntegerToBoolean"));
            }
            boolean b = Boolean.parseBoolean(v.substring(space + 1));
            rc = new IntegerToBoolean(intValue, b);
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with IntegerToBoolean expected."));
        }
        return (rc);
    }

    @Override
    public IntegerToBoolean cloneValue(IntegerToBoolean original) {
        return (new IntegerToBoolean(original.key, original.value));
    }

    /**
     * Write a IntegerToBoolean.
     * @param dos The output stream.
     * @param value The value to be serialized.
     * @throws Exception
     */
    public void serializeValue(DmcOutputStreamIF dos, IntegerToBoolean value) throws Exception {
        value.serializeIt(dos);
    }

    /**
     * Read a IntegerToBoolean.
     * @param dis the input stream.
     * @return A value read from the input stream.
     * @throws Exception
     */
    public IntegerToBoolean deserializeValue(DmcInputStreamIF dis) throws Exception {
        IntegerToBoolean rc = new IntegerToBoolean();
        rc.deserializeIt(dis);
        return (rc);
    }
}
