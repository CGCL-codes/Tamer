package bee.editor;

import bee.core.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author boto
 */
public class PaneEntities extends EditorPane {

    /**
     * Create the entity panel.
     */
    public PaneEntities(EditorData ed) {
        super(ed);
        initComponents();
        setupComponents();
    }

    private final String POPUP_CMD_ADD_GROUP = "AddGroup";

    private final String POPUP_CMD_REMOVE_GROUP = "RemoveGroup";

    private DefaultMutableTreeNode copyNode;

    private DefaultMutableTreeNode cutNode;

    private Vec2dInt undoPosition;

    private Vec2dInt nodeClickOffset = new Vec2dInt();

    private javax.swing.JButton jButtonEntityAdd;

    private javax.swing.JButton jButtonEntityCopy;

    private javax.swing.JButton jButtonEntityCut;

    private javax.swing.JButton jButtonEntityPaste;

    private javax.swing.JButton jButtonEntityRemove;

    private javax.swing.JComboBox jComboBoxEntityTypes;

    private javax.swing.JTree jTreeEntities;

    private javax.swing.JPanel jPaneEntities;

    private javax.swing.JPanel jPanelEntityEdit;

    private javax.swing.JPanel jPanelEntityListParams;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTable jTableEntityParams;

    private void initComponents() {
        jButtonEntityAdd = new JButton();
        jButtonEntityCopy = new JButton();
        jButtonEntityCut = new JButton();
        jButtonEntityPaste = new JButton();
        jButtonEntityRemove = new JButton();
        jComboBoxEntityTypes = new JComboBox();
        jTreeEntities = new JTree();
        jPaneEntities = new JPanel();
        jPanelEntityEdit = new JPanel();
        jPanelEntityListParams = new JPanel();
        jScrollPane1 = new JScrollPane();
        jScrollPane2 = new JScrollPane();
        jTableEntityParams = new JTable();
        setBackground(new java.awt.Color(255, 255, 255));
        jPanelEntityListParams.setBackground(new java.awt.Color(255, 255, 255));
        jPaneEntities.setBackground(new java.awt.Color(255, 255, 255));
        jPanelEntityEdit.setBackground(new java.awt.Color(255, 255, 255));
        jPanelEntityEdit.setAlignmentX(0.0F);
        javax.swing.GroupLayout jPaneEntitiesLayout = new javax.swing.GroupLayout(this);
        setLayout(jPaneEntitiesLayout);
        jComboBoxEntityTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        jButtonEntityAdd.setText("Add");
        jButtonEntityAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntityAddActionPerformed(evt);
            }
        });
        jButtonEntityCopy.setText("Copy");
        jButtonEntityCopy.setMnemonic(java.awt.event.KeyEvent.VK_C);
        jButtonEntityCopy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntityCopyActionPerformed(evt);
            }
        });
        jButtonEntityCut.setText("Cut");
        jButtonEntityCut.setMnemonic(java.awt.event.KeyEvent.VK_X);
        jButtonEntityCut.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntityCutActionPerformed(evt);
            }
        });
        jButtonEntityPaste.setText("Paste");
        jButtonEntityPaste.setMnemonic(java.awt.event.KeyEvent.VK_V);
        jButtonEntityPaste.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntityPasteActionPerformed(evt);
            }
        });
        jButtonEntityRemove.setText("Remove");
        jButtonEntityRemove.setMnemonic(java.awt.event.KeyEvent.VK_D);
        jButtonEntityRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntityRemoveActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanelEntityEditLayout = new javax.swing.GroupLayout(jPanelEntityEdit);
        jPanelEntityEdit.setLayout(jPanelEntityEditLayout);
        jPanelEntityEditLayout.setHorizontalGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelEntityEditLayout.createSequentialGroup().addContainerGap().addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanelEntityEditLayout.createSequentialGroup().addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButtonEntityPaste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButtonEntityCopy, javax.swing.GroupLayout.DEFAULT_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButtonEntityRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButtonEntityCut, javax.swing.GroupLayout.DEFAULT_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanelEntityEditLayout.createSequentialGroup().addComponent(jComboBoxEntityTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonEntityAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))).addContainerGap()));
        jPanelEntityEditLayout.setVerticalGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelEntityEditLayout.createSequentialGroup().addContainerGap().addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonEntityAdd).addComponent(jComboBoxEntityTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonEntityCopy).addComponent(jButtonEntityCut)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanelEntityEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonEntityRemove).addComponent(jButtonEntityPaste))));
        jPanelEntityListParams.setPreferredSize(new java.awt.Dimension(235, 382));
        jTreeEntities.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeEntitiesValueChanged(evt);
            }
        });
        jTreeEntities.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jScrollPane1.setViewportView(jTreeEntities);
        jTableEntityParams.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTableEntityParamsPropertyChange(evt);
            }
        });
        jTableEntityParams.setModel(new TableModelEntityParams());
        jTableEntityParams.setBackground(new java.awt.Color(255, 255, 255));
        jTableEntityParams.setFillsViewportHeight(true);
        jScrollPane2.setViewportView(jTableEntityParams);
        jTableEntityParams.getAccessibleContext().setAccessibleParent(jPaneEntities);
        javax.swing.GroupLayout jPanelEntityListParamsLayout = new javax.swing.GroupLayout(jPanelEntityListParams);
        jPanelEntityListParams.setLayout(jPanelEntityListParamsLayout);
        jPanelEntityListParamsLayout.setHorizontalGroup(jPanelEntityListParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelEntityListParamsLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanelEntityListParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))));
        jPanelEntityListParamsLayout.setVerticalGroup(jPanelEntityListParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEntityListParamsLayout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jScrollPane2.getAccessibleContext().setAccessibleParent(jScrollPane2);
        jPaneEntitiesLayout.setHorizontalGroup(jPaneEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPaneEntitiesLayout.createSequentialGroup().addGroup(jPaneEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanelEntityEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanelEntityListParams, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPaneEntitiesLayout.setVerticalGroup(jPaneEntitiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPaneEntitiesLayout.createSequentialGroup().addContainerGap().addComponent(jPanelEntityEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanelEntityListParams, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)));
        jTreeEntities.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) value;
                    if (tn.getUserObject() instanceof EntityGroup) return super.getTreeCellRendererComponent(tree, value, sel, expanded, false, row, hasFocus);
                }
                return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }
        });
        final JPopupMenu popup = new JPopupMenu();
        JMenuItem item = new JMenuItem("Add Group");
        item.addActionListener(new PopupActionListener());
        item.setActionCommand(POPUP_CMD_ADD_GROUP);
        popup.add(item);
        item = new JMenuItem("Remove Group");
        item.addActionListener(new PopupActionListener());
        item.setActionCommand(POPUP_CMD_REMOVE_GROUP);
        popup.add(item);
        jTreeEntities.addMouseListener(new MouseAdapter() {

            private void showPopup(MouseEvent e) {
                TreePath tp = jTreeEntities.getPathForLocation(e.getX(), e.getY());
                if (tp == null) return;
                jTreeEntities.setSelectionPath(tp);
                popup.show((JComponent) e.getSource(), e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }
        });
        jTreeEntities.setDragEnabled(true);
        jTreeEntities.setDropMode(DropMode.ON);
        jTreeEntities.setDropTarget(new DropTarget(jTreeEntities, new EntityDropTargetListener()));
    }

    /**
     * Drag & Drop handler for entities and groups in tree.
     *
     */
    private class EntityDropTargetListener implements DropTargetListener {

        DefaultMutableTreeNode srcNode;

        public void dragEnter(DropTargetDragEvent dtde) {
            TreePath path = jTreeEntities.getSelectionPath();
            if ((path == null) || (path.getPathCount() <= 1)) {
                return;
            }
            srcNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        }

        public void dragOver(DropTargetDragEvent dtde) {
        }

        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        public void dragExit(DropTargetEvent dte) {
            srcNode = null;
        }

        public void drop(DropTargetDropEvent dtde) {
            if (srcNode == null) {
                dtde.rejectDrop();
                return;
            }
            Point p = dtde.getLocation();
            TreePath path = jTreeEntities.getClosestPathForLocation(p.x, p.y);
            final DefaultMutableTreeNode dstnode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = dstnode.getUserObject();
            if (!(obj instanceof EntityGroup)) {
                dtde.rejectDrop();
                return;
            }
            EntityGroup group = (EntityGroup) obj;
            if (dstnode.isNodeAncestor(srcNode)) {
                dtde.rejectDrop();
                return;
            }
            if (srcNode.getUserObject() instanceof EntityGroup) {
                EntityGroup grp = (EntityGroup) srcNode.getUserObject();
                ed.lockData();
                EntityGroup srcparentgroup = ed.getEntityGroup().findGroupParent(grp);
                ed.unlockData();
                if (group.getGroups().contains(grp)) {
                    dtde.rejectDrop();
                    return;
                }
                ed.undoManager.addRegroup(grp, null, group);
                ed.lockData();
                srcparentgroup.removeGroup(grp);
                group.addGroup(grp);
                ed.unlockData();
            } else if (srcNode.getUserObject() instanceof Entity) {
                Entity ent = (Entity) srcNode.getUserObject();
                ed.lockData();
                EntityGroup srcparentgroup = ed.getEntityGroup().findEntityParent(ent);
                ed.unlockData();
                ed.undoManager.addRegroup(null, ent, group);
                ed.lockData();
                srcparentgroup.removeEntity(ent);
                group.addEntity(ent);
                ed.unlockData();
            } else {
                Log.error("internal DnD error");
                dtde.rejectDrop();
                return;
            }
            DefaultMutableTreeNode np = (DefaultMutableTreeNode) srcNode.getParent();
            np.remove(srcNode);
            ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) np);
            dstnode.add(srcNode);
            ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) dstnode);
            TreePath[] paths = new TreePath[] { new TreePath(srcNode.getPath()) };
            jTreeEntities.setSelectionPaths(paths);
            jTreeEntities.makeVisible(paths[0]);
            jTreeEntities.scrollPathToVisible(paths[0]);
        }
    }

    /**
     * Class for handling tree pop-up actions
     */
    private class PopupActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            TreePath path = jTreeEntities.getSelectionPath();
            if (path == null) return;
            DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = treenode.getUserObject();
            if (ae.getActionCommand().equals(POPUP_CMD_ADD_GROUP)) {
                if (obj instanceof EntityGroup) {
                    EntityGroup group = new EntityGroup("new group");
                    ed.lockData();
                    ((EntityGroup) obj).addGroup(group);
                    ed.unlockData();
                    ed.undoManager.addEntityGroupCreation(group);
                    DefaultMutableTreeNode newnode = new DefaultMutableTreeNode();
                    newnode.setUserObject(group);
                    treenode.add(newnode);
                    ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) treenode);
                    TreePath[] paths = new TreePath[] { new TreePath(newnode.getPath()) };
                    jTreeEntities.setSelectionPaths(paths);
                }
            }
            if (ae.getActionCommand().equals(POPUP_CMD_REMOVE_GROUP)) {
                if (obj instanceof EntityGroup) {
                    EntityGroup group = (EntityGroup) obj;
                    ed.lockData();
                    EntityGroup parent = ed.getEntityGroup().findGroupParent(group);
                    if (parent != null) {
                        if (group.getGroups().size() > 0 || group.getEntities().size() > 0) {
                            if (JOptionPane.showConfirmDialog(ed.mainFrame, "Entity group is not empty. Really remove it?", "Attention", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                ed.unlockData();
                                return;
                            }
                        }
                        ed.undoManager.addEntityGroupDestruction(group);
                        parent.removeGroup(group);
                        DefaultMutableTreeNode np = (DefaultMutableTreeNode) treenode.getParent();
                        np.remove(treenode);
                        ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) np);
                        TreePath[] paths = new TreePath[] { new TreePath(np.getPath()) };
                        jTreeEntities.setSelectionPaths(paths);
                    }
                    ed.unlockData();
                }
            }
        }
    }

    /**
     * Custom table model used for entity parameters' display.
     */
    private class TableModelEntityParams extends javax.swing.table.DefaultTableModel {

        boolean[] canEdit = new boolean[] { false, true };

        public TableModelEntityParams() {
            super();
            addColumn("Name");
            addColumn("Value");
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }
    }

    /**
     * Called when a file is loading.
     */
    @Override
    public void onLoad() {
        copyNode = null;
        cutNode = null;
    }

    /**
     * Called when file is closing.
     */
    @Override
    public void onClose() {
        copyNode = null;
        cutNode = null;
        jTreeEntities.setModel(null);
        jTreeEntities.setRootVisible(false);
    }

    /**
     * Setup all gui components.
     */
    private void setupComponents() {
        jComboBoxEntityTypes.removeAllItems();
        EntityBuilder eb = ed.getEntityBuilder();
        List<String> ae = eb.getAvailableEntities();
        for (int cnt = 0; cnt < ae.size(); cnt++) {
            jComboBoxEntityTypes.addItem(ae.get(cnt));
        }
        updateEntityTree();
        updateEntityParams(null);
    }

    /**
     * Build up the tree node hierarchy.
     */
    private void createTreeNode(DefaultMutableTreeNode node, EntityGroup group) {
        ed.lockData();
        for (EntityGroup grp : group.getGroups()) {
            DefaultMutableTreeNode grpnode = new DefaultMutableTreeNode(grp);
            node.add(grpnode);
            createTreeNode(grpnode, grp);
        }
        for (Entity entity : group.getEntities()) {
            DefaultMutableTreeNode entnode = new DefaultMutableTreeNode(entity);
            node.add(entnode);
        }
        ed.unlockData();
    }

    /**
     * Add an entity into the currently selected group (or parent group of a selected entity) in tree.
     * Set 'select' to true if the new added entity should be selected.
     * Return the new tree node.
     */
    public DefaultMutableTreeNode addEntityToTree(Entity entity, boolean select) {
        DefaultMutableTreeNode newnode = new DefaultMutableTreeNode();
        newnode.setUserObject(entity);
        DefaultMutableTreeNode dstnode = null;
        TreePath path = jTreeEntities.getSelectionPath();
        if (path != null) {
            dstnode = (DefaultMutableTreeNode) path.getLastPathComponent();
        } else {
            dstnode = (DefaultMutableTreeNode) jTreeEntities.getModel().getRoot();
        }
        Object obj = dstnode.getUserObject();
        EntityGroup dstgrp = null;
        if (obj instanceof Entity) {
            ed.lockData();
            dstgrp = ed.getEntityGroup().findEntityParent((Entity) obj);
            ed.unlockData();
            ((DefaultMutableTreeNode) dstnode.getParent()).add(newnode);
            ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) dstnode.getParent());
        } else if (obj instanceof EntityGroup) {
            dstgrp = (EntityGroup) obj;
            ((DefaultMutableTreeNode) dstnode).add(newnode);
            ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) dstnode);
        }
        if (dstgrp == null) {
            Log.error("Editor: internal error occured during Paste operation");
            return newnode;
        }
        ed.lockData();
        dstgrp.addEntity(entity);
        ed.unlockData();
        if (select) {
            TreePath[] paths = new TreePath[] { new TreePath(newnode.getPath()) };
            jTreeEntities.setSelectionPaths(paths);
            jTreeEntities.makeVisible(new TreePath(newnode.getPath()));
        }
        return newnode;
    }

    /**
     * Update the tree reflecting the entity group hierarchy.
     */
    public void updateEntityTree() {
        if (ed.currentFile.isEmpty()) {
            jTreeEntities.setModel(null);
            jTreeEntities.setRootVisible(false);
            return;
        }
        DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(ed.getEntityGroup());
        DefaultTreeModel tm = new DefaultTreeModel(rootnode);
        createTreeNode(rootnode, ed.getEntityGroup());
        jTreeEntities.setExpandsSelectedPaths(true);
        jTreeEntities.setModel(tm);
        jTreeEntities.setRootVisible(true);
    }

    private void updateEntityParams(Entity ent) {
        final TableModelEntityParams tm = new TableModelEntityParams();
        if (ent == null) {
            DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) jTreeEntities.getLastSelectedPathComponent();
            if (treenode == null) return;
            EntityGroup grp = null;
            if ((treenode.getUserObject() != null) && (treenode.getUserObject() instanceof EntityGroup)) grp = (EntityGroup) treenode.getUserObject();
            if (grp == null) return;
            tm.addRow(new String[] { "Group Name", grp.getName() });
            tm.addRow(new String[] { "Entity Count", new Integer(grp.getEntities().size()).toString() });
            jTableEntityParams.setModel(tm);
            return;
        }
        List<String> params = ent.getParamNames();
        tm.addRow(new String[] { "instance", ent.getInstanceName() });
        for (int cnt = 0; cnt < params.size(); cnt++) {
            try {
                tm.addRow(new String[] { params.get(cnt), ent.getParamValue(params.get(cnt)).toString() });
            } catch (Exception e) {
                Log.error("Editor: cannot get value of parameter " + params.get(cnt));
            }
        }
        jTableEntityParams.setModel(tm);
    }

    /**
     * Get currently selected entity in tree.
     * Return null if no entity is selected.
     */
    private Entity getTreeSelectionEntity() {
        if (jTreeEntities.getSelectionCount() == 0) return null;
        DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) jTreeEntities.getLastSelectedPathComponent();
        if (treenode.getUserObject() instanceof Entity) return (Entity) treenode.getUserObject();
        return null;
    }

    /**
     * Set the tree selection by entity uuid.
     */
    private void setTreeSelectionEntity(UUID uuid) {
        if (uuid == null) return;
        Entity entsel = ed.getEntityGroup().findEntity(uuid);
        if (entsel == null) return;
        DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) jTreeEntities.getModel().getRoot();
        selectTreeNode(rootnode, entsel);
    }

    /**
     * Select entity's tree node start searching for it with given node.
     */
    private void selectTreeNode(DefaultMutableTreeNode node, Entity entity) {
        for (int cnt = 0; cnt < node.getChildCount(); cnt++) {
            final DefaultMutableTreeNode childnode = (DefaultMutableTreeNode) node.getChildAt(cnt);
            Object userobj = childnode.getUserObject();
            if (userobj instanceof EntityGroup) {
                selectTreeNode(childnode, entity);
            } else if (userobj instanceof Entity) {
                if (((Entity) userobj).getUUID().equals(entity.getUUID())) {
                    TreePath[] paths = new TreePath[] { new TreePath(childnode.getPath()) };
                    jTreeEntities.setSelectionPaths(paths);
                    jTreeEntities.makeVisible(paths[0]);
                    jTreeEntities.scrollPathToVisible(paths[0]);
                    return;
                }
            }
        }
    }

    private void jTreeEntitiesValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        Entity selentity = getTreeSelectionEntity();
        updateEntityParams(selentity);
        if (selentity == null) {
            ed.camera.setHighlightNode(null);
            return;
        }
        final UUID uuid = selentity.getUUID();
        BaseState currstate = Core.get().getAppState();
        SceneNode topnode = currstate.getSceneManager().getTopNode();
        topnode.apply(new SceneNode.Visitor() {

            @Override
            public void apply(SceneNode node) {
                Object obj = node.getUserObject();
                if (obj instanceof UUID) {
                    if (uuid.equals(obj)) {
                        ed.camera.setHighlightNode(node);
                        return;
                    }
                }
                for (SceneNode child : node.getChildren()) {
                    child.apply(this);
                }
            }
        });
    }

    private void jTableEntityParamsPropertyChange(java.beans.PropertyChangeEvent evt) {
        int sel = jTableEntityParams.getEditingRow();
        if (sel < 0) {
            return;
        }
        if (jTableEntityParams.getModel().getColumnCount() < 2) {
            return;
        }
        String paramname = null;
        String value = null;
        try {
            paramname = jTableEntityParams.getModel().getValueAt(sel, 0).toString();
            value = jTableEntityParams.getModel().getValueAt(sel, 1).toString();
        } catch (Exception e) {
            return;
        }
        Entity ent = getTreeSelectionEntity();
        if (ent == null) {
            DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) jTreeEntities.getLastSelectedPathComponent();
            if (treenode == null) return;
            EntityGroup grp = null;
            if ((treenode.getUserObject() != null) && (treenode.getUserObject() instanceof EntityGroup)) grp = (EntityGroup) treenode.getUserObject();
            if (grp == null) return;
            if (sel == 0) {
                grp.setName(value);
                jTreeEntities.repaint();
            } else if (sel == 1) {
                jTableEntityParams.getModel().setValueAt(new Integer(grp.getEntities().size()).toString(), 1, 1);
            }
            return;
        }
        try {
            if (paramname.equals("instance")) {
                ent.setInstanceName(value);
            } else {
                ent.setParamValueAsString(paramname, value);
            }
        } catch (Exception e) {
            Log.error("Editor: cannot set entity parameter " + ent.getClass().getSimpleName() + "/" + paramname + "\n reason: " + e);
        }
        try {
            if (!paramname.equals("instance")) {
                String newval = ent.getParamValue(paramname).toString();
                jTableEntityParams.getModel().setValueAt(newval, sel, 1);
            } else {
                String newval = ent.getInstanceName();
                jTableEntityParams.getModel().setValueAt(newval, sel, 1);
            }
        } catch (Exception e) {
            Log.error("Editor: cannot set entity parameter " + ent.getClass().getSimpleName() + "/" + paramname + "\n reason: " + e);
        }
        if (paramname.equals("instance")) {
            DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) jTreeEntities.getLastSelectedPathComponent();
            ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) treenode);
        }
    }

    private void jButtonEntityAddActionPerformed(java.awt.event.ActionEvent evt) {
        if (ed.currentFile.isEmpty()) {
            return;
        }
        EntityBuilder eb = ed.getEntityBuilder();
        String selitem = (String) jComboBoxEntityTypes.getSelectedItem();
        Entity entity = null;
        try {
            entity = eb.createEntity(selitem, selitem.toLowerCase());
            if (entity.getParamNames().contains("position")) {
                Vec2dInt pos = new Vec2dInt(Core.get().getAppState().getSceneManager().getCameraPosition());
                Vec2dInt dim = new Vec2dInt(Core.get().getAppState().getSceneManager().getViewDimension());
                entity.setParamValue("position", pos.add(dim.div(2)));
            }
            List<String[]> plist = new ArrayList<String[]>();
            List<String> params = entity.getParamNames();
            for (int cnt = 0; cnt < params.size(); cnt++) {
                try {
                    plist.add(new String[] { params.get(cnt), entity.getParamValue(params.get(cnt)).toString() });
                } catch (Exception e) {
                    Log.error("Editor: cannot get value of parameter " + params.get(cnt));
                }
            }
            DlgEntityParams dlg = new DlgEntityParams(ed.mainFrame);
            dlg.setParameters(plist);
            dlg.setInstanceName(entity.getClass().getSimpleName().toLowerCase());
            dlg.setVisible(true);
            if (!dlg.getApplyState()) {
                entity.destroy();
                return;
            }
            plist = dlg.getParameters();
            for (int cnt = 0; cnt < plist.size(); cnt++) {
                String[] row = plist.get(cnt);
                try {
                    entity.setParamValueAsString(row[0], row[1]);
                } catch (Exception e) {
                    Log.error("Editor: cannot set value of parameter " + row[0]);
                }
            }
            entity.setInstanceName(dlg.getInstanceName());
        } catch (Exception e) {
            Log.error("could not create entity\n reason: " + e);
            return;
        }
        ed.lockData();
        try {
            entity.initialize();
            entity.postInitialize();
        } catch (Exception e) {
            Log.error("Editor: problem initializing new entity\n reason: " + e);
        }
        ed.unlockData();
        addEntityToTree(entity, true);
        ed.undoManager.addEntityCreation(entity);
    }

    private void jButtonEntityCopyActionPerformed(java.awt.event.ActionEvent evt) {
        TreePath path = jTreeEntities.getSelectionPath();
        if (path == null) return;
        cutNode = null;
        copyNode = (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    private void jButtonEntityCutActionPerformed(java.awt.event.ActionEvent evt) {
        TreePath path = jTreeEntities.getSelectionPath();
        if (path == null) return;
        copyNode = null;
        cutNode = (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    private void jButtonEntityPasteActionPerformed(java.awt.event.ActionEvent evt) {
        TreePath path = jTreeEntities.getSelectionPath();
        if (path == null) return;
        if ((copyNode == null) && (cutNode == null)) return;
        Object obj = (copyNode != null) ? copyNode.getUserObject() : cutNode.getUserObject();
        if (copyNode != null) {
            if (obj instanceof EntityGroup) {
                return;
            } else if (obj instanceof Entity) {
                ed.lockData();
                Entity newent = ((Entity) obj).clone();
                if (newent == null) {
                    ed.unlockData();
                    return;
                }
                newent.initialize();
                newent.postInitialize();
                ed.unlockData();
                addEntityToTree(newent, true);
                ed.undoManager.addEntityCreation(newent);
            } else {
                Log.error("Editor: unsupported tree node type");
                return;
            }
        } else if (cutNode != null) {
            if (obj instanceof EntityGroup) {
                EntityGroup grp = (EntityGroup) obj;
                ed.lockData();
                EntityGroup currparent = ed.getEntityGroup().findGroupParent(grp);
                if (currparent == null) {
                    ed.unlockData();
                    return;
                }
                TreeNode nodeparent = (TreeNode) cutNode.getParent();
                cutNode.removeFromParent();
                ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(nodeparent);
                DefaultMutableTreeNode dstnode = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object dstobj = dstnode.getUserObject();
                EntityGroup destgroup = null;
                if (dstobj instanceof Entity) {
                    destgroup = ed.getEntityGroup().findEntityParent((Entity) obj);
                    ((DefaultMutableTreeNode) dstnode.getParent()).add(cutNode);
                    ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(dstnode.getParent());
                } else if (dstobj instanceof EntityGroup) {
                    destgroup = (EntityGroup) obj;
                    ((DefaultMutableTreeNode) dstnode).add(cutNode);
                    ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(dstnode);
                }
                ed.undoManager.addRegroup(grp, null, destgroup);
                currparent.removeGroup(grp);
                destgroup.addGroup(grp);
                ed.unlockData();
            } else if (obj instanceof Entity) {
                Entity ent = (Entity) obj;
                ed.lockData();
                EntityGroup currparent = ed.getEntityGroup().findEntityParent(ent);
                if (currparent == null) {
                    ed.unlockData();
                    return;
                }
                TreeNode nodeparent = (TreeNode) cutNode.getParent();
                cutNode.removeFromParent();
                ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(nodeparent);
                DefaultMutableTreeNode dstnode = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object dstobj = dstnode.getUserObject();
                EntityGroup destgroup = null;
                if (dstobj instanceof Entity) {
                    destgroup = ed.getEntityGroup().findEntityParent((Entity) dstobj);
                    ((DefaultMutableTreeNode) dstnode.getParent()).add(cutNode);
                    ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(dstnode.getParent());
                } else if (dstobj instanceof EntityGroup) {
                    destgroup = (EntityGroup) dstobj;
                    ((DefaultMutableTreeNode) dstnode).add(cutNode);
                    ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged(dstnode);
                }
                ed.undoManager.addRegroup(null, ent, destgroup);
                currparent.removeEntity(ent);
                destgroup.addEntity(ent);
                ed.unlockData();
            } else {
                Log.error("Editor: unsupported tree node type");
                return;
            }
            TreePath[] paths = new TreePath[] { new TreePath(cutNode.getPath()) };
            jTreeEntities.setSelectionPaths(paths);
            jTreeEntities.makeVisible(paths[0]);
            jTreeEntities.scrollPathToVisible(paths[0]);
            cutNode = null;
        }
    }

    private void jButtonEntityRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        TreePath path = jTreeEntities.getSelectionPath();
        if (path == null) return;
        if (ed.camera != null) ed.camera.setHighlightNode(null);
        DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = treenode.getUserObject();
        if (obj instanceof EntityGroup) {
            EntityGroup group = (EntityGroup) obj;
            ed.lockData();
            EntityGroup parent = ed.getEntityGroup().findGroupParent(group);
            if (parent != null) {
                if (group.getGroups().size() > 0 || group.getEntities().size() > 0) {
                    if (JOptionPane.showConfirmDialog(ed.mainFrame, "Entity group is not empty. Really remove it?", "Attention", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        ed.unlockData();
                        return;
                    }
                }
                ed.undoManager.addEntityGroupDestruction(group);
                parent.removeGroup(group);
            }
            ed.unlockData();
        } else if (obj instanceof Entity) {
            Entity ent = (Entity) obj;
            ed.undoManager.addEntityDestruction(ent);
            ed.lockData();
            EntityGroup parent = ed.getEntityGroup().findEntityParent(ent);
            if (parent != null) {
                parent.removeEntity(ent);
            }
            ent.destroy();
            ed.unlockData();
        } else {
            return;
        }
        DefaultMutableTreeNode np = (DefaultMutableTreeNode) treenode.getParent();
        np.remove(treenode);
        ((DefaultTreeModel) jTreeEntities.getModel()).nodeStructureChanged((TreeNode) np);
        TreePath[] paths = new TreePath[] { new TreePath(np.getPath()) };
        jTreeEntities.setSelectionPaths(paths);
        jTreeEntities.makeVisible(paths[0]);
        jTreeEntities.scrollPathToVisible(paths[0]);
    }

    /**
     * Adapt the view size, call this method when the pane size changed.
     */
    protected void adaptViewSize() {
        if (Core.get().getAppState() == null) return;
        SceneManager sm = Core.get().getAppState().getSceneManager();
        Dimension dim = ed.canvas.getSize();
        Vec2dInt screendim = sm.getScreenDimension();
        screendim.x = dim.width;
        screendim.y = dim.height;
        sm.setViewDimension(new Vec2dInt(dim.width - ed.CAMERA_OFFSET_X * 2, dim.height - ed.CAMERA_OFFSET_Y * 2));
    }

    @Override
    public void eventCanvasResized(ComponentEvent evt) {
        adaptViewSize();
    }

    @Override
    protected void eventCanvasMouseReleased(MouseEvent evt) {
        SceneNode sel = ed.camera.getHighlightNode();
        if ((undoPosition != null) && (sel != null)) {
            ed.undoManager.addSceneNodeMovement(sel, undoPosition.clone(), sel.getPosition().clone());
            undoPosition = null;
        }
    }

    @Override
    protected void eventCanvasMousePressed(MouseEvent evt) {
        Vec2dInt pos = new Vec2dInt();
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            java.awt.Point p = evt.getPoint();
            pos.x = p.x;
            pos.y = p.y;
        } else {
            return;
        }
        SceneManager sm = Core.get().getAppState().getSceneManager();
        SceneNode topnode = sm.getTopNode();
        if (topnode == null) {
            return;
        }
        Vec2dInt campos = sm.getCameraPosition();
        Vec4dInt viewarea = new Vec4dInt(pos.x, pos.y, pos.x + 1, pos.y + 1);
        List<SceneNode> visnodes = new ArrayList<SceneNode>();
        viewarea = viewarea.add(campos.x - ed.CAMERA_OFFSET_X, campos.y - ed.CAMERA_OFFSET_Y, campos.x - ed.CAMERA_OFFSET_X, campos.y - ed.CAMERA_OFFSET_Y);
        topnode.cull(viewarea, visnodes, SceneNode.TYPE_SELECTABLE, false);
        for (int i = 0; i < visnodes.size(); i++) {
            SceneNode node = visnodes.get(i);
            if (node.equals(sm.getTopNode())) {
                visnodes.remove(node);
                break;
            }
        }
        if (visnodes.isEmpty()) {
            if (ed.camera != null) ed.camera.setHighlightNode(null);
            return;
        }
        Random rd = new Random();
        int cnt = Math.abs(rd.nextInt()) % visnodes.size();
        SceneNode selnode = visnodes.get(cnt);
        if (ed.camera != null) ed.camera.setHighlightNode(selnode);
        nodeClickOffset.x = selnode.getPosition().x - (campos.x + pos.x);
        nodeClickOffset.y = selnode.getPosition().y - (campos.y + pos.y);
        UUID uuid = selnode.getUserObject();
        setTreeSelectionEntity(uuid);
    }

    private void updateEntityPosition(UUID uuid, Vec2dInt pos) {
        if (uuid == null) return;
        EntityGroup entities = ed.getEntityGroup();
        Entity entsel = entities.findEntity(uuid);
        if (entsel == null) return;
        try {
            Vec2dInt currpos = entsel.getParamValue("position");
            currpos.x = pos.x;
            currpos.y = pos.y;
            entsel.setParamValue("position", currpos);
            updateEntityParams(entsel);
        } catch (Exception e) {
            Log.warning("cannot move the entity, it has no position parameter!");
        }
    }

    @Override
    protected void eventCanvasKeyReleased(KeyEvent evt) {
        if (!evt.isControlDown()) ed.camera.setHighlightMode(Camera.HighlightMode.HIGHTLIGHT_MODE_NORMAL);
    }

    @Override
    protected void eventCanvasKeyPressed(KeyEvent evt) {
        if ((ed.camera == null) || (ed.camera.getHighlightNode() == null)) {
            return;
        }
        if (!evt.isControlDown()) return;
        ed.camera.setHighlightMode(Camera.HighlightMode.HIGHTLIGHT_MODE_MOVE);
        int x = 0;
        int y = 0;
        switch(evt.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                y = 1;
                break;
            case KeyEvent.VK_UP:
                y = -1;
                break;
            case KeyEvent.VK_LEFT:
                x = -1;
                break;
            case KeyEvent.VK_RIGHT:
                x = 1;
                break;
            default:
                return;
        }
        SceneNode sel = ed.camera.getHighlightNode();
        if (undoPosition == null) {
            undoPosition = new Vec2dInt(sel.getPosition().clone());
        }
        Vec2dInt pos = sel.getPosition();
        pos.x += x;
        pos.y += y;
        sel.setPosition(pos);
        UUID uuid = sel.getUserObject();
        updateEntityPosition(uuid, pos);
        Core.get().getAppState().getSceneManager().getTopNode().calculateBounds();
    }

    @Override
    protected void eventCanvasMouseMoved(java.awt.event.MouseEvent evt) {
        BaseState st = Core.get().getAppState();
        if (st instanceof StateEntities) {
            ((StateEntities) st).setMousePosition(evt.getX(), evt.getY());
        }
    }

    @Override
    protected void eventCanvasMouseDragged(java.awt.event.MouseEvent evt) {
        BaseState st = Core.get().getAppState();
        if (st instanceof StateEntities) {
            ((StateEntities) st).setMousePosition(evt.getX(), evt.getY());
        }
        if ((ed.camera == null) || (ed.camera.getHighlightNode() == null)) {
            return;
        }
        SceneNode sel = ed.camera.getHighlightNode();
        if (undoPosition == null) {
            undoPosition = new Vec2dInt(sel.getPosition().clone());
        }
        Vec2dInt pos = new Vec2dInt(evt.getX(), evt.getY());
        SceneManager sm = Core.get().getAppState().getSceneManager();
        pos.x += sm.getCameraPosition().x + nodeClickOffset.x;
        pos.y += sm.getCameraPosition().y + nodeClickOffset.y;
        sel.setPosition(pos);
        UUID uuid = sel.getUserObject();
        updateEntityPosition(uuid, pos);
        sm.getTopNode().calculateBounds();
    }
}
