package org.dmd.mvw.tools.mvwgenerator.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeMenuBarREFSV provides storage for a single-valued MenuBarREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:530)
 */
@SuppressWarnings("serial")
public class DmcTypeMenuBarREFSV extends DmcTypeMenuBarREF implements Serializable {

    protected MenuBarREF value;

    public DmcTypeMenuBarREFSV() {
    }

    public DmcTypeMenuBarREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeMenuBarREFSV getNew() {
        return (new DmcTypeMenuBarREFSV(attrInfo));
    }

    public DmcTypeMenuBarREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeMenuBarREFSV(ai));
    }

    @Override
    public DmcAttribute<MenuBarREF> cloneIt() {
        DmcTypeMenuBarREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public MenuBarREF getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public MenuBarREF set(Object v) throws DmcValueException {
        MenuBarREF rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public MenuBarREF getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
