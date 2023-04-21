package org.qtitools.qti.node.outcome.processing;

import java.util.List;
import org.qtitools.qti.exception.QTIProcessingInterrupt;
import org.qtitools.qti.group.outcome.processing.OutcomeRuleGroup;
import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.validation.ValidationResult;
import org.qtitools.qti.validation.ValidationWarning;

/**
 * An outcomeProcessingFragment is A simple group of outcomeRules which are grouped together in order to allow them
 * to be managed as A separate resource. It should not be used for any other purpose.
 * 
 * @author Jiri Kajaba
 */
public class OutcomeProcessingFragment extends OutcomeRule {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "outcomeProcessingFragment";

    /**
	 * Constructs rule.
	 *
	 * @param parent parent of this rule
	 */
    public OutcomeProcessingFragment(XmlObject parent) {
        super(parent);
        getNodeGroups().add(new OutcomeRuleGroup(this));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
	 * Gets outcomeRule children.
	 *
	 * @return outcomeRule children
	 */
    public List<OutcomeRule> getOutcomeRules() {
        return getNodeGroups().getOutcomeRuleGroup().getOutcomeRules();
    }

    @Override
    protected ValidationResult validateChildren() {
        ValidationResult result = super.validateChildren();
        if (getOutcomeRules().size() == 0) result.add(new ValidationWarning(this, "Node " + CLASS_TAG + " should contain some rules."));
        return result;
    }

    @Override
    public void evaluate() throws QTIProcessingInterrupt {
        for (OutcomeRule outcomeRule : getOutcomeRules()) outcomeRule.evaluate();
    }
}
