package cz.vse.gebz.aplikace.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(true);
        layout.addPerspectiveShortcut(ApplicationWorkbenchAdvisor.PERSPECTIVE_ID);
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, layout.getEditorArea());
        bottom.addView(IPageLayout.ID_OUTLINE);
        IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.RIGHT, 0.5f, "bottom");
        bottomRight.addView(IPageLayout.ID_PROP_SHEET);
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.25f, layout.getEditorArea());
        left.addView(IPageLayout.ID_RES_NAV);
    }
}
