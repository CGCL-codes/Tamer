package se.mdh.mrtc.saveccm.composite.diagram.view.factories;

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
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.CompositeEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayDelayAttributsCompartmentEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayDelayEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayDelayModelsCompartmentEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayNameEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.edit.parts.DelayPrecisionEditPart;
import se.mdh.mrtc.saveccm.composite.diagram.part.SaveccmVisualIDRegistry;

/**
 * @generated
 */
public class DelayViewFactory extends AbstractShapeViewFactory {

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
            semanticHint = SaveccmVisualIDRegistry.getType(DelayEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!CompositeEditPart.MODEL_ID.equals(SaveccmVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", CompositeEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        IAdaptable eObjectAdapter = null;
        EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
        if (eObject != null) {
            eObjectAdapter = new EObjectAdapter(eObject);
        }
        getViewService().createNode(eObjectAdapter, view, SaveccmVisualIDRegistry.getType(DelayNameEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, SaveccmVisualIDRegistry.getType(DelayDelayEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, SaveccmVisualIDRegistry.getType(DelayPrecisionEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, SaveccmVisualIDRegistry.getType(DelayDelayAttributsCompartmentEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, SaveccmVisualIDRegistry.getType(DelayDelayModelsCompartmentEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
