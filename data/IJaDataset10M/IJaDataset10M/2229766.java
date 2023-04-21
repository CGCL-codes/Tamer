package odm.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import odm.diagram.edit.parts.OWLEditPart;
import odm.diagram.part.OdmVisualIDRegistry;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class SubDataPropertyOfViewFactory extends ConnectionViewFactory {

    /**
	 * @generated 
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createRoutingStyle());
        styles.add(NotationFactory.eINSTANCE.createFontStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = OdmVisualIDRegistry.getType(odm.diagram.edit.parts.SubDataPropertyOfEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!OWLEditPart.MODEL_ID.equals(OdmVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", OWLEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
    }
}
