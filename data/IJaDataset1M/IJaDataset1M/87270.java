package org.wsmostudio.ui.perspective;

import org.eclipse.ui.*;

public class OntologyPerspectiveFactory implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String edName = layout.getEditorArea();
        IFolderLayout naviFolder = layout.createFolder("naviFolder", IPageLayout.LEFT, 0.3f, edName);
        naviFolder.addView(IPageLayout.ID_RES_NAV);
        IFolderLayout outlineFolder = layout.createFolder("outlineFolder", IPageLayout.BOTTOM, 0.5f, "naviFolder");
        outlineFolder.addView("org.wsmostudio.ui.views.OntologyView");
        outlineFolder.addPlaceholder("org.wsmostudio.ui.views.WSMOView");
        IFolderLayout propsFolder = layout.createFolder("propsFolder", IPageLayout.BOTTOM, 0.8f, edName);
        propsFolder.addView(IPageLayout.ID_PROP_SHEET);
    }

    public boolean isFixed() {
        return false;
    }
}
