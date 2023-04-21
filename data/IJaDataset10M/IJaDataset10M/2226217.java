package ch.hsr.orm.model.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import ch.hsr.orm.model.diagram.edit.parts.UniOneToMany2EditPart;
import ch.hsr.orm.model.diagram.edit.parts.UniOneToManyOwnerAttributeEditPart;
import ch.hsr.orm.model.diagram.edit.parts.WrapLabel19EditPart;
import ch.hsr.orm.model.diagram.edit.parts.WrapLabel7EditPart;
import ch.hsr.orm.model.diagram.part.OrmmetaVisualIDRegistry;

/**
 * @generated
 */
public class UniOneToMany2ViewFactory extends ConnectionViewFactory {

    /**
	 * @generated
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createConnectorStyle());
        styles.add(NotationFactory.eINSTANCE.createFontStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = OrmmetaVisualIDRegistry.getType(UniOneToMany2EditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        IAdaptable eObjectAdapter = null;
        EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
        if (eObject != null) {
            eObjectAdapter = new EObjectAdapter(eObject);
        }
        getViewService().createNode(eObjectAdapter, view, OrmmetaVisualIDRegistry.getType(UniOneToManyOwnerAttributeEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, OrmmetaVisualIDRegistry.getType(WrapLabel7EditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(eObjectAdapter, view, OrmmetaVisualIDRegistry.getType(WrapLabel19EditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
    }
}
