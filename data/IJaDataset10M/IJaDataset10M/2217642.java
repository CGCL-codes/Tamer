package org.jalgo.module.ebnf.controller.ebnf;

import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.Rule;
import org.jalgo.module.ebnf.util.IAction;

/**
 * This is the action to delete a rule
 * 
 * @author Tom Kazimiers, Johannes Mey
 * 
 */
public class DeleteRuleAction implements IAction {

    private Rule rule;

    private EbnfController controller;

    /**
	 * @param controller
	 *            the controller that owns the definition
	 * @param rule
	 *            the rule to be deleted
	 */
    public DeleteRuleAction(EbnfController controller, Rule rule) {
        this.rule = rule;
        this.controller = controller;
    }

    public void undo() throws DefinitionFormatException {
        controller.getDefinition().addRule(rule);
    }

    public void perform() throws DefinitionFormatException {
        controller.getDefinition().removeRule(rule);
    }
}
