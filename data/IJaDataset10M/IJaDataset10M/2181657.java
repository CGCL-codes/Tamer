package org.dmd.dms.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeActionDefinitionREFSV provides storage for a single-valued ActionDefinitionREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpDerivedTypes(MetaGenerator.java:243)
 */
@SuppressWarnings("serial")
public class DmcTypeActionDefinitionREFSV extends DmcTypeActionDefinitionREF implements Serializable {

    protected ActionDefinitionREF value;

    public DmcTypeActionDefinitionREFSV() {
    }

    public DmcTypeActionDefinitionREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeActionDefinitionREFSV getNew() {
        return (new DmcTypeActionDefinitionREFSV(attrInfo));
    }

    public DmcTypeActionDefinitionREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeActionDefinitionREFSV(ai));
    }

    @Override
    public DmcAttribute<ActionDefinitionREF> cloneIt() {
        DmcTypeActionDefinitionREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public ActionDefinitionREF getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public ActionDefinitionREF set(Object v) throws DmcValueException {
        ActionDefinitionREF rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public ActionDefinitionREF getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
