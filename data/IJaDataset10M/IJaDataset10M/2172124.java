package ch.hsr.orm.model.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import ch.hsr.orm.model.ModelPackage;
import ch.hsr.orm.model.diagram.edit.parts.AttributeEditPart;
import ch.hsr.orm.model.diagram.edit.parts.VersionAttributeEditPart;
import ch.hsr.orm.model.diagram.part.OrmmetaDiagramUpdater;
import ch.hsr.orm.model.diagram.part.OrmmetaNodeDescriptor;
import ch.hsr.orm.model.diagram.part.OrmmetaVisualIDRegistry;

/**
 * @generated
 */
public class EntityAttributeCompartmentCanonicalEditPolicy extends CanonicalEditPolicy {

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
        for (Iterator it = OrmmetaDiagramUpdater.getEntityEntityAttributeCompartment_5001SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((OrmmetaNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = OrmmetaVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case AttributeEditPart.VISUAL_ID:
            case VersionAttributeEditPart.VISUAL_ID:
                return !semanticChildren.contains(view.getElement()) || visualID != OrmmetaVisualIDRegistry.getNodeVisualID((View) getHost().getModel(), view.getElement());
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
            myFeaturesToSynchronize.add(ModelPackage.eINSTANCE.getPersistable_Attributes());
            myFeaturesToSynchronize.add(ModelPackage.eINSTANCE.getEntity_VersionAttribute());
        }
        return myFeaturesToSynchronize;
    }
}
