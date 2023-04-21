package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that removes a single value from the domain of a numeric variable
 */
public class RemoveFloatAction implements SearchAction {

    private CspFloatVariable var;

    private float val;

    public RemoveFloatAction(CspFloatVariable var, float val) {
        this.var = var;
        this.val = val;
    }

    public SearchAction performAction() throws PropagationFailureException {
        var.removeValue(val);
        return null;
    }

    public String toString() {
        return "remove-number(" + var + ", " + val + ")";
    }
}
