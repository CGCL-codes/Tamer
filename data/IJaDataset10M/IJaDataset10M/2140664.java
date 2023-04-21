package net.sf.avocado_cad.eclipse.ui;

import net.sf.avocado_cad.eclipse.ui.views.MainView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.addView(MainView.MAIN_VIEW_ID, IPageLayout.LEFT, .5f, IPageLayout.ID_EDITOR_AREA);
    }
}
