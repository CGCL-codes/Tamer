package com.nhncorp.cubridqa.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import com.nhncorp.cubridqa.cases.CasesView;
import com.nhncorp.cubridqa.navigation.NavigationView;

/**
 * 
 * The default  perspective of QA tools . 
 * @ClassName: DefaultPerspective
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class DefaultPerspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(false);
        layout.addStandaloneView(NavigationView.ID, true, IPageLayout.LEFT, 0.22f, editorArea);
        IFolderLayout folderLayoutTop = layout.createFolder("right", IPageLayout.TOP, 0.7f, editorArea);
        folderLayoutTop.addView(CasesView.ID);
        layout.getViewLayout(CasesView.ID).setMoveable(true);
        layout.getViewLayout(CasesView.ID).setCloseable(true);
    }
}
