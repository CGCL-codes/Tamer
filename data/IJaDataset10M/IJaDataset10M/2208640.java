package se.mdh.mrtc.saveccm.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.SaveccmPackage;
import se.mdh.mrtc.saveccm.diagram.edit.parts.CombinedIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.CombinedOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.DataIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.DataOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.TriggerIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.TriggerOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmDiagramUpdater;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmNodeDescriptor;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;

/**
 * @generated
 */
public class SwitchCanonicalEditPolicy extends CanonicalEditPolicy {

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
        for (Iterator it = SaveccmDiagramUpdater.getSwitch_1002SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((SaveccmNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = SaveccmVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case TriggerIn3EditPart.VISUAL_ID:
            case TriggerOut3EditPart.VISUAL_ID:
            case DataIn3EditPart.VISUAL_ID:
            case DataOut3EditPart.VISUAL_ID:
            case CombinedIn3EditPart.VISUAL_ID:
            case CombinedOut3EditPart.VISUAL_ID:
                return !semanticChildren.contains(view.getElement()) || visualID != SaveccmVisualIDRegistry.getNodeVisualID((View) getHost().getModel(), view.getElement());
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
            myFeaturesToSynchronize.add(SaveccmPackage.eINSTANCE.getElement_Offer());
        }
        return myFeaturesToSynchronize;
    }
}
