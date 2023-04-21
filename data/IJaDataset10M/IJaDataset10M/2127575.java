package org.hip.kernel.bom.impl;

import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;

/**
 * Implementation of <code>ICriteriaStack</code> for LDAP filters.
 *
 * @author Luthiger
 * Created on 10.07.2007
 */
public class LDAPCriteriaStack extends AbstractCriteriaStack {

    private static final String PATTERN_NORMAL = "%s(%s)";

    private static final String PATTERN_BRACKET = "%3$s(%1$s)(%2$s)";

    public String render() {
        if (criteria.size() < 1) return "";
        String outPrevious = criteria.remove(0);
        if (criteria.size() < 1) return outPrevious;
        operators.remove(0);
        BinaryBooleanOperator lPrevOp = operators.remove(0);
        String lPattern = PATTERN_NORMAL;
        outPrevious = String.format("%s(%s)(%s)", lPrevOp.getPrefixOperand(), outPrevious, criteria.remove(0));
        int i = 0;
        for (String lCriterium : criteria) {
            BinaryBooleanOperator lOperator = operators.get(i++);
            if (!lPrevOp.equals(lOperator)) {
                lPattern = PATTERN_BRACKET;
            }
            lPrevOp = lOperator;
            outPrevious = String.format(lPattern, outPrevious, lCriterium, lPrevOp.getPrefixOperand());
        }
        reset();
        return outPrevious;
    }
}
