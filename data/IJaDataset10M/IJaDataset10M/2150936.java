package org.digitall.apps.filemanager;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.digitall.common.filemanager.ExpandTreePathThread;
import org.digitall.common.filemanager.RemoteFile;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicScrollPane;

/**
 * Example which displays the directory structure of the system
 * in a JTree coponent. The nodes are expandable, and will list
 * the sub directories of the selected node.
 *
 * @author Havard Rast Blok
 *
 */
public class LocalFileTreePanel extends BasicContainerPanel implements DropTargetListener {

    /** The model which holds the nodes */
    protected TreeModel model;

    /** The tree GUI */
    protected JTree tree;

    private GridLayout gridLayout = new GridLayout(1, 2);

    private String currentDirectory = "";

    private LocalFileList jlFileList = new LocalFileList();

    private Object dragSource;

    private FileManTransfersPanel fileMan;

    private TreePath dropPath = null;

    private TreePath prevDropPath = null;

    private BasicScrollPane scrollFileList;

    private BasicScrollPane scrollTree;

    private ExpandTreePathThread expandThread;

    /**
     * Constructs the jpCenter which holds a JTree with the file structure.
     */
    public LocalFileTreePanel(FileManTransfersPanel _fileMan) {
        try {
            fileMan = _fileMan;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setLayout(gridLayout);
        this.setSize(new Dimension(542, 403));
        MutableTreeNode root = new DefaultMutableTreeNode("Local Folders");
        File roots[] = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            File f = roots[i];
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getAbsoluteFile().toString());
            root.insert(node, i);
        }
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.addTreeSelectionListener(new MyTreeSelectionListener());
        scrollTree = new BasicScrollPane(tree);
        add(scrollTree);
        scrollFileList = new BasicScrollPane(jlFileList);
        add(scrollFileList);
        DropTarget target = new DropTarget(tree, this);
        jlFileList.setDragEnabled(true);
        jlFileList.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                fileMan.setDragSource(jlFileList);
            }
        });
        tree.setAutoscrolls(true);
    }

    /**
     * Adds the sub directories, if any, of the given path to the given node.
     *
     * @param node tree node to add sub directory nodes to
     * @param path file system path to search for sub directories
     */
    protected void addChildren(MutableTreeNode node, String path) {
        DirectoryFileFilter filter = new DirectoryFileFilter();
        File dir = new File(path);
        File subDirs[] = dir.listFiles(filter);
        if (subDirs != null) {
            Arrays.sort(subDirs);
            for (int i = 0; i < subDirs.length; i++) {
                File f = subDirs[i];
                MutableTreeNode child = new DefaultMutableTreeNode(f.getName());
                node.insert(child, i);
            }
        }
    }

    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
    }

    private TreePath getNodeForEvent(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        return tree.getClosestPathForLocation(p.x, p.y);
    }

    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        if (dragSource == null) {
            dropTargetDragEvent.rejectDrag();
        } else {
            dropPath = getNodeForEvent(dropTargetDragEvent);
            if (dropPath != prevDropPath) {
                expandThread = new ExpandTreePathThread(tree);
                expandThread.setPath(dropPath);
                expandThread.start();
                tree.setSelectionPath(dropPath);
                prevDropPath = dropPath;
            }
            dropTargetDragEvent.acceptDrag(dropTargetDragEvent.getDropAction());
        }
    }

    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
    }

    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        Point pt = dropTargetDropEvent.getLocation();
        DropTargetContext dtc = dropTargetDropEvent.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath.getLastPathComponent();
        try {
            Transferable tr = dropTargetDropEvent.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (tr.isDataFlavorSupported(flavors[i])) {
                    if (RemoteFileList.class.isInstance(dragSource)) {
                        dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
                        RemoteFileList fileList = (RemoteFileList) dragSource;
                        RemoteFile[] selectedFiles = fileList.getSelectedFiles();
                        for (int j = 0; j < selectedFiles.length; j++) {
                            Object[] nodes = dropPath.getPath();
                            String dir = "";
                            for (int k = 1; k < nodes.length; k++) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes[k];
                                dir += (String) node.getUserObject();
                                dir += File.separator;
                            }
                            dir = dir.replaceAll("//", "/");
                            String destination = dir;
                            selectedFiles[j].setDestinationDir(destination);
                            boolean canContinue = true;
                            File outputFile = new File(selectedFiles[j].getDestinationDir() + selectedFiles[j].getName());
                            if (outputFile.exists()) {
                                if (!Advisor.question("Archivo existente", "El archivo " + selectedFiles[j].getName() + " ya existe,\n�desea sobreescribirlo?")) {
                                    canContinue = false;
                                }
                            }
                            if (canContinue) {
                                System.out.println("Downloading file: " + selectedFiles[j].getDestinationDir() + selectedFiles[j].getName());
                                fileMan.getFile(selectedFiles[j]);
                            }
                        }
                        dropTargetDropEvent.dropComplete(true);
                        return;
                    }
                }
            }
            dropTargetDropEvent.rejectDrop();
        } catch (Exception e) {
            e.printStackTrace();
            dropTargetDropEvent.rejectDrop();
        }
    }

    public void dragExit(DropTargetEvent dropTargetEvent) {
        dropPath = null;
        if (expandThread != null) {
            expandThread.setPath(dropPath);
        }
    }

    public void setDragSource(RemoteFileList _remoteFileList) {
        dragSource = _remoteFileList;
    }

    /**
     * Inner class which listens for selection events on the tree.
     * When one is received, it will get the path of the selected
     * node and add sub directory nodes, if any, to it.
     */
    class MyTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getNewLeadSelectionPath();
            if (path == null) {
                return;
            }
            DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object nodes[] = path.getPath();
            String dir = "";
            for (int i = 1; i < nodes.length; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes[i];
                dir += (String) node.getUserObject();
                dir += File.separator;
            }
            addChildren(curNode, dir);
            setCurrentDirectory(dir.replaceAll("//", "/"));
            repaint();
        }

        private void setCurrentDirectory(String _currentDirectory) {
            currentDirectory = _currentDirectory;
            jlFileList.setCurrentDirectory(currentDirectory);
        }
    }
}
