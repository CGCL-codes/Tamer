package org.bpt.processtracker2;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class decides how the views should be organised.
 * Boby Thomas Pazheparampil
 * May 2007
 */
public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
        layout.addStandaloneView(MemoryView.ID, false, IPageLayout.TOP, 0.20f, editorArea);
        layout.addStandaloneView(View.ID, false, IPageLayout.BOTTOM, 0.80f, editorArea);
        layout.getViewLayout(NavigationView.ID).setCloseable(false);
    }
}
