package com.free2be.dimensions.config;

import com.free2be.dimensions.decider.Decision;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;
import org.xml.sax.Attributes;

/**
 * This class contains the rules used by the digester to parse the configuration
 * file.
 * 
 * @author Antonio Petrelli
 */
public class DimensionsConfigRuleSet extends RuleSetBase {

    /** Creates a new instance of DimensionsConfigRuleSet */
    public DimensionsConfigRuleSet() {
    }

    /**
     * Adds the rule instances to the digester.
     * 
     * @param digester
     *            The digester to use.
     */
    public void addRuleInstances(Digester digester) {
        digester.addObjectCreate("dimensions-config/decider", "com.free2be.dimensions.decider.UserDeviceDecider", "className");
        digester.addSetProperty("dimensions-config/decider/set-property", "name", "value");
        digester.addSetNext("dimensions-config/decider", "setDecider", "com.free2be.dimensions.decider.Decider");
        digester.addObjectCreate("dimensions-config/inheritance-resolver", "com.free2be.dimensions.tiles.inheritance.UserDeviceInheritanceResolver", "className");
        digester.addSetNext("dimensions-config/inheritance-resolver", "setResolver", "com.free2be.dimensions.tiles.inheritance.InheritanceResolver");
        digester.addObjectCreate("dimensions-config/decisions/decision", Decision.class);
        digester.addRule("dimensions-config/decisions/decision/parameter", new AddDecisionPropertyRule());
        digester.addRule("dimensions-config/decisions/decision/definitions-config", new SetTilesDefsPathRule());
        digester.addSetNext("dimensions-config/decisions/decision", "addSuitableDecision", "com.free2be.dimensions.decider.Decision");
    }

    final class AddDecisionPropertyRule extends Rule {

        public void begin(String namespace, String name, Attributes attributes) throws Exception {
            Decision decision;
            decision = (Decision) digester.peek();
            decision.setParameter(attributes.getValue("name"), attributes.getValue("value"));
        }
    }

    final class SetTilesDefsPathRule extends Rule {

        public void begin(String namespace, String name, Attributes attributes) throws Exception {
            Decision decision;
            DimensionsConfig config;
            decision = (Decision) digester.peek();
            config = (DimensionsConfig) digester.peek(1);
            config.getResolver().addDefinitionsPath(decision, attributes.getValue("path"));
        }
    }
}
