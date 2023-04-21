package jopt.csp.spi.search.actions;

import java.util.Arrays;
import java.util.LinkedList;
import jopt.csp.search.LongSelector;
import jopt.csp.search.SearchAction;
import jopt.csp.search.VariableSelector;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will generate values for an array of variables.  It tries to
 * bind each variable to a single value such that all constraints are satisfied
 * and finishes searching upon finding the first consistent assignment.
 */
public class GenerateLongAction extends AbstractSearchNodeAction {

    private CspLongVariable vars[];

    private LongSelector selector;

    private VariableSelector varSelector;

    /**
	 * Creates new generation action
	 * 
	 * @param vars  Variable to instantiate
	 */
    public GenerateLongAction(CspLongVariable vars[]) {
        this(vars, null, null);
    }

    /**
	 * Creates new generation action
	 * 
	 * @param vars  		Variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 * @param varSelector	Used to select next variable to bind
	 */
    public GenerateLongAction(CspLongVariable vars[], LongSelector selector, VariableSelector varSelector) {
        this.vars = vars;
        this.selector = selector;
        this.varSelector = varSelector;
    }

    /**
	 * Called by search tree to execute this action.
	 * 
	 * @return Next action to execute in search
	 */
    public SearchAction performAction() throws PropagationFailureException {
        LinkedList<InstantiateLongAction> instantiateActions = new LinkedList<InstantiateLongAction>();
        if (varSelector != null) {
            varSelector.reset();
            varSelector.setVariables(vars);
            while (varSelector.hasNext()) {
                CspLongVariable var = (CspLongVariable) varSelector.next();
                if (!var.isBound()) instantiateActions.add(new InstantiateLongAction(var, selector));
            }
            varSelector.reset();
        } else {
            for (int i = 0; i < vars.length; i++) {
                CspLongVariable var = vars[i];
                if (!var.isBound()) instantiateActions.add(new InstantiateLongAction(var, selector));
            }
        }
        if (instantiateActions.size() == 1) return (SearchAction) instantiateActions.get(0);
        if (instantiateActions.size() > 0) return combineActions(instantiateActions);
        return null;
    }

    public String toString() {
        return "generate(" + Arrays.asList(vars) + ")";
    }
}
