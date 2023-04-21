package org.dmd.dmv.shared.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmv.shared.generated.dmo.IntegerRangeRuleDMO;

/**
 * The DmcTypeIntegerRangeRuleREFSV provides storage for a single-valued IntegerRangeRule
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpSVType(GenUtility.java:1833)
 *    Called from: org.dmd.dms.util.DmoTypeFormatter.dumpNormalREFType(DmoTypeFormatter.java:254)
 */
@SuppressWarnings("serial")
public class DmcTypeIntegerRangeRuleREFSV extends DmcTypeIntegerRangeRuleREF implements Serializable {

    protected IntegerRangeRuleDMO value;

    public DmcTypeIntegerRangeRuleREFSV() {
    }

    public DmcTypeIntegerRangeRuleREFSV(DmcAttributeInfo ai) {
        super(ai);
    }

    public DmcTypeIntegerRangeRuleREFSV getNew() {
        return (new DmcTypeIntegerRangeRuleREFSV(attrInfo));
    }

    public DmcTypeIntegerRangeRuleREFSV getNew(DmcAttributeInfo ai) {
        return (new DmcTypeIntegerRangeRuleREFSV(ai));
    }

    @Override
    public DmcAttribute<IntegerRangeRuleDMO> cloneIt() {
        DmcTypeIntegerRangeRuleREFSV rc = getNew();
        rc.value = value;
        return (rc);
    }

    public IntegerRangeRuleDMO getSVCopy() {
        if (value == null) return (null);
        return (cloneValue(value));
    }

    @Override
    public IntegerRangeRuleDMO set(Object v) throws DmcValueException {
        IntegerRangeRuleDMO rc = typeCheck(v);
        if (value == null) value = rc; else {
            if (value.equals(rc)) rc = null; else value = rc;
        }
        return (rc);
    }

    @Override
    public IntegerRangeRuleDMO getSV() {
        return (value);
    }

    @Override
    public int getMVSize() {
        return (0);
    }
}
