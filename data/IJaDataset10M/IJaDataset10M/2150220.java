package org.dmd.dmp.shared.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmp.shared.generated.enums.ResponseTypeEnum;

/**
 * The DmcTypeResponseTypeEnumSV provides storage for a single-valued ResponseTypeEnum
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpEnumType(DmoTypeFormatter.java:360)
 */
@SuppressWarnings("serial")
public class DmcTypeResponseTypeEnumSV extends DmcTypeResponseTypeEnum implements Serializable {

    protected ResponseTypeEnum value;

    public DmcTypeResponseTypeEnumSV() {
    }

    public DmcTypeResponseTypeEnumSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeResponseTypeEnumSV getNew() {
        return (new DmcTypeResponseTypeEnumSV(attrInfo));
    }

    public DmcTypeResponseTypeEnumSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeResponseTypeEnumSV(ai));
    }

    @Override
    public DmcAttribute<ResponseTypeEnum> cloneIt() {
        DmcTypeResponseTypeEnumSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public ResponseTypeEnum getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public ResponseTypeEnum set(Object v) throws DmcValueException {
        ResponseTypeEnum rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public ResponseTypeEnum getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
