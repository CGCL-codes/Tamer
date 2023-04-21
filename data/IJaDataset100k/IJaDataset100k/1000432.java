package com.migniot.streamy.application;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import com.migniot.streamy.application.view.DownloadView;
import com.migniot.streamy.application.view.ExplorerView;
import com.migniot.streamy.application.view.IncomingView;
import com.migniot.streamy.application.view.StartupView;
import com.migniot.streamy.browser.view.BrowserView;

/**
 * The default perspective.
 */
public class Perspective implements IPerspectiveFactory {

    /**
	 * {@inheritDoc}
	 */
    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        IFolderLayout main = layout.createFolder("com.migniot.streamy.application.MainFolder", IPageLayout.LEFT, 0.85f, IPageLayout.ID_EDITOR_AREA);
        main.addView(StartupView.VIEW_ID);
        layout.getViewLayout(StartupView.VIEW_ID).setCloseable(false);
        main.addView(ExplorerView.VIEW_ID);
        main.addPlaceholder(BrowserView.VIEW_ID + ":*");
        IFolderLayout right = layout.createFolder("com.migniot.streamy.application.RightFolder", IPageLayout.RIGHT, 0.15f, IPageLayout.ID_EDITOR_AREA);
        right.addView(DownloadView.VIEW_ID);
        layout.getViewLayout(DownloadView.VIEW_ID).setCloseable(false);
        IFolderLayout rightDown = layout.createFolder("com.migniot.streamy.application.RightDownFolder", IPageLayout.BOTTOM, 0.75f, "com.migniot.streamy.application.RightFolder");
        rightDown.addView(IncomingView.VIEW_ID);
        layout.getViewLayout(IncomingView.VIEW_ID).setCloseable(false);
    }
}
