package jung.ext.actions;

import edu.uci.ics.jung.graph.Vertex;

/**
 * The class <code>CreateEdgeAction</code> has to be overloaded to relay
 * a particular mutation object to the 
 * <code>MutationVisualizationViewer</code> class.
 * @see The (refactored) EditingPopupGraphPlugin for example
 * 
 * @author A.C. van Rossum
 */
public abstract class CreateEdgeAction extends AbstractMutationAction {

    public CreateEdgeAction(Vertex source, Vertex target, ActionFactory actionFactory) {
        super("[" + source + "," + target + "]", actionFactory);
    }
}
