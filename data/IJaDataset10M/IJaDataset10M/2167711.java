package org.schemeway.plugins.schemescript;

import org.eclipse.ui.*;
import org.schemeway.plugins.schemescript.views.*;

public class SchemePerspective implements IPerspectiveFactory {

    public SchemePerspective() {
        super();
    }

    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        defineLayout(layout);
    }

    private void defineLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.20f, editorArea);
        topLeft.addView(IPageLayout.ID_RES_NAV);
        IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f, "topLeft");
        bottomLeft.addView(IPageLayout.ID_OUTLINE);
        layout.addFastView(DefinitionListView.DEFINITION_LIST_ID);
        layout.addFastView("org.eclipse.ui.console.ConsoleView");
    }

    private void defineActions(IPageLayout layout) {
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    }
}
