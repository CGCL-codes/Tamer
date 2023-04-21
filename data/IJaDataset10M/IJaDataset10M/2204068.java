package com.hofstetter.diplthesis.ctb.ctb.diagram.view.factories;

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
import com.hofstetter.diplthesis.ctb.ctb.diagram.edit.parts.DiagramEditPart;
import com.hofstetter.diplthesis.ctb.ctb.diagram.edit.parts.PatternInformationEditPart;
import com.hofstetter.diplthesis.ctb.ctb.diagram.edit.parts.PatternInformationPatternNameEditPart;
import com.hofstetter.diplthesis.ctb.ctb.diagram.part.CtbVisualIDRegistry;

/**
 * @generated
 */
public class PatternInformationViewFactory extends AbstractShapeViewFactory {

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
            semanticHint = CtbVisualIDRegistry.getType(PatternInformationEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!DiagramEditPart.MODEL_ID.equals(CtbVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", DiagramEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        IAdaptable eObjectAdapter = null;
        EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
        if (eObject != null) {
            eObjectAdapter = new EObjectAdapter(eObject);
        }
        getViewService().createNode(eObjectAdapter, view, CtbVisualIDRegistry.getType(PatternInformationPatternNameEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
