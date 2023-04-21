package org.dmd.dms.generated.types;

import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.types.Modifier;

/**
 * The DmcTypeModifierSTATIC provides static access to functions used to manage values of type Modifier
 * These methods are used to support ComplexTypeDefinitions.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSTATICType(GenUtility.java:2008)
 *    Called from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1923)
 */
public class DmcTypeModifierSTATIC {

    public static DmcTypeModifierSTATIC instance;

    static DmcTypeModifierSV typeHelper;

    static {
        instance = new DmcTypeModifierSTATIC();
    }

    protected DmcTypeModifierSTATIC() {
        typeHelper = new DmcTypeModifierSV();
    }

    public Modifier typeCheck(Object value) throws DmcValueException {
        return (typeHelper.typeCheck(value));
    }

    public Modifier cloneValue(Modifier value) throws DmcValueException {
        return (typeHelper.cloneValue(value));
    }

    public void serializeValue(DmcOutputStreamIF dos, Modifier value) throws Exception {
        typeHelper.serializeValue(dos, value);
    }

    public Modifier deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (typeHelper.deserializeValue(dis));
    }
}
