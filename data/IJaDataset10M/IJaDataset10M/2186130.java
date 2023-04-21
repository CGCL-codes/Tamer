package de.mpiwg.vspace.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import de.mpiwg.vspace.diagram.edit.parts.ExhibitionEditPart;
import de.mpiwg.vspace.diagram.edit.parts.SceneEditPart;
import de.mpiwg.vspace.diagram.edit.parts.SceneSceneLinkLabelCompartmentEditPart;
import de.mpiwg.vspace.diagram.edit.parts.SceneTitleEditPart;
import de.mpiwg.vspace.diagram.part.ExhibitionVisualIDRegistry;

/**
 * @generated
 */
public class SceneViewFactory extends AbstractShapeViewFactory {

    /**
	 * @generated
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createShapeStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = ExhibitionVisualIDRegistry.getType(SceneEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!ExhibitionEditPart.MODEL_ID.equals(ExhibitionVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", ExhibitionEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        IAdaptable eObjectAdapter = null;
        EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
        if (eObject != null) {
            eObjectAdapter = new EObjectAdapter(eObject);
        }
        getViewService().createNode(eObjectAdapter, view, ExhibitionVisualIDRegistry.getType(SceneTitleEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, ExhibitionVisualIDRegistry.getType(SceneSceneLinkLabelCompartmentEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
