package de.mywines.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import de.mywines.ui.views.FormsView;
import de.mywines.ui.views.TreeView;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addView(TreeView.ID, IPageLayout.LEFT, 0.4f, editorArea);
        layout.addView(FormsView.ID, IPageLayout.RIGHT, 0.4f, TreeView.ID);
        layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.7f, FormsView.ID);
    }
}
