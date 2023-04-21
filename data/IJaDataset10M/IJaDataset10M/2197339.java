package com.google.gwt.dev.cfg;

import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import java.util.Iterator;

/**
 * A compound condition that is only satisfied if all of its children are
 * satisfied.
 */
public class ConditionAll extends CompoundCondition {

    public ConditionAll() {
    }

    protected boolean doEval(TreeLogger logger, PropertyOracle propertyOracle, TypeOracle typeOracle, String testType) throws UnableToCompleteException {
        for (Iterator<Condition> iter = getConditions().iterator(); iter.hasNext(); ) {
            Condition condition = iter.next();
            if (!condition.isTrue(logger, propertyOracle, typeOracle, testType)) {
                return false;
            }
        }
        return true;
    }

    protected String getEvalAfterMessage(String testType, boolean result) {
        if (result) {
            return "Yes: All subconditions were true";
        } else {
            return "No: One or more subconditions was false";
        }
    }

    protected String getEvalBeforeMessage(String testType) {
        return "Checking if all subconditions are true (<all>)";
    }
}
