package org.nowires.heimdall.validation;

import java.util.Vector;

/**
 * This entity serves as a container for rule volations. Its purpose is to
 * represent the validation of an object and hold all rule violations that 
 * occur. 
 * 
 * @author Yngve Espelid, yngvee@ii.uib.no
 */
public class ValidationSummary {

    /** A container for rule violations */
    private Vector<RuleViolation> rvs;

    /**
	 * A simple contsructor initializing the container.
	 */
    ValidationSummary() {
        rvs = new Vector<RuleViolation>();
    }

    /**
	 * Method for retrieving all rule violations that occured during
	 * validation of an object.
	 * 
	 * @return the container holding the rule violations
	 */
    public Vector<RuleViolation> getRuleViolations() {
        return rvs;
    }

    /**
	 * Method enabling the Validator to register rule violations.
	 * @param rv a rule violation tha occured during validation
	 */
    void addRuleViolation(RuleViolation rv) {
        rvs.add(rv);
    }

    /**
	 * Method for checking whether no rule violations occured during 
	 * validation of an object.
	 * 
	 * @return true if no rule violations occured, false otherwise
	 */
    public boolean isEmpty() {
        if (rvs.size() == 0) return true;
        return false;
    }

    /**
	 * The purpose of the toString() method is to give a textual 
	 * representation of all rule violations that occurred during
	 * validation of an object.
	 */
    public String toString() {
        String ruleViolations = "";
        for (RuleViolation rv : rvs) {
            System.out.println(rv);
        }
        return ruleViolations;
    }
}
