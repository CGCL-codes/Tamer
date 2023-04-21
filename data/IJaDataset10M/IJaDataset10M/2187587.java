package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.enums.*;

@SuppressWarnings("serial")
public abstract class DmcTypeDebugEnum extends DmcAttribute<DebugEnum> implements Serializable {

    /**
     * Default constructor.
     */
    public DmcTypeDebugEnum() {
    }

    /**
     * Default constructor.
     */
    public DmcTypeDebugEnum(DmcAttributeInfo ai) {
        super(ai);
    }

    protected DebugEnum typeCheck(Object value) throws DmcValueException {
        DebugEnum rc = null;
        if (value instanceof DebugEnum) {
            rc = (DebugEnum) value;
        } else if (value instanceof String) {
            rc = DebugEnum.get((String) value);
            if (rc == null) {
                throw (new DmcValueException("Value: " + value.toString() + " is not a valid DebugEnum value."));
            }
        } else if (value instanceof Integer) {
            rc = DebugEnum.get((Integer) value);
            if (rc == null) {
                throw (new DmcValueException("Value: " + value.toString() + " is not a valid DebugEnum value."));
            }
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with DebugEnum expected."));
        }
        return (rc);
    }

    /**
     * Returns a clone of a value associated with this type.
     */
    public DebugEnum cloneValue(DebugEnum val) {
        DebugEnum rc = val;
        return (rc);
    }

    /**
     * Writes a DebugEnum.
     */
    @Override
    public void serializeValue(DmcOutputStreamIF dos, DebugEnum value) throws Exception {
        dos.writeShort(value.intValue());
    }

    /**
     * Reads a DebugEnum.
     */
    @Override
    public DebugEnum deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (DebugEnum.get(dis.readShort()));
    }
}
