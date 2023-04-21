package net.sourceforge.srr.utilities;

import java.util.List;
import net.sourceforge.srr.rule.Rule;
import net.sourceforge.srr.rule.action.RuleActionI;
import net.sourceforge.srr.rule.assertion.Assertion;
import net.sourceforge.srr.rule.assertion.AssertionVerbI;
import net.sourceforge.srr.rule.binarybooleanexpression.BinaryBooleanExpression;
import net.sourceforge.srr.rule.booleanexpression.BooleanExpressionI;
import net.sourceforge.srr.rule.booleanexpression.BooleanValue;
import net.sourceforge.srr.rule.condition.RuleCondition;
import net.sourceforge.srr.rule.condition.RuleConditionI;
import net.sourceforge.srr.rule.condition.RuleConditionWithConstraint;
import net.sourceforge.srr.rule.ruleset.RuleSet;

public class RuleSetToString {

    private static final String NL = StringUtilities.NEW_LINE;

    private static final String ONE_TAB = StringUtilities.ONE_TAB;

    private static final String ruleSetSeparator = "-----------------------------------------------------------------------------------------------------------";

    private static final String ruleSeparator = ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .";

    private final StringBuilder sb = new StringBuilder();

    private String currentIndent = "";

    private final String oneIndent = ONE_TAB;

    private boolean didVisitBooleanExpression = false;

    public String getRuleSetAsString(RuleSet ruleSet) {
        toString(ruleSet);
        return sb.toString();
    }

    private void toString(RuleSet ruleSet) {
        sb.append(NL);
        sb.append(ruleSetSeparator);
        sb.append(NL);
        sb.append(" Begin RuleSet: ");
        sb.append(ruleSet.getId());
        sb.append(NL);
        sb.append(ruleSetSeparator);
        List<Rule> rules = ruleSet.getRules();
        for (Rule rule : rules) {
            toString(rule);
            sb.append(NL);
            sb.append(NL);
            sb.append(ruleSeparator);
        }
        sb.append(NL);
        sb.append(ruleSetSeparator);
        sb.append(NL);
        sb.append(" End RuleSet: ");
        sb.append(ruleSet.getId());
        sb.append(NL);
        sb.append(ruleSetSeparator);
        sb.append(NL);
    }

    private void toString(Rule rule) {
        didVisitBooleanExpression = false;
        clearCurrentIndent();
        incrementCurrentIndent();
        sb.append(NL);
        appendNewLineWithIndent();
        sb.append("Rule Id: ");
        sb.append(rule.getId());
        appendNewLineWithIndent();
        sb.append("Rule Name: ");
        sb.append(rule.getName());
        appendNewLineWithIndent();
        sb.append("Rule Description: ");
        sb.append(rule.getDescription());
        sb.append(NL);
        toString(rule.getCondition());
        appendNewLineWithIndent();
        sb.append("Action: ");
        if (rule.getAction() == null) {
            sb.append("action is null");
        } else {
            sb.append(getRuleActionString(rule.getAction()));
        }
        decrementCurrentIndent();
    }

    private void toString(RuleConditionI ruleCondition) {
        if (ruleCondition instanceof RuleCondition) {
            toString((RuleCondition) ruleCondition);
        } else if (ruleCondition instanceof RuleConditionWithConstraint) {
            toString((RuleConditionWithConstraint) ruleCondition);
        } else {
            sb.append(NL);
            sb.append(NL);
            sb.append("ERROR:  Unrecognized implementer of RuleConditionI");
            sb.append(NL);
            sb.append(NL);
        }
    }

    private void toString(RuleCondition ruleCondition) {
        appendNewLineWithIndent();
        sb.append("RuleCondition is:");
        incrementCurrentIndent();
        appendNewLineWithIndent();
        sb.append("Conditions:");
        sb.append(NL);
        toString(ruleCondition.getConditionalExpression());
        sb.append(NL);
        decrementCurrentIndent();
    }

    private void toString(RuleConditionWithConstraint ruleCondition) {
        appendNewLineWithIndent();
        sb.append("RuleConditionWithConstraint is:");
        incrementCurrentIndent();
        appendNewLineWithIndent();
        sb.append("Conditions:");
        sb.append(NL);
        toString(ruleCondition.getConditionalExpression());
        sb.append(NL);
        appendNewLineWithIndent();
        sb.append("Constraint:");
        sb.append(NL);
        toString(ruleCondition.getConstraint());
        decrementCurrentIndent();
        sb.append(NL);
    }

    private void toString(BooleanExpressionI booleanExpression) {
        if (booleanExpression instanceof BinaryBooleanExpression) {
            toString((BinaryBooleanExpression) booleanExpression);
        } else if (booleanExpression instanceof BooleanValue) {
            toString((BooleanValue) booleanExpression);
        } else if (booleanExpression instanceof Assertion) {
            toString((Assertion) booleanExpression);
        } else {
            sb.append(NL);
            sb.append(NL);
            sb.append("ERROR:  Unrecognized implementer of BooleanExpressionI");
            sb.append(NL);
            sb.append(NL);
        }
    }

    private void toString(BinaryBooleanExpression bbe) {
        didVisitBooleanExpression = true;
        incrementCurrentIndent();
        if (bbe.getOperand1() == null) {
            sb.append("here is a BinaryBooleanExpression with operand1 = null; that's not right");
        } else {
            toString(bbe.getOperand1());
            appendNewLineWithIndent();
            sb.append(bbe.getOperator().toString());
            sb.append(NL);
            toString(bbe.getOperand2());
        }
        decrementCurrentIndent();
    }

    private void toString(BooleanValue bv) {
        incrementCurrentIndent();
        sb.append(currentIndent);
        sb.append(bv.toString());
        decrementCurrentIndent();
        sb.append(NL);
    }

    private void toString(Assertion assertion) {
        final String divider = " | ";
        if (!didVisitBooleanExpression) {
            incrementCurrentIndent();
        }
        sb.append(currentIndent);
        sb.append(assertion.getEvaluatedValue());
        sb.append(divider);
        sb.append(assertion.getSubject());
        sb.append(divider);
        sb.append(getAssertionVerbString(assertion.getVerb()));
        sb.append(divider);
        sb.append(assertion.getVerbObject());
        if (!didVisitBooleanExpression) {
            decrementCurrentIndent();
        }
    }

    private String getAssertionVerbString(AssertionVerbI verb) {
        return verb.getClass().getSimpleName();
    }

    private String getRuleActionString(RuleActionI action) {
        return action.getClass().getSimpleName();
    }

    private void clearCurrentIndent() {
        currentIndent = "";
    }

    private String incrementCurrentIndent() {
        currentIndent = currentIndent + oneIndent;
        return currentIndent;
    }

    private String decrementCurrentIndent() {
        currentIndent = currentIndent.substring(oneIndent.length());
        return currentIndent;
    }

    private void appendNewLineWithIndent() {
        sb.append(NL);
        sb.append(currentIndent);
    }
}
