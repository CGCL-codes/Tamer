package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.enums.*;

@SuppressWarnings("serial")
public abstract class DmcTypeOperationalContextEnum extends DmcAttribute<OperationalContextEnum> implements Serializable {

    /**
     * Default constructor.
     */
    public DmcTypeOperationalContextEnum() {
    }

    /**
     * Default constructor.
     */
    public DmcTypeOperationalContextEnum(DmcAttributeInfo ai) {
        super(ai);
    }

    protected OperationalContextEnum typeCheck(Object value) throws DmcValueException {
        OperationalContextEnum rc = null;
        if (value instanceof OperationalContextEnum) {
            rc = (OperationalContextEnum) value;
        } else if (value instanceof String) {
            rc = OperationalContextEnum.get((String) value);
            if (rc == null) {
                throw (new DmcValueException("Value: " + value.toString() + " is not a valid OperationalContextEnum value."));
            }
        } else if (value instanceof Integer) {
            rc = OperationalContextEnum.get((Integer) value);
            if (rc == null) {
                throw (new DmcValueException("Value: " + value.toString() + " is not a valid OperationalContextEnum value."));
            }
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with OperationalContextEnum expected."));
        }
        return (rc);
    }

    /**
     * Returns a clone of a value associated with this type.
     */
    public OperationalContextEnum cloneValue(OperationalContextEnum val) {
        OperationalContextEnum rc = val;
        return (rc);
    }

    /**
     * Writes a OperationalContextEnum.
     */
    @Override
    public void serializeValue(DmcOutputStreamIF dos, OperationalContextEnum value) throws Exception {
        dos.writeShort(value.intValue());
    }

    /**
     * Reads a OperationalContextEnum.
     */
    @Override
    public OperationalContextEnum deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (OperationalContextEnum.get(dis.readShort()));
    }
}
