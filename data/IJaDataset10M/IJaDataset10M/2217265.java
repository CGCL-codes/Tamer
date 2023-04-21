package com.aptana.ide.syncing;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IDecoratorManager;
import com.aptana.ide.core.io.IVirtualFile;
import com.aptana.ide.core.io.VirtualManagerBase;
import com.aptana.ide.core.ui.actions.ActionDelegate;
import com.aptana.ide.core.ui.io.file.LocalFileManager;

/**
 * Uploads an item
 * @author Ingo Muschenetz
 *
 */
public class FileTypeUncloakAction extends ActionDelegate {

    ArrayList selectedFiles;

    /**
	 * @param action 
	 * 
	 */
    public void run(IAction action) {
        if (selectedFiles == null) {
            return;
        }
        for (Iterator iter = selectedFiles.iterator(); iter.hasNext(); ) {
            IVirtualFile element = (IVirtualFile) iter.next();
            String expression = VirtualManagerBase.getFileTypeCloakExpression(element);
            LocalFileManager.removeGlobalSyncCloakExpression(expression);
        }
        IDecoratorManager dm = SyncingPlugin.getDefault().getWorkbench().getDecoratorManager();
        dm.update("com.aptana.ide.syncing.VirtualFileCloakedDecorator");
    }

    /**
	 * @param action 
	 * @param selection 
	 * 
	 */
    public void selectionChanged(IAction action, ISelection selection) {
        action.setEnabled(false);
        selectedFiles = new ArrayList();
        Object[] objects = getValidSelection(selection);
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (!(object instanceof IVirtualFile)) {
                return;
            }
            IVirtualFile f = (IVirtualFile) object;
            if (!f.isCloaked()) {
                return;
            }
            selectedFiles.add(f);
        }
        action.setEnabled(selectedFiles.size() > 0);
    }
}
