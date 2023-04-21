package net.sf.gwoc.handlers;

import net.sf.gwoc.client.LogHelper;
import net.sf.gwoc.client.RCPHelper;
import net.sf.gwoc.pref.PreferenceInitializer;
import net.sf.gwoc.runner.CacheFolders;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;

public class CacheFoldersAction extends Action {

    private final IViewPart folderView;

    public CacheFoldersAction(IViewPart folderView) {
        this.folderView = folderView;
        setText("Cache Folders");
        setToolTipText("Cache Folders");
    }

    @Override
    public void run() {
        doit(folderView.getViewSite().getWorkbenchWindow());
    }

    protected static void doit(IWorkbenchWindow window) {
        LogHelper.info("caching folders");
        if (PreferenceInitializer.settingsDone()) {
            RCPHelper.runProgressMonitored(window.getShell(), new CacheFolders());
            RCPHelper.executeCommand("net.sf.gwoc.commands.Connect");
        }
    }
}
