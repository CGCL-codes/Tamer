package org.dmd.features.extgwt.examples.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;

/**
 * The DmcTypeFolderREFSV provides storage for a single-valued FolderREF
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1813)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNamedREF(DmoTypeFormatter.java:522)
 */
@SuppressWarnings("serial")
public class DmcTypeFolderREFSV extends DmcTypeFolderREF implements Serializable {

    protected FolderREF value;

    public DmcTypeFolderREFSV() {
    }

    public DmcTypeFolderREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeFolderREFSV getNew() {
        return (new DmcTypeFolderREFSV(attrInfo));
    }

    public DmcTypeFolderREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeFolderREFSV(ai));
    }

    @Override
    public DmcAttribute<FolderREF> cloneIt() {
        DmcTypeFolderREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public FolderREF getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public FolderREF set(Object v) throws DmcValueException {
        FolderREF rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public FolderREF getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
