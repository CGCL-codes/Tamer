package org.dmd.dmt.shared.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmt.shared.generated.dmo.ClientCountFilterDMO;

/**
 * The DmcTypeClientCountFilterREFSV provides storage for a single-valued ClientCountFilter
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNormalREFType(DmoTypeFormatter.java:254)
 */
@SuppressWarnings("serial")
public class DmcTypeClientCountFilterREFSV extends DmcTypeClientCountFilterREF implements Serializable {

    protected ClientCountFilterDMO value;

    public DmcTypeClientCountFilterREFSV() {
    }

    public DmcTypeClientCountFilterREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeClientCountFilterREFSV getNew() {
        return (new DmcTypeClientCountFilterREFSV(attrInfo));
    }

    public DmcTypeClientCountFilterREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeClientCountFilterREFSV(ai));
    }

    @Override
    public DmcAttribute<ClientCountFilterDMO> cloneIt() {
        DmcTypeClientCountFilterREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public ClientCountFilterDMO getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public ClientCountFilterDMO set(Object v) throws DmcValueException {
        ClientCountFilterDMO rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public ClientCountFilterDMO getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
