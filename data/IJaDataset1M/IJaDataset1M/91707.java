package net.sourceforge.srr.testutilities.testapplication.rule.assertion.verbs4stringvalues;

import net.sourceforge.srr.domainproperty.value.DomainPropertyValueNullException;
import net.sourceforge.srr.rule.assertion.AssertionVerbI;
import net.sourceforge.srr.rule.evaluation.RuleEvaluationContextI;
import net.sourceforge.srr.testutilities.testapplication.rule.assertion.AssertionVerbDpv;

/**
 * Represents the assertion that a DomainPropertyValue is populated.
 */
public class ValuePopulated extends AssertionVerbDpv implements AssertionVerbI {

    /**
     * @return Returns true when the value of the subject from the argument
     *         condition is populated in the argument context
     * 
     * @param assertion
     *            the assertion that is being evaluated
     * @param context
     *            the context in which the evaluation is being performed
     */
    public boolean applyVerb(Object subject, Object verbObject, RuleEvaluationContextI context) {
        Object value = null;
        try {
            value = getApplicationValue(subject, context);
        } catch (DomainPropertyValueNullException e) {
        }
        if (null == value) {
            return false;
        }
        return true;
    }

    /**
     * @param subject
     *            the subject of the assertion
     * @param context
     *            the rule-running context
     * 
     * @return the value from the current state of the application
     */
    public boolean requiresObject() {
        return false;
    }
}
