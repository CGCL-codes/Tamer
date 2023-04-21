package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeDmsDefinitionREFSV provides storage for a single-valued DmsDefinitionREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:243)
 */
@SuppressWarnings("serial")
public class DmcTypeDmsDefinitionREFSV extends DmcTypeDmsDefinitionREF implements Serializable {

    protected DmsDefinitionREF value;

    public DmcTypeDmsDefinitionREFSV() {
    }

    public DmcTypeDmsDefinitionREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeDmsDefinitionREFSV getNew() {
        return (new DmcTypeDmsDefinitionREFSV(attrInfo));
    }

    public DmcTypeDmsDefinitionREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeDmsDefinitionREFSV(ai));
    }

    @Override
    public DmcAttribute<DmsDefinitionREF> cloneIt() {
        DmcTypeDmsDefinitionREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public DmsDefinitionREF getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public DmsDefinitionREF set(Object v) throws DmcValueException {
        DmsDefinitionREF rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public DmsDefinitionREF getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
