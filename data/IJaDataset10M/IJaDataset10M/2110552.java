package org.spbu.plweb.diagram.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.spbu.plweb.PlwebPackage;
import org.spbu.plweb.diagram.edit.parts.DocTopic4EditPart;
import org.spbu.plweb.diagram.part.PlwebDiagramUpdater;
import org.spbu.plweb.diagram.part.PlwebNodeDescriptor;
import org.spbu.plweb.diagram.part.PlwebVisualIDRegistry;

/**
 * @generated
 */
public class PageTopicPageCompartmentCanonicalEditPolicy extends CanonicalEditPolicy {

    /**
	 * @generated
	 */
    protected void refreshOnActivate() {
        List<?> c = getHost().getChildren();
        for (int i = 0; i < c.size(); i++) {
            ((EditPart) c.get(i)).activate();
        }
        super.refreshOnActivate();
    }

    /**
	 * @generated
	 */
    @SuppressWarnings("rawtypes")
    protected List getSemanticChildrenList() {
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection<EObject> semanticChildren, final View view) {
        return isMyDiagramElement(view) && !semanticChildren.contains(view.getElement());
    }

    /**
	 * @generated
	 */
    private boolean isMyDiagramElement(View view) {
        return false;
    }

    /**
	 * @generated
	 */
    protected void refreshSemantic() {
    }
}
