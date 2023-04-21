package org.dmd.dms.generated.types;

import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dms.generated.enums.OperationalContextEnum;

/**
 * The DmcTypeOperationalContextEnumSTATIC provides static access to functions used to manage values of type OperationalContextEnum
 * These methods are used to support ComplexTypeDefinitions.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSTATICType(GenUtility.java:2008)
 *    Called from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1923)
 */
public class DmcTypeOperationalContextEnumSTATIC {

    public static DmcTypeOperationalContextEnumSTATIC instance;

    static DmcTypeOperationalContextEnumSV typeHelper;

    static {
        instance = new DmcTypeOperationalContextEnumSTATIC();
    }

    protected DmcTypeOperationalContextEnumSTATIC() {
        typeHelper = new DmcTypeOperationalContextEnumSV();
    }

    public OperationalContextEnum typeCheck(Object value) throws DmcValueException {
        return (typeHelper.typeCheck(value));
    }

    public OperationalContextEnum cloneValue(OperationalContextEnum value) throws DmcValueException {
        return (typeHelper.cloneValue(value));
    }

    public void serializeValue(DmcOutputStreamIF dos, OperationalContextEnum value) throws Exception {
        typeHelper.serializeValue(dos, value);
    }

    public OperationalContextEnum deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (typeHelper.deserializeValue(dis));
    }
}
