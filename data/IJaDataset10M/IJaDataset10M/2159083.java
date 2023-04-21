package org.dmd.dms.generated.types;

import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcObjectName;
import org.dmd.dmc.DmcNameBuilderIF;
import org.dmd.dmc.types.DmcTypeDmcObjectName;
import org.dmd.dmc.types.DotName;

/**
 * The DmcTypeDotNameSTATIC provides static access to functions used to manage values of type DotName
 * These methods are used to support ComplexTypeDefinitions.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSTATICType(GenUtility.java:2008)
 *    Called from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1923)
 */
public class DmcTypeDotNameSTATIC implements DmcNameBuilderIF {

    public static DmcTypeDotNameSTATIC instance;

    static DmcTypeDotNameSV typeHelper;

    static String nameClass = "DotName";

    static final int attrID = 107;

    static {
        instance = new DmcTypeDotNameSTATIC();
    }

    protected DmcTypeDotNameSTATIC() {
        typeHelper = new DmcTypeDotNameSV();
    }

    public DotName typeCheck(Object value) throws DmcValueException {
        return (typeHelper.typeCheck(value));
    }

    public DotName cloneValue(DotName value) throws DmcValueException {
        return (typeHelper.cloneValue(value));
    }

    public void serializeValue(DmcOutputStreamIF dos, DotName value) throws Exception {
        typeHelper.serializeValue(dos, value);
    }

    public DotName deserializeValue(DmcInputStreamIF dis) throws Exception {
        return (typeHelper.deserializeValue(dis));
    }

    @Override
    public DmcTypeDmcObjectName<?> getNewNameHolder(DmcObjectName name, DmcAttributeInfo ai) {
        DmcTypeDmcObjectName<?> rc = typeHelper.getNew(ai);
        try {
            rc.set(name);
        } catch (DmcValueException e) {
            throw (new IllegalStateException("Shouldn't throw exception when setting a name attribute value in a DmcNameBuilderIF - occurred for type: " + name.getNameClass(), e));
        }
        return (rc);
    }

    @Override
    public String getNameClass() {
        return (nameClass);
    }

    @Override
    public int getNameAttributeID() {
        return (attrID);
    }
}
