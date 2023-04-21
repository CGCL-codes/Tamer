package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.types.UUIDName;
import org.dmd.dmc.types.DmcTypeUUIDName;

/**
 * The DmcTypeUUIDNameSV provides storage for a single-valued UUIDName
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:270)
 */
@SuppressWarnings("serial")
public class DmcTypeUUIDNameSV extends DmcTypeUUIDName implements Serializable {

    protected UUIDName value;

    public DmcTypeUUIDNameSV() {
    }

    public DmcTypeUUIDNameSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeUUIDNameSV getNew() {
        return (new DmcTypeUUIDNameSV(attrInfo));
    }

    public DmcTypeUUIDNameSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeUUIDNameSV(ai));
    }

    @Override
    public DmcAttribute<UUIDName> cloneIt() {
        DmcTypeUUIDNameSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public UUIDName getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public UUIDName set(Object v) throws DmcValueException {
        UUIDName rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public UUIDName getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
