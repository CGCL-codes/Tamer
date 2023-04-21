package com.nokia.ats4.appmodel.main.swing;

import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.grapheditor.GraphEditor;
import com.nokia.ats4.appmodel.main.event.GraphEditorChangedEvent;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.perspective.DesignPerspective;
import com.nokia.ats4.appmodel.util.TreeDragAndDropHandler;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * JPanel with a tree component for displaying the Kendo models hierarchy.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class ModelTreePanel extends javax.swing.JPanel {

    private MainApplicationModel appModel = null;

    /**
     * Creates new form ModelTreePanel
     */
    public ModelTreePanel() {
        this.appModel = MainApplicationModel.getInstance();
        initComponents();
        DefaultTreeModel model = (DefaultTreeModel) modelTree.getModel();
        model.setRoot(null);
        modelTree.setEditable(false);
        modelTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        modelTree.addMouseListener(new TreeMouseListener());
        modelTree.setCellRenderer(new KendoTreeRenderer());
        ActiveNodeUpdater updater = new ActiveNodeUpdater();
        EventQueue.addListener(updater, GraphEditorChangedEvent.class);
    }

    private void initComponents() {
        treeScrollPane = new javax.swing.JScrollPane();
        modelTree = new javax.swing.JTree();
        setPreferredSize(new java.awt.Dimension(200, 600));
        modelTree.setAutoscrolls(true);
        modelTree.setShowsRootHandles(true);
        modelTree.setToggleClickCount(0);
        treeScrollPane.setViewportView(modelTree);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE));
    }

    private javax.swing.JTree modelTree;

    private javax.swing.JScrollPane treeScrollPane;

    /**
     * Set the KendoModel to tree for displaying the model hierarchy.
     *
     * @param model Root model from where to start
     */
    public void setTreeModel(TreeModel model) {
        this.modelTree.setModel(model);
        this.modelTree.getModel().addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
            }

            public void treeNodesInserted(TreeModelEvent e) {
                setNodesVisible();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                setNodesVisible();
            }

            public void treeStructureChanged(TreeModelEvent e) {
            }

            private void setNodesVisible() {
                ((DefaultTreeModel) modelTree.getModel()).reload();
                for (int i = 0; i < modelTree.getRowCount(); i++) {
                    modelTree.expandRow(i);
                }
            }
        });
    }

    /**
     * Add a mouse events listener to JTree.
     *
     * @param listener MouseListener to add
     */
    public void addTreeListener(MouseListener listener) {
        this.modelTree.addMouseListener(listener);
    }

    /**
     * Add a key listener for the JTree component.
     *
     * @param listener Listenet to add
     */
    public void addTreeKeyListener(KeyListener listener) {
        this.modelTree.addKeyListener(listener);
    }

    public void setTreeDragEnabled(boolean isEnabled) {
        this.modelTree.setDragEnabled(isEnabled);
    }

    public void setTreeTransferHandler(TreeDragAndDropHandler treeDragAndDropHandler) {
        this.modelTree.setTransferHandler(treeDragAndDropHandler);
    }

    /**
     * MouseListener that sets the selection in the tree on right-click of one
     * of the tree nodes.
     */
    private class TreeMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                int row = modelTree.getClosestRowForLocation(e.getX(), e.getY());
                modelTree.setSelectionRow(row);
            }
        }
    }

    /**
     * ActiveNodeUpdater listens for GraphEditorChangedEvents and highlights
     * the currently selected KendoModel (i.e. graph editor) in the model tree.
     */
    private class ActiveNodeUpdater implements KendoEventListener {

        public void processEvent(KendoEvent event) {
            KendoModel activeModel = null;
            GraphEditorChangedEvent evt = (GraphEditorChangedEvent) event;
            if (appModel.getActivePerspective() instanceof DesignPerspective) {
                DesignPerspective dp = (DesignPerspective) appModel.getActivePerspective();
                if (dp != null) {
                    GraphEditor ed = dp.getGraphEditor(evt.getActiveEditorIndex());
                    if (ed != null) {
                        activeModel = ed.getKendoModel();
                        TreeModel treeModel = modelTree.getModel();
                        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
                        if (treeModel != null && root != null) {
                            Enumeration enumeration = root.breadthFirstEnumeration();
                            while (enumeration.hasMoreElements()) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                                if (node.getUserObject() == activeModel) {
                                    modelTree.setSelectionPath(new TreePath(node.getPath()));
                                    modelTree.scrollPathToVisible(modelTree.getSelectionPath());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
