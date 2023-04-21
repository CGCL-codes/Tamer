package org.dmd.dms.generated.types;

import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.types.StringToString;

/**
 * The DmcTypeStringToStringSTATIC provides static access to functions used to manage values of type StringToString
 * These methods are used to support ComplexTypeDefinitions.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSTATICType(GenUtility.java:2008)
 *    Called from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1923)
 */
public class DmcTypeStringToStringSTATIC {

    public static DmcTypeStringToStringSTATIC instance;

    static DmcTypeStringToStringSV typeHelper;

    static {
        instance = new DmcTypeStringToStringSTATIC();
    }

    protected DmcTypeStringToStringSTATIC() {
        typeHelper = new DmcTypeStringToStringSV();
    }

    public StringToString typeCheck(Object value) throws DmcValueException {
        return (typeHelper.typeCheck(value));
    }

    public StringToString cloneValue(StringToString value) throws DmcValueException {
        return (typeHelper.cloneValue(value));
    }

    public void serializeValue(DmcOutputStreamIF dos, StringToString value) throws Exception {
        typeHelper.serializeValue(dos, value);
    }

    public StringToString deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (typeHelper.deserializeValue(dis));
    }
}
