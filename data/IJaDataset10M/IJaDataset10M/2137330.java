package pck_tap.Userinterface.dat.frmEditFermentationCollection;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import pck_tap.alg.Images;
import pck_tap.beerxml.recipe.Fermentation;
import pck_tap.beerxml.recipe.Fermentation_Step;
import pck_tap.current.CurrentObjectMemory;

public class JTreeFermentationProfiles extends JPanel {

    private Boolean debug = false;

    private CurrentObjectMemory com = new CurrentObjectMemory();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JTree jTreeBron = new JTree();

    private JToolBar jLeftToolBar = new JToolBar();

    private BorderLayout borderLayout1 = new BorderLayout();

    private JButton jButton1Refresh = new JButton();

    private FlowLayout flowLayout1 = new FlowLayout();

    private ImageIcon refresh = Images.getToolbar_refresh();

    private ImageIcon delete = Images.getToolbar_delete();

    private ImageIcon save = Images.getToolbar_save();

    private ImageIcon add = Images.getToolbar_add();

    DefaultMutableTreeNode nNode;

    MutableTreeNode node;

    TreePath path;

    DefaultTreeModel jTreeModelDoel;

    DefaultMutableTreeNode jTreeNodeDoelRoot;

    DefaultTreeModel jTreeModelBron;

    DefaultMutableTreeNode jTreeNodeBronRoot;

    private JButton jButtonDelete = new JButton();

    private JButton jButtonSave = new JButton();

    private JButton jButtonAdd = new JButton();

    public JTreeFermentationProfiles() {
        try {
            jbInit();
            jbInitTreeBron();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setSize(new Dimension(245, 421));
        this.setToolTipText("Add a");
        jTreeBron.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                jTreeBron_valueChanged(e);
            }
        });
        jTreeBron.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                jTreeBron_propertyChange(e);
            }
        });
        jLeftToolBar.setMinimumSize(new Dimension(32, 3));
        jLeftToolBar.setMaximumSize(new Dimension(32, 3));
        jLeftToolBar.setPreferredSize(new Dimension(32, 3));
        jLeftToolBar.setLayout(flowLayout1);
        jLeftToolBar.setFloatable(false);
        jLeftToolBar.setRollover(true);
        jLeftToolBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        jButton1Refresh.setIcon(refresh);
        jButton1Refresh.setToolTipText("Refresh the data");
        jButton1Refresh.setFocusPainted(false);
        jButton1Refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doActionDataReload(e);
            }
        });
        jButtonDelete.setIcon(delete);
        jButtonDelete.setToolTipText("Delete a fermentation profile");
        jButtonDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton2_actionPerformed(e);
            }
        });
        jButtonSave.setIcon(save);
        jButtonSave.setToolTipText("Sa");
        jButtonSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButtonSave_actionPerformed(e);
            }
        });
        jButtonAdd.setToolTipText("Add new Fermentation Profile");
        jButtonAdd.setIcon(add);
        jButtonAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButtonAdd_actionPerformed(e);
            }
        });
        jScrollPane1.getViewport().add(jTreeBron, null);
        jLeftToolBar.add(jButton1Refresh, null);
        jLeftToolBar.add(jButtonAdd, null);
        jLeftToolBar.add(jButtonDelete, null);
        jLeftToolBar.add(jButtonSave, null);
        this.add(jLeftToolBar, BorderLayout.WEST);
        this.add(jScrollPane1, BorderLayout.CENTER);
    }

    public void setCom(CurrentObjectMemory com) {
        this.com = com;
        actionDoRefreshData();
    }

    public CurrentObjectMemory getCom() {
        return com;
    }

    private void jbInitTreeBron() {
        jTreeBron.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jTreeNodeBronRoot = new DefaultMutableTreeNode("Fermentation Profiles");
        jTreeModelBron = new DefaultTreeModel(jTreeNodeBronRoot);
        jTreeBron.setModel(jTreeModelBron);
        jTreeBron.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        jTreeModelBron.setRoot(jTreeNodeBronRoot);
    }

    private void actionDoRefreshData() {
        removeAllBron();
        Enumeration eNum = this.com.getXmlCollections().getRecipe().getFermentation().elements();
        while (eNum.hasMoreElements()) {
            DefaultMutableTreeNode vDefaultMutableTreeNodeNew = new DefaultMutableTreeNode();
            Fermentation vFermentation = ((Fermentation) eNum.nextElement());
            vDefaultMutableTreeNodeNew.setUserObject(vFermentation);
            file2bron(vDefaultMutableTreeNodeNew);
        }
    }

    private void removeAllBron() {
        DefaultMutableTreeNode FirstLeaf = jTreeNodeBronRoot.getFirstLeaf();
        DefaultMutableTreeNode NextLeaf;
        int childCount = this.jTreeNodeBronRoot.getChildCount();
        if (childCount > 0) {
            jTreeModelBron.removeNodeFromParent(FirstLeaf);
            for (int i = 0; i < childCount - 1; i++) {
                NextLeaf = this.jTreeNodeBronRoot.getNextNode();
                jTreeModelBron.removeNodeFromParent(NextLeaf);
            }
        }
    }

    private void file2bron(DefaultMutableTreeNode p_node2move) {
        DefaultMutableTreeNode nodeClode = (DefaultMutableTreeNode) p_node2move.clone();
        if (p_node2move != null && p_node2move.isLeaf()) {
            jTreeModelBron.insertNodeInto(nodeClode, jTreeNodeBronRoot, jTreeModelBron.getChildCount(jTreeNodeBronRoot));
            jTreeBronExpandAll();
        }
    }

    public void jTreeBronExpandAll() {
        if (jTreeBron == null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jTreeBron.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                expandBron((TreeNode) (jTreeBron.getModel().getRoot()));
                jTreeBron.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void expandBron(TreeNode _tn) {
        if (_tn.getChildCount() == 0) {
            TreePath tp = new TreePath(((DefaultMutableTreeNode) _tn).getPath());
            this.jTreeBron.expandPath(tp.getParentPath());
            return;
        }
        Enumeration eNum = _tn.children();
        while (eNum.hasMoreElements()) {
            expandBron((DefaultMutableTreeNode) (eNum.nextElement()));
        }
    }

    private void doActionDataReload(ActionEvent e) {
        actionDoRefreshData();
    }

    private void jButton2_actionPerformed(ActionEvent e) {
        try {
            DefaultMutableTreeNode p_node2move = (DefaultMutableTreeNode) jTreeBron.getLastSelectedPathComponent();
            this.com.getXmlCollections().getRecipe().getFermentation().remove((Fermentation) p_node2move.getUserObject());
            jTreeModelBron.removeNodeFromParent(p_node2move);
        } catch (Exception ea) {
        }
    }

    private void jButtonSave_actionPerformed(ActionEvent e) {
        com.getXmlFermentationWriterStax().save();
    }

    private void pl(String s) {
        if (debug) {
            System.out.println(this.getClass().getName() + s);
        }
    }

    private void jTreeBron_valueChanged(TreeSelectionEvent e) {
        try {
            pl(".jTreeBron_valueChanged : " + this.jTreeBron.getLastSelectedPathComponent().toString());
            this.firePropertyChange("value changed", this.jTreeBron.getLastSelectedPathComponent(), this.jTreeBron.getLastSelectedPathComponent());
        } catch (Exception ex) {
            ex = null;
        }
    }

    private void jTreeFermentationProfiles_propertyChange(PropertyChangeEvent e) {
        pl(".jTreeFermentationProfiles_propertyChange : " + e.getPropertyName());
    }

    private void this_propertyChange(PropertyChangeEvent e) {
        pl(".this_propertyChange : " + e.getPropertyName());
    }

    private void jTreeBron_propertyChange(PropertyChangeEvent e) {
        try {
            if (e.getPropertyName().equalsIgnoreCase("leadSelectionPath")) {
                this.firePropertyChange(e.getPropertyName(), e.getOldValue(), ((DefaultMutableTreeNode) this.jTreeBron.getLastSelectedPathComponent()).getUserObject());
            }
        } catch (Exception ee) {
        }
    }

    private void jButtonAdd_actionPerformed(ActionEvent e) {
        Fermentation vFermentation = new Fermentation();
        Fermentation_Step vMs = new Fermentation_Step();
        vMs.setName("<new>");
        vFermentation.getFermentation_Steps().addElement(vMs);
        this.com.getXmlCollections().getRecipe().getFermentation().addElement(vFermentation);
        DefaultMutableTreeNode vDefaultMutableTreeNodeNew = new DefaultMutableTreeNode();
        vFermentation.setName("new");
        vDefaultMutableTreeNodeNew.setUserObject(vFermentation);
        DefaultMutableTreeNode nodeClode = (DefaultMutableTreeNode) vDefaultMutableTreeNodeNew.clone();
        if (vDefaultMutableTreeNodeNew != null && vDefaultMutableTreeNodeNew.isLeaf()) {
            jTreeModelBron.insertNodeInto(nodeClode, jTreeNodeBronRoot, jTreeModelBron.getChildCount(jTreeNodeBronRoot));
            jTreeBronExpandAll();
        }
        TreeNode[] nodes = jTreeModelBron.getPathToRoot(nodeClode);
        TreePath path = new TreePath(nodes);
        jTreeBron.scrollPathToVisible(path);
        jTreeBron.setSelectionPath(path);
    }
}
