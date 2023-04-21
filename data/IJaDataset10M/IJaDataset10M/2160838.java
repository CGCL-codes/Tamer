package net.sourceforge.srr.rule.evaluation;

/**
 * Each instance represents information about a rule that has been evaluated.
 */
public class EvaluatedRule {

    /**
     * A universally unique identifier for the rule.
     */
    private final long ruleId;

    /**
     * The description from the rule that was evaluated
     */
    private final String ruleDescription;

    /**
     * Null before the conditions have been evaluated. True when the conditions
     * evaluate to true False when the conditions evaluate to false
     * Unpredictable on exceptions
     */
    private Boolean conditionsMet;

    /**
     * For rule conditions with constraints, only. Null before the constraint
     * has been evaluated True when the constraint evaluate to true (satisfied)
     * False when the constraint evaluate to false (violated) Unpredictable on
     * exceptions
     */
    private Boolean constraintSatisfied;

    /**
     * Tells if the evaluation of the rule says that the action should be taken.
     * <p>
     * This depends on the setting of the two Rule instance variables:
     * runActionWhenConditionsAre runActionWhenConstraintIs
     * <p>
     * Examples:
     * <p>
     * When the rule uses a RuleCondition:
     * <p>
     * and runActionWhenConditionsAre = true; the action should be taken when
     * the conditions evaluate to true.
     * <p>
     * <p>
     * When the rule uses a RuleConditionWithConstraint:
     * <p>
     * and runActionWhenConstraintIs = false; the action should be taken when
     * the constraint is broken.
     */
    private Boolean shouldDoAction;

    public long getRuleId() {
        return ruleId;
    }

    /**
     * Construct an instance.
     * <p>
     * 
     * @param rule
     *            the rule which will be evaluated.
     */
    public EvaluatedRule(long ruleId, String desc) {
        this.ruleId = ruleId;
        this.ruleDescription = desc;
    }

    /**
     * @return the description of the rule
     */
    public String getDescription() {
        return this.ruleDescription;
    }

    /**
     * @return Null before the conditions have been evaluated True when the
     *         conditions evaluate to true False when the conditions evaluate to
     *         false Unpredictable on exceptions
     */
    public Boolean getConditionsMet() {
        return conditionsMet;
    }

    /**
     * Set the conditionsMet instance variable.
     * 
     * @param conditionsMet
     *            The value to set.
     */
    public void setConditionsMet(Boolean conditionsMet) {
        this.conditionsMet = conditionsMet;
    }

    /**
     * For rule conditions with constraints, only
     * 
     * @return Null before the constraint has been evaluated True when the
     *         constraint evaluate to true (satisfied) False when the constraint
     *         evaluate to false (violated) Unpredictable on exceptions
     */
    public Boolean getConstraintSatisfied() {
        return constraintSatisfied;
    }

    /**
     * @param constrainSatisfied
     *            The constraintSatisfied to set.
     */
    public void setConstraintSatisfied(Boolean constrainSatisfied) {
        this.constraintSatisfied = constrainSatisfied;
    }

    /**
     * @return Returns the shouldDoAction.
     */
    public Boolean getShouldDoAction() {
        return shouldDoAction;
    }

    /**
     * @param shouldDoAction
     *            The value to set.
     */
    public void setShouldDoAction(Boolean shouldDoAction) {
        this.shouldDoAction = shouldDoAction;
    }
}
