package net.sf.opentranquera.rules;

/**
 * This exception is thrown when an error occurs while execute a rule.
 * 
 * @author Diego Campodonico
 */
public class EvaluationRuleException extends RuleException {

    public EvaluationRuleException(String message) {
        super(message);
    }
}
