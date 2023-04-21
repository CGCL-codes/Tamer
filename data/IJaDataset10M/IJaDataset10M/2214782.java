package com.jiexplorer.rcp.model;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import com.jiexplorer.rcp.JIExplorerPlugin;
import com.jiexplorer.rcp.config.ApplicationConfig;
import com.jiexplorer.rcp.model.thumbnail.IThumbnailItem;
import com.jiexplorer.rcp.srv.ImageSrv;
import com.jiexplorer.rcp.ui.IInputContentProvider;
import com.jiexplorer.rcp.ui.INodeChangedListener;
import com.jiexplorer.rcp.util.FileSortbyDate;
import com.jiexplorer.rcp.util.FileSortbyName;
import com.jiexplorer.rcp.util.FileSortbyPath;
import com.jiexplorer.rcp.util.FileSortbySize;
import com.jiexplorer.rcp.util.FileSortbyType;
import com.jiexplorer.rcp.util.Messages;
import com.jiexplorer.rcp.util.OrderedImageList;
import com.jiexplorer.rcp.util.OrderedList;

public class FolderNode extends AbstractResourceNode implements IFolderNode, IInputContentProvider {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FolderNode.class);

    protected boolean root = false;

    protected boolean isSystemRoot = false;

    @Override
    public void update(final File file) {
        uri = file.toURI();
        length = file.length();
        lastModified = file.lastModified();
        isDir = file.isDirectory();
        exists = file.exists();
        absolutePath = file.getAbsolutePath();
        if (file.getParent() == null) {
            isSystemRoot = true;
        }
        try {
            if (!file.getName().equals("hiberfil.sys") && file.exists()) {
                type = FileSystemView.getFileSystemView().getSystemTypeDescription(file);
                displayName = FileSystemView.getFileSystemView().getSystemDisplayName(file);
            }
        } catch (final Exception e) {
            type = "";
            displayName = "";
        }
        name = file.getName().length() > 0 ? file.getName() : file.toURI().getPath();
        if (osName.startsWith("win") && name.indexOf('/') == 0) {
            name = name.substring(1);
        }
        if (parent != null) {
            parent.fireNodesUpdated(new ITreeNode[] { this });
        }
    }

    public void addNode(final IFileNode node) {
        addChild(node);
    }

    public void rename(final File file) {
        getFile().renameTo(file);
        update(file);
    }

    public IFileNode contains(final IFileNode node) {
        for (final ITreeNode fileNode : getChildren()) {
            if (fileNode instanceof IFileNode && ((IFileNode) fileNode).getAbsolutePath().equals(node.getAbsolutePath())) {
                return (IFileNode) fileNode;
            }
        }
        return null;
    }

    public boolean contains(final File file) {
        for (final ITreeNode fileNode : getChildren()) {
            if (fileNode instanceof IFileNode && ((IFileNode) fileNode).getAbsolutePath().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    public void deleteDeep(final IProgressMonitor m) {
        deleteDeep(m, true);
    }

    public void deleteDeep(final IProgressMonitor m, final boolean update) {
        if (update) {
            parent.removeChild(this);
        }
        if (explored) {
            if (getChildren() != null) {
                final IFileNode[] delete_nodes = new IFileNode[getChildren().size()];
                getChildren().toArray(delete_nodes);
                for (final IFileNode file : delete_nodes) {
                    if (file.isFile()) {
                        file.deleteDeep(m);
                    }
                }
                for (final IFileNode file : delete_nodes) {
                    if (file.isDirectory()) {
                        file.deleteDeep(m);
                    }
                }
            }
        }
        final File folder = getFile();
        final File[] childFiles = folder.listFiles();
        for (int i = 0; childFiles != null && i < childFiles.length; i++) {
            deleteDeep(childFiles[i]);
        }
        if (m != null) {
            m.subTask(this.getAbsolutePath());
        }
        deleteFile(folder);
        if (m != null) {
            m.worked(10);
        }
    }

    private void deleteDeep(final File file) {
        if (file.isDirectory()) {
            final File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (final File f : childFiles) {
                    deleteDeep(f);
                }
            }
        }
        deleteFile(file);
    }

    private static final void deleteFile(final File file) {
        deleteFile(0, file);
    }

    private static final void deleteFile(int count, final File file) {
        if (file.exists()) {
            if (count > 4) {
                file.deleteOnExit();
            } else if (!file.delete()) {
                System.gc();
                if (!file.delete()) {
                    try {
                        Thread.sleep(100);
                    } catch (final InterruptedException e) {
                    }
                    deleteFile(++count, file);
                }
            }
        }
    }

    public List<IFileNode> getChildrenFolders(final INodeChangedListener listener, final IProgressMonitor job) {
        createChildrenFolders(listener, job);
        final OrderedList<IFileNode> list = new OrderedList<IFileNode>();
        for (final ITreeNode node : getChildren()) {
            if (node instanceof FolderNode) {
                list.add((IFileNode) node);
            }
        }
        list.sort();
        return list;
    }

    public OrderedImageList<IImageNode> getImageList() {
        final OrderedImageList<IImageNode> results = new OrderedImageList<IImageNode>();
        if (children != null) {
            for (final ITreeNode node : children) {
                if (node instanceof IImageNode) {
                    results.add(node);
                }
            }
        }
        return results;
    }

    @Override
    protected void createChildren() {
        createChildren(null, new NullProgressMonitor());
    }

    protected void createChildrenFolders(final INodeChangedListener listener, final IProgressMonitor job) {
        if (!explored) {
            children = new OrderedList<ITreeNode>();
            final File folder = getFile();
            final File[] childFiles = folder.listFiles();
            if (job != null && job.isCanceled()) {
                log.debug("createChildrenFolders - IProgressMonitor Canceled");
                return;
            }
            job.beginTask("", childFiles.length);
            switch(JIExplorerPlugin.getDefault().getPluginPreferences().getInt(ApplicationConfig.SORT_ORDER)) {
                case 4:
                    Arrays.sort(childFiles, new FileSortbyPath<File>());
                    break;
                case 3:
                    Arrays.sort(childFiles, new FileSortbyDate<File>());
                    break;
                case 2:
                    Arrays.sort(childFiles, new FileSortbySize<File>());
                    break;
                case 1:
                    Arrays.sort(childFiles, new FileSortbyType<File>());
                    break;
                case 0:
                default:
                    Arrays.sort(childFiles, new FileSortbyName<File>());
            }
            if (job != null && job.isCanceled()) {
                log.debug("createChildrenFolders - IProgressMonitor Canceled");
                return;
            }
            for (final File child : childFiles) {
                if (job != null && job.isCanceled()) {
                    log.debug("createChildrenFolders - IProgressMonitor Canceled");
                    return;
                }
                if (child.isDirectory()) {
                    final IFileNode fnode = IFileNodeFactory.getInstance().makeNode(FolderNode.this, child);
                    addChild(fnode);
                    if (listener != null) {
                        fnode.addNodeChangeListener(listener);
                        log.debug("createChildrenFolders - added - " + fnode.getName());
                        listener.onAdd(FolderNode.this, new ITreeNode[] { fnode });
                    }
                }
                job.worked(1);
            }
            for (final File child : childFiles) {
                if (job != null && job.isCanceled()) {
                    log.debug("createChildrenFolders - IProgressMonitor Canceled");
                    return;
                }
                if (!child.isDirectory()) {
                    final IFileNode fnode = IFileNodeFactory.getInstance().makeNode(FolderNode.this, child);
                    addChild(fnode);
                    if (listener != null) {
                        fnode.addNodeChangeListener(listener);
                        log.debug("createChildrenFolders - added - " + fnode.getName());
                        listener.onAdd(FolderNode.this, new ITreeNode[] { fnode });
                    }
                }
            }
            ((OrderedList) children).sort();
            explored = true;
        } else if (listener != null) {
            final ITreeNode[] tmp = new ITreeNode[children.size()];
            listener.onAdd(FolderNode.this, children.toArray(tmp));
        }
    }

    protected void createChildren(final INodeChangedListener listener, final IProgressMonitor pm) {
        if (!explored) {
            explored = true;
            children = new OrderedList();
            final File folder = getFile();
            final File[] childFiles = folder.listFiles();
            pm.beginTask("", childFiles.length);
            switch(JIExplorerPlugin.getDefault().getPluginPreferences().getInt(ApplicationConfig.SORT_ORDER)) {
                case 4:
                    Arrays.sort(childFiles, new FileSortbyPath<File>());
                    break;
                case 3:
                    Arrays.sort(childFiles, new FileSortbyDate<File>());
                    break;
                case 2:
                    Arrays.sort(childFiles, new FileSortbySize<File>());
                    break;
                case 1:
                    Arrays.sort(childFiles, new FileSortbyType<File>());
                    break;
                case 0:
                default:
                    Arrays.sort(childFiles, new FileSortbyName<File>());
            }
            if (pm.isCanceled()) {
                log.debug("createChildrenFolders - IProgressMonitor Canceled");
                return;
            }
            for (final File child : childFiles) {
                if (pm.isCanceled()) {
                    log.debug("createChildrenFolders - IProgressMonitor Canceled");
                    return;
                }
                if (child.isDirectory()) {
                    final IFileNode fnode = IFileNodeFactory.getInstance().makeNode(FolderNode.this, child);
                    addChild(fnode);
                    pm.worked(1);
                    if (listener != null) {
                        fnode.addNodeChangeListener(listener);
                        log.debug("createChildrenFolders - added - " + fnode.getName());
                        listener.onAdd(FolderNode.this, new ITreeNode[] { fnode });
                    }
                }
            }
            for (final File child : childFiles) {
                if (pm.isCanceled()) {
                    log.debug("createChildrenFolders - IProgressMonitor Canceled");
                    return;
                }
                if (!child.isDirectory()) {
                    final IFileNode fnode = IFileNodeFactory.getInstance().makeNode(FolderNode.this, child);
                    addChild(fnode);
                    pm.worked(1);
                    if (listener != null) {
                        fnode.addNodeChangeListener(listener);
                        log.debug("createChildrenFolders - added - " + fnode.getName());
                        listener.onAdd(FolderNode.this, new ITreeNode[] { fnode });
                    }
                }
            }
            ((OrderedList) children).sort();
            explored = true;
        } else if (listener != null) {
            final ITreeNode[] tmp = new ITreeNode[children.size()];
            listener.onAdd(FolderNode.this, children.toArray(tmp));
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public boolean isRoot() {
        return isSystemRoot;
    }

    public void setRoot(final boolean root) {
        this.isSystemRoot = root;
    }

    @Override
    public void fireNodeExpand() {
        final Iterator<INodeChangedListener> listenerIter = modelChangeListeners.iterator();
        final FolderNode folder = FolderNode.this.clone();
        SafeRunnable.run(new ISafeRunnable() {

            public void handleException(final Throwable exception) {
                exception.printStackTrace();
            }

            public void run() throws Exception {
                while (listenerIter.hasNext()) {
                    listenerIter.next().onExpand(folder);
                }
            }
        });
    }

    public void fireTreeNodeCollapse() {
        final Iterator<INodeChangedListener> listenerIter = modelChangeListeners.iterator();
        final FolderNode folder = FolderNode.this.clone();
        SafeRunnable.run(new ISafeRunnable() {

            public void handleException(final Throwable exception) {
                exception.printStackTrace();
            }

            public void run() throws Exception {
                while (listenerIter.hasNext()) {
                    listenerIter.next().onCollapse(folder);
                }
            }
        });
    }

    public void setSortColumn(final int i) {
        ((OrderedList) children).sort(i);
    }

    public Object[] getChildren(final int colCnt) {
        final long rowsCnt = Math.round(((float) getChildren().size() / (float) colCnt) + .5);
        final Object[] rows = new Object[(int) rowsCnt];
        for (int i = 0; i < rowsCnt; i++) {
            final Object[] row = new Object[colCnt];
            for (int ecnt = 0; ecnt < colCnt; ecnt++) {
                if ((i * colCnt) + ecnt < getChildren().size()) {
                    row[ecnt] = getChildren().get((i * colCnt) + ecnt);
                }
            }
            rows[i] = row;
        }
        return rows;
    }

    @Override
    public boolean hasChildren() {
        return getObjectCnt() > 0;
    }

    public String getCRC32() {
        return null;
    }

    public Image getThumbnail(final IThumbnailItem item) {
        if (isSystemRoot) {
            return ImageSrv.getInstance().getImage(ImageSrv.IMAGE_DRIVE);
        } else {
            return ImageSrv.getInstance().getImage(ImageSrv.IMAGE_FOLDER);
        }
    }

    public Object[] getElements(final INodeChangedListener changeListener, final IProgressMonitor pm) {
        return getChildren(changeListener, pm).toArray();
    }

    public Object[] getElements(final INodeChangedListener changeListener, final Object[] source) {
        final OrderedList l = new OrderedList();
        if (source != null) {
            for (final Object obj : source) {
                l.addAll(((IFolderNode) obj).getChildren(changeListener, null));
            }
            return l.toArray();
        }
        return new Object[0];
    }

    public List<IImageNode> getImageList(final Object[] source) {
        final OrderedImageList l = new OrderedImageList();
        if (source != null) {
            for (final Object obj : source) {
                l.addAll(((FolderNode) obj).getImageList());
            }
            return l;
        }
        return new OrderedImageList(0);
    }

    public int getObjectCnt() {
        if (!explored) {
            final File f = getFile();
            if (f == null) {
                return 0;
            }
            final String[] childFiles = getFile().list();
            if (childFiles == null) {
                return 0;
            }
            return childFiles.length;
        } else {
            return getChildren().size();
        }
    }

    public String getObjectCntStr() {
        return NLS.bind(Messages.object_cnt, new Object[] { new Long(this.getObjectCnt()) });
    }

    public String getStatusString() {
        return NLS.bind(Messages.provider_status, new Object[] { getType(), getName(), new Integer(size()) });
    }

    public String getType() {
        return Messages.folder_type;
    }

    public int size() {
        return getObjectCnt();
    }

    @Override
    public List<ITreeNode> getChildren(final INodeChangedListener changeListener, final IProgressMonitor pm) {
        if (children != null && children.size() > 0) {
            ITreeNode[] a = new ITreeNode[children.size()];
            changeListener.onAdd(this, children.toArray(a));
            return children;
        }
        createChildren(changeListener, pm);
        return children;
    }

    @Override
    public Object getAdapter(final Class adapter) {
        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    @Override
    public FolderNode clone() {
        try {
            final FolderNode cloneNode = (FolderNode) Class.forName(getClass().getName()).newInstance();
            cloneNode.setParent(getParent());
            cloneNode.setUid(getUid());
            cloneNode.setName(getName());
            for (final INodeChangedListener c : this.modelChangeListeners) {
                cloneNode.addNodeChangeListener(c);
            }
            cloneNode.update(getFile());
            return cloneNode;
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
