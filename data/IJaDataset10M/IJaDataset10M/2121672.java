package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.ResourceSet;

/**
 * Action that picks a resource from an alternative resource set
 */
public class AssignAlternativeResourceAction extends AbstractSearchNodeAction {

    private ResourceSet altResSet;

    private int resourceIndex;

    /**
     * Creates an AssignAlternativeResourceAction
     * @param altResSet set of resources
     * @param resourceIndex index into the set of the resource to select
     */
    public AssignAlternativeResourceAction(ResourceSet altResSet, int resourceIndex) {
        this.altResSet = altResSet;
        this.resourceIndex = resourceIndex;
    }

    /**
     * Sets the resource set from which a resource will be selected
     * @param ars the resource set
     */
    public void setAssignAlternativeResourceSet(ResourceSet ars) {
        this.altResSet = ars;
    }

    /**
     * Sets the index into the resource set that will be selected
     * @param resourceIndex the index
     */
    public void setResourceIndex(int resourceIndex) {
        this.resourceIndex = resourceIndex;
    }

    /**
     * Assigns the resource set to the resource index
     * @return null as there is no further action necessary
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
        altResSet.assignTo(resourceIndex);
        return null;
    }

    public String toString() {
        return "assign-resource(" + altResSet + ") to resource index: " + resourceIndex;
    }
}
