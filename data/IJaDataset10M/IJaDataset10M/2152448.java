package net.sourceforge.srr.rule.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.srr.rule.assertion.AssertionSubject;
import net.sourceforge.srr.rule.booleanexpression.BooleanExpressionI;

/**
 * Each instance represents a set of related rules, normally from a RuleSet,
 * which share conditions but have different subjects in the constraint.
 * <p>
 * This is just for convenience in writing the rules in XML format. You could
 * simply write each rule without substitutions and not use this class.
 * <p>
 * Example: On all change requests, the following data are required: domain
 * properties: X, Y, Z
 * <p>
 * In this case, 3 rules would be created from this data. One for each of the
 * domain properties X, Y and Z. The 3 rules would be identical except that the
 * subject in the constraint for one would be X, for the other would be Y, an
 * for the last it would be Z.
 * <p>
 * A better name would be:
 * RuleConditionWithConstraintWithSubjectSubstitutionsInTheConstraint. But this
 * seems too long a name.
 * <p>
 * Instances of this class are replaced during the rule *reading* process with
 * rules whose conditions are RuleConditionWithConstraint, so instances of this
 * class are never evaluated during ruleSet.evaluate() or ruleSet.run();
 */
public class RuleConditionWithSubstitutions extends Object {

    private final List<AssertionSubject> subjects = new ArrayList<AssertionSubject>();

    private String description;

    private BooleanExpressionI constraint;

    private BooleanExpressionI conditionalExpression;

    public RuleConditionWithSubstitutions() {
    }

    public void addSubject(Object sub) {
        subjects.add((AssertionSubject) sub);
    }

    public List<AssertionSubject> getSubjects() {
        return Collections.unmodifiableList(this.subjects);
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setConditionalExpression(BooleanExpressionI cond) {
        conditionalExpression = cond;
    }

    public BooleanExpressionI getConditionalExpression() {
        return conditionalExpression;
    }

    public void setConstraint(BooleanExpressionI constr) {
        constraint = constr;
    }

    public BooleanExpressionI getConstraints() {
        return constraint;
    }

    static String lineSeparator = "\n";

    static String oneTab = "\t";

    @Override
    public String toString() {
        String returnValue = lineSeparator;
        returnValue = returnValue + "RuleConditionWithConstraint is:" + lineSeparator;
        returnValue = returnValue + oneTab + "Description:" + lineSeparator;
        returnValue = returnValue + oneTab + oneTab + this.description + lineSeparator;
        returnValue = returnValue + "Conditions:" + lineSeparator;
        returnValue = returnValue + this.getConditionalExpression().toString() + lineSeparator;
        returnValue = returnValue + lineSeparator + "Constraint:" + lineSeparator;
        returnValue = returnValue + oneTab + this.getConstraints().toString() + lineSeparator;
        return returnValue;
    }
}
