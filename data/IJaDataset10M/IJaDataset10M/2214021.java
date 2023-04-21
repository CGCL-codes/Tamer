package com.jiexplorer.rcp.transferable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.map.MultiValueMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import com.jiexplorer.rcp.ccp.PasteService;
import com.jiexplorer.rcp.model.FolderNode;
import com.jiexplorer.rcp.model.IFileNode;
import com.jiexplorer.rcp.model.ITreeNode;
import com.jiexplorer.rcp.model.IFileNodeFactory;
import com.jiexplorer.rcp.ui.dialog.BaseDialog;
import com.jiexplorer.rcp.util.Messages;
import com.jiexplorer.rcp.util.Utilities;

public class FolderTransferableDestination implements ITransferableDestination {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FolderTransferableDestination.class);

    private final FolderNode parentFolder;

    public FolderTransferableDestination(final FolderNode parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void paste(final Object[] source, final boolean opperation, final IProgressMonitor monitor) {
        final int cnt = Utilities.countFiles((String[]) source);
        monitor.beginTask("Paste Action", cnt);
        final List<String> removeList = new ArrayList<String>();
        final List<ITreeNode> updateList = new ArrayList<ITreeNode>();
        boolean replaceAll = false;
        boolean replaceNone = false;
        for (int i = 0; i < source.length; i++) {
            if (monitor.isCanceled()) {
                return;
            }
            final File srcFile = new File((String) source[i]);
            if (srcFile.exists()) {
                try {
                    final File destFile = new File(parentFolder.getFile(), srcFile.getName());
                    if (destFile.exists()) {
                        if (!replaceNone) {
                            boolean replace_once = false;
                            if (!replaceAll) {
                                final int replace_value = BaseDialog.openReplace(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.dialog_Replace_title, NLS.bind(Messages.dialog_Replace_description, new String[] { srcFile.getName(), String.valueOf(srcFile.length()), destFile.getName(), String.valueOf(destFile.length()) }));
                                switch(replace_value) {
                                    case 0:
                                        replace_once = true;
                                        break;
                                    case 1:
                                        replaceAll = true;
                                        break;
                                    case 2:
                                        replace_once = false;
                                        break;
                                    case 3:
                                        replaceNone = true;
                                        break;
                                }
                            }
                            if ((replaceAll && !replaceNone) || replace_once) {
                                IFileNode replaceNode = null;
                                final Iterator it = parentFolder.getChildren().iterator();
                                while (it.hasNext()) {
                                    final IFileNode node = (IFileNode) it.next();
                                    if (node.getName().equals(destFile.getName())) {
                                        replaceNode = node;
                                    }
                                }
                                if (replaceNode != null) {
                                    parentFolder.removeChild(replaceNode);
                                    Utilities.copy(srcFile, destFile, true, monitor);
                                    final ITreeNode newNode = IFileNodeFactory.getInstance().makeNode(parentFolder, destFile);
                                    updateList.add(newNode);
                                    log.debug("replaceNode " + newNode.getName());
                                    removeList.add(srcFile.getAbsolutePath());
                                }
                            }
                        }
                    } else {
                        Utilities.copy(srcFile, destFile, true, monitor);
                        final ITreeNode newNode = IFileNodeFactory.getInstance().makeNode(parentFolder, destFile);
                        updateList.add(newNode);
                        log.debug("addNode " + newNode.getName());
                        removeList.add(srcFile.getAbsolutePath());
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            monitor.worked(10);
        }
        final ITreeNode[] unodes = new ITreeNode[updateList.size()];
        updateList.toArray(unodes);
        log.debug("Update Nodes ");
        parentFolder.addChildren(unodes);
        if (PasteService.getInstance().isCutOperation() && PasteService.getInstance().getCutData() != null) {
            final Object objs = PasteService.getInstance().getCutData();
            boolean cutValue = true;
            if (objs instanceof ITreeNode[] && ((ITreeNode[]) objs).length > 0) {
                final List<String> pasteData = new ArrayList<String>();
                for (final ITreeNode node : ((ITreeNode[]) objs)) {
                    if (node instanceof IFileNode) {
                        pasteData.add(((IFileNode) node).getAbsolutePath());
                    }
                }
                for (final Object pobj : source) {
                    if (!pasteData.contains(pobj)) {
                        cutValue = false;
                    }
                }
            } else {
                cutValue = false;
            }
            if (cutValue) {
                final MultiValueMap map = new MultiValueMap();
                for (final ITreeNode node : ((ITreeNode[]) objs)) {
                    if (node instanceof IFileNode && removeList.contains(((IFileNode) node).getAbsolutePath())) {
                        final IFileNode fnode = ((IFileNode) node);
                        if (fnode.getParent() != null && !fnode.isDirectory()) {
                            fnode.deleteDeep(monitor, false);
                            map.put(fnode.getParent(), fnode);
                        } else {
                            fnode.deleteDeep(monitor);
                        }
                    }
                }
                for (final Object obj : map.keySet()) {
                    if (obj != null && obj instanceof ITreeNode) {
                        final Collection c = map.getCollection(obj);
                        ITreeNode[] nodes = new ITreeNode[c.size()];
                        c.toArray(nodes);
                        ((ITreeNode) obj).removeChildren(nodes);
                    }
                }
            }
        }
        monitor.done();
        PasteService.getInstance().setCutOperation(false);
    }

    public void dispose() {
    }

    public String getName() {
        return parentFolder.getName();
    }

    @Override
    public String toString() {
        return parentFolder.getName();
    }
}
