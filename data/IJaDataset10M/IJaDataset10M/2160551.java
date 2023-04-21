package net.sf.gwoc.runner;

import java.util.Map;
import net.sf.gwoc.client.RCPHelper;
import net.sf.gwoc.data.Cache;
import net.sf.gwoc.data.FolderData;
import net.sf.gwoc.data.Singleton;
import net.sf.gwoc.pref.PreferenceInitializer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

public class InitCache implements Runnable {

    private final TreeViewer treeViewer;

    private final Shell shell;

    public InitCache(TreeViewer treeViewer, Shell shell) {
        this.treeViewer = treeViewer;
        this.shell = shell;
    }

    @Override
    public void run() {
        if (!PreferenceInitializer.settingsDone()) {
            return;
        }
        int x = 1;
        do {
            Map<String, FolderData> folderTree = Cache.getConnection().getFolderHM();
            if (folderTree.size() > 0) {
                treeViewer.setInput(folderTree);
                break;
            }
            RCPHelper.runProgressMonitored(shell, new CacheFolders());
            Singleton.closeISearcherIfOld();
        } while (x-- > 0);
    }
}
