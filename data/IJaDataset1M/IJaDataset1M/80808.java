package cz.vse.gebz.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.diagram.edit.parts.BazeZnalosti4EditPart;
import cz.vse.gebz.diagram.edit.parts.BinarniAtribut3EditPart;
import cz.vse.gebz.diagram.edit.parts.NominalniAtribut3EditPart;
import cz.vse.gebz.diagram.edit.parts.NumerickyAtribut3EditPart;
import cz.vse.gebz.diagram.part.BzDiagramUpdater;
import cz.vse.gebz.diagram.part.BzNodeDescriptor;
import cz.vse.gebz.diagram.part.BzVisualIDRegistry;

/**
 * @generated
 */
public class BazeZnalostiDotazyCanonicalEditPolicy extends CanonicalEditPolicy {

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
        for (Iterator it = BzDiagramUpdater.getBazeZnalostiDotazy_7032SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((BzNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = BzVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case BazeZnalosti4EditPart.VISUAL_ID:
            case BinarniAtribut3EditPart.VISUAL_ID:
            case NominalniAtribut3EditPart.VISUAL_ID:
            case NumerickyAtribut3EditPart.VISUAL_ID:
                return !semanticChildren.contains(view.getElement()) || visualID != BzVisualIDRegistry.getNodeVisualID((View) getHost().getModel(), view.getElement());
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
            myFeaturesToSynchronize.add(GebzPackage.eINSTANCE.getBazeZnalosti_Objekty());
        }
        return myFeaturesToSynchronize;
    }
}
