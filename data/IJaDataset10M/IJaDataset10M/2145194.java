package vilaug.peirce;

import jung.ext.icon.IconVertex;
import jung.ext.actions.AbstractMutationAction;
import jung.ext.actions.ActionFactory;

/**
 * The class <code>CreateDirectedEdgeAction</code> relays a particular
 * mutation object to the <code>MutationVisualizationViewer</code>class.
 * @see The (refactored) EditingPopupGraphPlugin for example
 * 
 * @author A.C. van Rossum
 */
public class CreateRelationAction extends AbstractMutationAction {

    /**
	 * The <code>CreateRelationAction</code> needs a source, target
	 * and related vertex. The first is the representamen, the second
	 * the referent, the third the interpretant in semi-Peircean sense. 
	 * @param source and reprentamen
	 * @param target and referent
	 * @param related and interpretant
	 * @param actionFactory
	 */
    public CreateRelationAction(IconVertex source, IconVertex target, IconVertex related, ActionFactory actionFactory) {
        super("Create relation", actionFactory);
        PeirceRelation relation = new PeirceRelation(source, target, related, null);
        mutation = ((PeirceMutationFactory) actionFactory.getMutationFactory()).createRelation(relation);
    }
}
