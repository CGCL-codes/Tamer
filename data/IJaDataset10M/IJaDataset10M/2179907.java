package hub.sam.mof.simulator.editor.diagram.view.factories;

import hub.sam.mof.simulator.editor.diagram.part.M3ActionsVisualIDRegistry;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.TitleStyle;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class MClassMOperationsViewFactory extends BasicNodeViewFactory {

    /**
	 * @generated
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(org.eclipse.gmf.runtime.notation.NotationFactory.eINSTANCE.createDrawerStyle());
        styles.add(org.eclipse.gmf.runtime.notation.NotationFactory.eINSTANCE.createSortingStyle());
        styles.add(org.eclipse.gmf.runtime.notation.NotationFactory.eINSTANCE.createFilteringStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = M3ActionsVisualIDRegistry.getType(hub.sam.mof.simulator.editor.diagram.edit.parts.MClassMOperationsEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        setupCompartmentTitle(view);
        setupCompartmentCollapsed(view);
    }

    /**
	 * @generated
	 */
    protected void setupCompartmentTitle(View view) {
        TitleStyle titleStyle = (TitleStyle) view.getStyle(org.eclipse.gmf.runtime.notation.NotationPackage.eINSTANCE.getTitleStyle());
        if (titleStyle != null) {
            titleStyle.setShowTitle(true);
        }
    }

    /**
	 * @generated
	 */
    protected void setupCompartmentCollapsed(View view) {
        DrawerStyle drawerStyle = (DrawerStyle) view.getStyle(org.eclipse.gmf.runtime.notation.NotationPackage.eINSTANCE.getDrawerStyle());
        if (drawerStyle != null) {
            drawerStyle.setCollapsed(false);
        }
    }
}
