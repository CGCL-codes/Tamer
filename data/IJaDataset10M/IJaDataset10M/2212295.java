package com.hofstetter.diplthesis.ctb.ctb.diagram.view.factories;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.BasicNodeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.TitleStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import com.hofstetter.diplthesis.ctb.ctb.diagram.edit.parts.FunctionalComponentFunctionalComponentCompartementEditPart;
import com.hofstetter.diplthesis.ctb.ctb.diagram.part.CtbVisualIDRegistry;

/**
 * @generated
 */
public class FunctionalComponentFunctionalComponentCompartementViewFactory extends BasicNodeViewFactory {

    /**
	 * @generated
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createSortingStyle());
        styles.add(NotationFactory.eINSTANCE.createFilteringStyle());
        styles.add(NotationFactory.eINSTANCE.createShapeStyle());
        return styles;
    }

    /**
	 * @generated
	 */
    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = CtbVisualIDRegistry.getType(FunctionalComponentFunctionalComponentCompartementEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        setupCompartmentTitle(view);
        setupCompartmentCollapsed(view);
    }

    /**
	 * @generated
	 */
    protected LayoutConstraint createLayoutConstraint() {
        return NotationFactory.eINSTANCE.createBounds();
    }

    /**
	 * @generated
	 */
    protected void initializeFromPreferences(View view) {
        super.initializeFromPreferences(view);
        IPreferenceStore store = (IPreferenceStore) getPreferencesHint().getPreferenceStore();
        org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(store, IPreferenceConstants.PREF_FILL_COLOR);
        ViewUtil.setStructuralFeatureValue(view, NotationPackage.eINSTANCE.getFillStyle_FillColor(), FigureUtilities.RGBToInteger(fillRGB));
    }

    /**
	 * @generated
	 */
    protected void setupCompartmentTitle(View view) {
        TitleStyle titleStyle = (TitleStyle) view.getStyle(NotationPackage.eINSTANCE.getTitleStyle());
        if (titleStyle != null) {
            titleStyle.setShowTitle(true);
        }
    }

    /**
	 * @generated
	 */
    protected void setupCompartmentCollapsed(View view) {
        DrawerStyle drawerStyle = (DrawerStyle) view.getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
        if (drawerStyle != null) {
            drawerStyle.setCollapsed(false);
        }
    }
}
