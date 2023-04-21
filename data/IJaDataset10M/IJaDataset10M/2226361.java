package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.enums.FilterTypeEnum;

/**
 * The DmcTypeFilterTypeEnumSV provides storage for a single-valued FilterTypeEnum
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:232)
 */
@SuppressWarnings("serial")
public class DmcTypeFilterTypeEnumSV extends DmcTypeFilterTypeEnum implements Serializable {

    protected FilterTypeEnum value;

    public DmcTypeFilterTypeEnumSV() {
    }

    public DmcTypeFilterTypeEnumSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeFilterTypeEnumSV getNew() {
        return (new DmcTypeFilterTypeEnumSV(attrInfo));
    }

    public DmcTypeFilterTypeEnumSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeFilterTypeEnumSV(ai));
    }

    @Override
    public DmcAttribute<FilterTypeEnum> cloneIt() {
        DmcTypeFilterTypeEnumSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public FilterTypeEnum getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public FilterTypeEnum set(Object v) throws DmcValueException {
        FilterTypeEnum rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public FilterTypeEnum getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
