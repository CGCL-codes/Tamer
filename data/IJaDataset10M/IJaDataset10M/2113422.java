package fr.insa.rennes.pelias.pcreator;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import fr.insa.rennes.pelias.pcreator.views.ChainNavigator;
import fr.insa.rennes.pelias.pcreator.views.ServiceNavigator;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        layout.setFixed(true);
        layout.setEditorAreaVisible(true);
        layout.addView(ChainNavigator.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
        layout.addView(ServiceNavigator.ID, IPageLayout.BOTTOM, 0.3f, ChainNavigator.ID);
        layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
        layout.setEditorAreaVisible(false);
    }
}
