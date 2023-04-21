package org.plazmaforge.studio.reportdesigner.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.plazmaforge.studio.core.views.FileNavigatorView;
import org.plazmaforge.studio.dbconnector.views.DBAliasView;

/** 
 * @author Oleh Hapon
 * $Id: ReportDesignerPerspective.java,v 1.4 2010/04/28 06:43:12 ohapon Exp $
 */
public class ReportDesignerPerspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(true);
        String editorArea = layout.getEditorArea();
        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.15f, editorArea);
        topLeft.addView(IPageLayout.ID_OUTLINE);
        IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.70f, editorArea);
        right.addView(FileNavigatorView.ID);
        right.addView(DBAliasView.ID);
    }
}
