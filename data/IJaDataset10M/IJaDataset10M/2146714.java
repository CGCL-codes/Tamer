package org.formaria.editor.eclipse;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * A perspective for AriaEditor page editor
 * <p>
 * Copyright (c) Formaria Ltd., 2008
 * </p>
 * $Revision: 1.2 $ License: see license.txt
 */
public class AriaEditorPerspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.20f, editorArea);
        topLeft.addView(IPageLayout.ID_RES_NAV);
        topLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);
        IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f, "topLeft");
        bottomLeft.addView("ARIA_EDITOR_INSPECTOR");
        IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.75f, editorArea);
        topRight.addView("ARIA_EDITOR_PALETTE");
        IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.50f, "topRight");
        bottomRight.addView("ARIA_EDITOR_STYLES");
        bottomRight.addView("ARIA_EDITOR_PROPERTIES");
    }
}
