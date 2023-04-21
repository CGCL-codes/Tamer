package jfb.examples.gmf.filesystem.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import jfb.examples.gmf.filesystem.FilesystemPackage;
import jfb.examples.gmf.filesystem.diagram.edit.parts.File2EditPart;
import jfb.examples.gmf.filesystem.diagram.edit.parts.Folder2EditPart;
import jfb.examples.gmf.filesystem.diagram.part.FilesystemDiagramUpdater;
import jfb.examples.gmf.filesystem.diagram.part.FilesystemNodeDescriptor;
import jfb.examples.gmf.filesystem.diagram.part.FilesystemVisualIDRegistry;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class FolderFolderCompartment2CanonicalEditPolicy extends CanonicalEditPolicy {

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
        for (Iterator it = FilesystemDiagramUpdater.getFolderFolderCompartment_7002SemanticChildren(viewObject).iterator(); it.hasNext(); ) {
            result.add(((FilesystemNodeDescriptor) it.next()).getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection semanticChildren, final View view) {
        int visualID = FilesystemVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case Folder2EditPart.VISUAL_ID:
            case File2EditPart.VISUAL_ID:
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
            myFeaturesToSynchronize.add(FilesystemPackage.eINSTANCE.getFolder_Folders());
            myFeaturesToSynchronize.add(FilesystemPackage.eINSTANCE.getFolder_Files());
        }
        return myFeaturesToSynchronize;
    }
}
