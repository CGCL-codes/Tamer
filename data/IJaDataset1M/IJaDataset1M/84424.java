package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.types.Field;
import org.dmd.dms.generated.types.DmcTypeField;

/**
 * The DmcTypeFieldSV provides storage for a single-valued Field
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:270)
 */
@SuppressWarnings("serial")
public class DmcTypeFieldSV extends DmcTypeField implements Serializable {

    protected Field value;

    public DmcTypeFieldSV() {
    }

    public DmcTypeFieldSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeFieldSV getNew() {
        return (new DmcTypeFieldSV(attrInfo));
    }

    public DmcTypeFieldSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeFieldSV(ai));
    }

    @Override
    public DmcAttribute<Field> cloneIt() {
        DmcTypeFieldSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public Field getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public Field set(Object v) throws DmcValueException {
        Field rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public Field getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
