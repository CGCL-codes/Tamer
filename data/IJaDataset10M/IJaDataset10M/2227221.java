package se.mdh.mrtc.save.taEditor.diagram.view.factories;

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

/**
 * @generated
 */
public class LocationViewFactory extends AbstractShapeViewFactory {

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
            semanticHint = se.mdh.mrtc.save.taEditor.diagram.part.TaEditorVisualIDRegistry.getType(se.mdh.mrtc.save.taEditor.diagram.edit.parts.LocationEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!se.mdh.mrtc.save.taEditor.diagram.edit.parts.TAEditPart.MODEL_ID.equals(se.mdh.mrtc.save.taEditor.diagram.part.TaEditorVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", se.mdh.mrtc.save.taEditor.diagram.edit.parts.TAEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        IAdaptable eObjectAdapter = null;
        EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
        if (eObject != null) {
            eObjectAdapter = new EObjectAdapter(eObject);
        }
        getViewService().createNode(eObjectAdapter, view, se.mdh.mrtc.save.taEditor.diagram.part.TaEditorVisualIDRegistry.getType(se.mdh.mrtc.save.taEditor.diagram.edit.parts.LocationLocationNameEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, se.mdh.mrtc.save.taEditor.diagram.part.TaEditorVisualIDRegistry.getType(se.mdh.mrtc.save.taEditor.diagram.edit.parts.LocationInvariantEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
