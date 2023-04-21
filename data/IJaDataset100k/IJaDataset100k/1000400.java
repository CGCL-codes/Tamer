package de.mpiwg.vspace.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import de.mpiwg.vspace.diagram.edit.parts.ExhibitionModuleLinkExhibitionModuleTargetEditPart;
import de.mpiwg.vspace.diagram.part.ExhibitionVisualIDRegistry;

/**
 * @generated
 */
public class ExhibitionModuleLinkExhibitionModuleTargetViewFactory extends ConnectionViewFactory {

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
            semanticHint = ExhibitionVisualIDRegistry.getType(ExhibitionModuleLinkExhibitionModuleTargetEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
    }
}
