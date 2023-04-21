package hub.sam.mof.simulator.editor.diagram.edit.policies;

import hub.sam.mof.simulator.editor.diagram.edit.parts.MActivityEditPart;
import hub.sam.mof.simulator.editor.diagram.edit.parts.MClassEditPart;
import hub.sam.mof.simulator.editor.diagram.part.M3ActionsDiagramUpdater;
import hub.sam.mof.simulator.editor.diagram.part.M3ActionsNodeDescriptor;
import hub.sam.mof.simulator.editor.diagram.part.M3ActionsVisualIDRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class EPackageContentsCanonicalEditPolicy extends CanonicalEditPolicy {

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
        for (Iterator it = M3ActionsDiagramUpdater.getEPackageContents_5003SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((M3ActionsNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated NOT
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        if (view.getEAnnotation("Shortcut") != null) {
            return M3ActionsDiagramUpdater.isShortcutOrphaned(view);
        }
        int visualID = M3ActionsVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EClass2EditPart.VISUAL_ID:
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataType2EditPart.VISUAL_ID:
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnum2EditPart.VISUAL_ID:
            case MClassEditPart.VISUAL_ID:
            case MActivityEditPart.VISUAL_ID:
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
            myFeaturesToSynchronize.add(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
        }
        return myFeaturesToSynchronize;
    }

    /**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#refreshSemantic()
	 */
    @Override
    protected void refreshSemantic() {
        deleteOrphanedViews();
    }

    /**
	 * Delete orphaned views
	 */
    protected void deleteOrphanedViews() {
        if (resolveSemanticElement() == null) {
            return;
        }
        List viewChildren = getViewChildren();
        List semanticChildren = new ArrayList(getSemanticChildrenList());
        List orphaned = cleanCanonicalSemanticChildren(viewChildren, semanticChildren);
        boolean changed = false;
        if (!orphaned.isEmpty()) {
            changed = deleteViews(orphaned.iterator());
        }
    }
}
