package bpmn.diagram.view.factories;

import bpmn.diagram.edit.parts.BPMNDiagramEditPart;
import bpmn.diagram.edit.parts.LinkEventNameEditPart;
import bpmn.diagram.part.BpmnVisualIDRegistry;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class LinkEventViewFactory extends AbstractShapeViewFactory {

    /**
	 * @generated 
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createFontStyle());
        styles.add(NotationFactory.eINSTANCE.createDescriptionStyle());
        styles.add(NotationFactory.eINSTANCE.createFillStyle());
        styles.add(NotationFactory.eINSTANCE.createLineStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = BpmnVisualIDRegistry.getType(bpmn.diagram.edit.parts.LinkEventEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!BPMNDiagramEditPart.MODEL_ID.equals(BpmnVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", BPMNDiagramEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        getViewService().createNode(semanticAdapter, view, BpmnVisualIDRegistry.getType(LinkEventNameEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
