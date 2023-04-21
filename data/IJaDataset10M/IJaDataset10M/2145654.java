package toxtree.ui.tree.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionRule;
import toxTree.io.Tools;

/**
 * Launches {@link toxTree.core.IDecisionRuleEditor} of a preset rule.
 * An {@link toxtree.ui.tree.actions.AbstractTreeAction} descendant
 * 
 * @author Nina  jeliazkova
 * <b>Modified</b> 2005-10-23
 */
public class EditRuleAction extends AbstractTreeAction implements IRuleAction {

    protected IDecisionRule rule;

    private static final long serialVersionUID = 2770657186363871323L;

    public EditRuleAction(IDecisionMethod tree) {
        this(tree, "Edit rule");
    }

    public EditRuleAction(IDecisionMethod tree, String arg0) {
        this(tree, arg0, Tools.getImage("tree.png"));
    }

    public EditRuleAction(IDecisionMethod tree, String arg0, Icon arg1) {
        super(tree, arg0, arg1);
        rule = null;
        putValue(AbstractAction.SHORT_DESCRIPTION, "Launches rule visualization/editor.");
    }

    public void actionPerformed(ActionEvent arg0) {
        if (rule != null) {
            rule.getEditor().edit(getParentFrame(), rule);
        }
    }

    public IDecisionRule getRule() {
        return rule;
    }

    public void setRule(IDecisionRule rule) {
        this.rule = rule;
    }
}
