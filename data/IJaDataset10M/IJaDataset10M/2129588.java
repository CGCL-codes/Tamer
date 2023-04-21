package com.ssd.mdaworks.classdiagram.classDiagram.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractLabelViewFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class MOperationViewFactory extends AbstractLabelViewFactory {

    /**
	 * @generated
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramVisualIDRegistry.getType(com.ssd.mdaworks.classdiagram.classDiagram.diagram.edit.parts.MOperationEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
    }
}
