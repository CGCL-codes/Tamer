package net.sourceforge.squirrel_sql.plugins.favs;

import javax.swing.JPanel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

final class QueryTreeModel extends DefaultTreeModel {

    /** Logger for this class. */
    private static ILogger s_log = LoggerController.createLogger(QueryTreeModel.class);

    private IApplication _app;

    private FoldersCache _cache;

    private JPanel _emptyPnl = new JPanel();

    private MyModelListener _modelListener = new MyModelListener();

    QueryTreeModel(IApplication app, FoldersCache cache) throws IllegalArgumentException {
        super(new FolderNode(new Folder(null, "Root")));
        if (app == null) {
            throw new IllegalArgumentException("Null IApplication passed");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Null FoldersCache passed");
        }
        addTreeModelListener(_modelListener);
        _app = app;
        _cache = cache;
        loadTree();
    }

    private void loadTree() {
        _modelListener.stopListening();
        try {
            Folder rootFolder = _cache.getRootFolder();
            if (rootFolder != null) {
                FolderNode rootNode = new FolderNode(rootFolder);
                setRoot(rootNode);
                loadSubFolders(rootNode);
            } else {
                rootFolder = new Folder(null, "Root");
                setRoot(new FolderNode(rootFolder));
                _cache.setRootFolder(rootFolder);
            }
            reload();
        } finally {
            _modelListener.startListening();
        }
    }

    private void loadSubFolders(FolderNode node) {
        Folder[] subFolders = node.getFolder().getSubFolders();
        for (int i = 0; i < subFolders.length; ++i) {
            FolderNode childNode = new FolderNode(subFolders[i]);
            node.add(childNode);
            loadSubFolders(childNode);
        }
    }

    private static class MyModelListener implements TreeModelListener {

        private boolean _listening = true;

        public void treeStructureChanged(TreeModelEvent evt) {
            s_log.debug("treeStructureChanged");
        }

        public void treeNodesInserted(TreeModelEvent evt) {
            s_log.debug("treeNodesInserted");
        }

        public void treeNodesChanged(TreeModelEvent evt) {
            s_log.debug("treeNodesChanged");
            Object objs[] = evt.getChildren();
            if (objs != null) {
                for (int i = 0; i < objs.length; ++i) {
                    FolderNode node = (FolderNode) objs[i];
                    Folder folder = node.getFolder();
                    String newName = node.toString();
                    String oldName = folder.getName();
                    s_log.debug(oldName);
                    if (!newName.equals(oldName)) {
                        try {
                            folder.setName(newName);
                        } catch (Exception ex) {
                            s_log.error("Error", ex);
                        }
                    }
                }
            }
        }

        public void treeNodesRemoved(TreeModelEvent evt) {
            s_log.debug("treeNodesRemoved");
        }

        void startListening() {
            _listening = true;
        }

        void stopListening() {
            _listening = false;
        }
    }
}
