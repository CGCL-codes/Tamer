package org.gridbus.broker.gui.rule;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> RuleEngine </node>
 */
public final class RuleEngineFactory {

    public static RuleEngine newRuleEngine(String type) throws RuleException {
        RuleEngine engine = null;
        if ("xpml".equalsIgnoreCase(type)) {
            engine = new XpmlRuleEngine();
        } else if ("ServiceDescription".equalsIgnoreCase(type)) {
            engine = new XgrlRuleEngine();
        } else if ("JobDefinition".equalsIgnoreCase(type)) {
            engine = new JsdlRuleEngine();
        } else if ("CredentialDescription".equalsIgnoreCase(type)) {
            engine = new CredentialRuleEngine();
        } else if ("ApplicationDescription".equalsIgnoreCase(type)) {
            engine = new AdlRuleEngine();
        } else {
            engine = new GenericRuleEngine();
        }
        return engine;
    }
}
