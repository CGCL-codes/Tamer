package webml.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import webml.WebmlPackage;
import webml.diagram.edit.parts.DocTopicEditPart;
import webml.diagram.part.WebmlDiagramUpdater;
import webml.diagram.part.WebmlNodeDescriptor;
import webml.diagram.part.WebmlVisualIDRegistry;

/**
 * @generated
 */
public class AreaAreaTopicCompartment2CanonicalEditPolicy extends CanonicalEditPolicy {

    /**
	 * @generated
	 */
    Set myFeaturesToSynchronize;

    /**
	 * @generated
	 */
    protected List getSemanticChildrenList() {
        View viewObject = (View) getHost().getModel();
        List result = new LinkedList();
        for (Iterator it = WebmlDiagramUpdater.getAreaAreaTopicCompartment_7009SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((WebmlNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = WebmlVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case DocTopicEditPart.VISUAL_ID:
                if (!semanticChildren.contains(view.getElement())) {
                    return true;
                }
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected String getDefaultFactoryHint() {
        return null;
    }

    /**
	 * @generated
	 */
    protected Set getFeaturesToSynchronize() {
        if (myFeaturesToSynchronize == null) {
            myFeaturesToSynchronize = new HashSet();
            myFeaturesToSynchronize.add(WebmlPackage.eINSTANCE.getContentUnit_Topic());
        }
        return myFeaturesToSynchronize;
    }
}
