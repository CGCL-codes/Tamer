package org.dmd.dmc.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeDotName class provides support for simple, DotName names for objects.
 */
@SuppressWarnings("serial")
public abstract class DmcTypeDotName extends DmcTypeDmcHierarchicObjectName<DotName> implements Serializable {

    public DmcTypeDotName() {
    }

    public DmcTypeDotName(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public DotName typeCheck(Object value) throws DmcValueException {
        DotName rc = null;
        if (value instanceof DotName) {
            rc = (DotName) value;
        } else if (value instanceof String) {
            rc = new DotName((String) value);
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with DotName or String expected."));
        }
        return rc;
    }

    @Override
    public DotName cloneValue(DotName original) {
        return (new DotName(original.name));
    }

    /**
	 * Write a DotName.
     * @param dos The output stream.
     * @param value The value to be serialized.
     * @throws Exception
	 */
    public void serializeValue(DmcOutputStreamIF dos, DotName value) throws Exception {
        dos.writeUTF(value.getNameString());
    }

    /**
     * Read a DotName.
     * @param dis the input stream.
     * @return A value read from the input stream.
     * @throws Exception
     */
    public DotName deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (new DotName(dis.readUTF()));
    }
}
