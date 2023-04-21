package com.iver.andami.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * @author fjp
 *
 * The reason behind this class is to have the opportunity of use a DlgPreferences
 * in a local way. Then, you don't need to be a SingletonView.
 */
public class GenericDlgPreferences extends JPanel implements IWindow {

    private WindowInfo viewInfo = null;

    private IPreference activePreference;

    private Hashtable preferences = new Hashtable();

    DefaultTreeModel treeModel = null;

    private JTree jTreePlugins = null;

    private JPanel jPanelButtons = null;

    private JButton jButtonOK = null;

    private JButton jButtonCancel = null;

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    private JPanel jPanelCenter = null;

    private JLabel jLabelBigTitle = null;

    private JScrollPane jScrollPane = null;

    private JSplitPane jSplitPaneCenter = null;

    private JPanel jPanelContainer = null;

    private JButton jButtonRestore;

    private ActionListener action;

    private boolean dirtyTree = false;

    private class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        public MyTreeCellRenderer() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node.getUserObject() instanceof IPreference) {
                    IPreference pref = (IPreference) node.getUserObject();
                    this.setText(pref.getTitle());
                }
            }
            return this;
        }
    }

    public GenericDlgPreferences() {
        super();
        this.action = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PluginServices.getMDIManager().setWaitCursor();
                String actionCommand = e.getActionCommand();
                if ("RESTORE".equals(actionCommand)) {
                    if (activePreference != null) {
                        activePreference.initializeDefaults();
                    }
                } else {
                    Iterator it = preferences.keySet().iterator();
                    if ("CANCEL".equals(actionCommand)) {
                        while (it.hasNext()) {
                            IPreference pref = (IPreference) preferences.get(it.next());
                            if (pref.isValueChanged()) pref.initializeValues();
                        }
                        closeView();
                    } else if ("OK".equals(actionCommand)) {
                        boolean shouldClose = true;
                        while (it.hasNext()) {
                            IPreference preference = (IPreference) preferences.get(it.next());
                            try {
                                if (preference.isValueChanged()) {
                                    preference.saveValues();
                                    preference.initializeValues();
                                }
                            } catch (StoreException ex) {
                                shouldClose = false;
                                PluginServices.getMDIManager().restoreCursor();
                                JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), ex.getMessage());
                                setActivePage(preference);
                            }
                        }
                        if (shouldClose) closeView();
                    }
                }
                PluginServices.getMDIManager().restoreCursor();
            }
        };
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setSize(new java.awt.Dimension(750, 479));
        super.add(getJPanelButtons(), BorderLayout.SOUTH);
        setPreferredSize(new java.awt.Dimension(390, 369));
        super.add(getJSplitPaneCenter(), java.awt.BorderLayout.CENTER);
        getJSplitPaneCenter().setLeftComponent(getJScrollPane());
        getJSplitPaneCenter().setRightComponent(getJPanelNorth());
        treeModel = new DefaultTreeModel(root);
    }

    public void refreshExtensionPoints() {
        ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
        ExtensionPoint extensionPoint = (ExtensionPoint) extensionPoints.get("AplicationPreferences");
        Iterator iterator = extensionPoint.keySet().iterator();
        while (iterator.hasNext()) {
            try {
                IPreference obj = (IPreference) extensionPoint.create((String) iterator.next());
                this.addPreferencePage(obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        ArrayList<IPreference> prefList = new ArrayList<IPreference>(preferences.values());
        addPreferencePages(prefList);
    }

    public void storeValues() {
        ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
        ExtensionPoint extensionPoint = (ExtensionPoint) extensionPoints.get("AplicationPreferences");
        Iterator iterator = extensionPoint.keySet().iterator();
        while (iterator.hasNext()) {
            try {
                IPreference obj = (IPreference) extensionPoint.create((String) iterator.next());
                this.addPreferencePage(obj);
                {
                    try {
                        obj.saveValues();
                    } catch (StoreException e) {
                        PluginServices.getMDIManager().restoreCursor();
                        JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), e.getMessage());
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * It is very common to be confused with this method. But
	 * the one you are looking for is addPreferencePage(IPreference)
	 */
    public Component add(Component c) {
        throw new Error("Do not use com.iver.cit.gvsig.gui.preferences.DlgPreferences.add(Component) register an extension point instead");
    }

    public Component add(Component c, int i) {
        return add(c);
    }

    public void add(Component c, Object o) {
        add(c);
    }

    public void add(Component c, Object o, int i) {
        add(c);
    }

    public WindowInfo getWindowInfo() {
        if (viewInfo == null) {
            viewInfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
            viewInfo.setTitle(PluginServices.getText(this, "Preferences"));
            viewInfo.setWidth(this.getWidth() + 8);
            viewInfo.setHeight(this.getHeight());
        }
        return viewInfo;
    }

    /**
	 * This method initializes jTreePlugins
	 *
	 * @return javax.swing.JTree
	 */
    private JTree getJTreePlugins() {
        if (jTreePlugins == null) {
            jTreePlugins = new JTree();
            jTreePlugins.setRootVisible(false);
            MyTreeCellRenderer treeCellRenderer = new MyTreeCellRenderer();
            treeCellRenderer.setOpenIcon(null);
            treeCellRenderer.setClosedIcon(null);
            treeCellRenderer.setLeafIcon(null);
            jTreePlugins.setCellRenderer(treeCellRenderer);
            jTreePlugins.setShowsRootHandles(true);
            jTreePlugins.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreePlugins.getLastSelectedPathComponent();
                    if (node == null) return;
                    setActivePage((IPreference) node.getUserObject());
                }
            });
            jTreePlugins.putClientProperty("JTree.linestyle", "Angled");
            jTreePlugins.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        }
        return jTreePlugins;
    }

    /**
	 * It takes an IPreference page and adds it to the application's preferences
	 * dialog. The preference page is added in alphabetical order within the
	 * branch where the page is hanging on, and defined by its title.
	 * @param page
	 */
    public void addPreferencePage(IPreference page) {
        if (preferences.containsKey(page.getID())) return;
        preferences.put(page.getID(), page);
        page.initializeValues();
        if (dirtyTree) {
            dirtyTree = false;
            DefaultTreeModel model = new DefaultTreeModel(root);
            treeModel = model;
            jTreePlugins.setModel(model);
        }
        doInsertNode(treeModel, page);
        getJTreePlugins().setModel(treeModel);
        getJTreePlugins().repaint();
    }

    private void addPreferencePages(ArrayList<IPreference> prefs) {
        while (prefs.size() > 0) {
            IPreference pref = prefs.get(0);
            if (pref.getParentID() != null && preferences.get(pref.getParentID()) == null) {
                prefs.remove(pref);
                addPreferencePages(prefs);
                addPreference(pref);
            } else {
                addPreference(pref);
                prefs.remove(pref);
            }
        }
    }

    private void addPreference(IPreference pref) {
        DefaultTreeModel model = new DefaultTreeModel(root);
        doInsertNode(model, pref);
    }

    private DefaultMutableTreeNode findNode(String searchID) {
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode nodeAux = (DefaultMutableTreeNode) e.nextElement();
            if (nodeAux != null) {
                IPreference pref = (IPreference) nodeAux.getUserObject();
                if (pref == null) continue;
                if (pref.getID().equals(searchID)) {
                    return nodeAux;
                }
            }
        }
        return null;
    }

    private void doInsertNode(DefaultTreeModel treeModel, IPreference page) {
        dirtyTree = ((page.getParentID() != null) && (findNode(page.getParentID()) == null));
        if (findNode(page.getID()) != null) return;
        if (page.getParentID() != null) {
            if (preferences.containsKey(page.getParentID())) {
                IPreference parent = (IPreference) preferences.get(page.getParentID());
                DefaultMutableTreeNode nodeParent = findNode(parent.getID());
                if (nodeParent == null) {
                    doInsertNode(treeModel, parent);
                } else {
                    DefaultMutableTreeNode nodeValue = new DefaultMutableTreeNode(page);
                    int children = nodeParent.getChildCount();
                    int pos = 0;
                    for (int i = 0; i < children; i++) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getChild(nodeParent, i);
                        if (node.getUserObject() instanceof IPreference) {
                            String pageTitle = ((IPreference) node.getUserObject()).getTitle();
                            if (pageTitle.compareTo(page.getTitle()) < 0) ++pos;
                        }
                    }
                    treeModel.insertNodeInto(nodeValue, nodeParent, pos);
                }
            }
        } else {
            DefaultMutableTreeNode nodeValue = new DefaultMutableTreeNode(page);
            int children = root.getChildCount();
            int pos = 0;
            for (int i = 0; i < children; i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getChild(root, i);
                if (node.getUserObject() instanceof IPreference) {
                    String pageTitle = ((IPreference) node.getUserObject()).getTitle();
                    if (pageTitle.compareTo(page.getTitle()) < 0) ++pos;
                }
            }
            treeModel.insertNodeInto(nodeValue, root, pos);
        }
    }

    /**
	 * This method initializes jPanelButtons
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelButtons() {
        if (jPanelButtons == null) {
            jPanelButtons = new JPanel(new BorderLayout());
            JPanel jPanelAux = new JPanel();
            JLabel l = new JLabel();
            l.setPreferredSize(new Dimension(40, 20));
            jPanelButtons.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
            jPanelAux.add(getJButtonRestore(), BorderLayout.WEST);
            jPanelAux.add(l, BorderLayout.CENTER);
            jPanelAux.add(getJButtonOK(), BorderLayout.EAST);
            jPanelAux.add(getJButtonCancel(), BorderLayout.EAST);
            jPanelButtons.add(jPanelAux);
        }
        return jPanelButtons;
    }

    /**
	 * This method initializes jButtonOK
	 *
	 * @return JButton
	 */
    private JButton getJButtonOK() {
        if (jButtonOK == null) {
            jButtonOK = new JButton();
            jButtonOK.setText(PluginServices.getText(this, "aceptar"));
            jButtonOK.setActionCommand("OK");
            jButtonOK.addActionListener(action);
        }
        return jButtonOK;
    }

    /**
	 * This method initializes jButtonOK
	 *
	 * @return JButton
	 */
    private JButton getJButtonRestore() {
        if (jButtonRestore == null) {
            jButtonRestore = new JButton();
            jButtonRestore.setText(PluginServices.getText(this, "restore_defaults"));
            jButtonRestore.setActionCommand("RESTORE");
            jButtonRestore.addActionListener(action);
        }
        return jButtonRestore;
    }

    private void closeView() {
        PluginServices.getMDIManager().closeWindow(this);
    }

    /**
	 * This method initializes jButtonCancel
	 *
	 * @return JButton
	 */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setText(PluginServices.getText(this, "cancelar"));
            jButtonCancel.setActionCommand("CANCEL");
            jButtonCancel.addActionListener(action);
        }
        return jButtonCancel;
    }

    /**
	 * This method initializes jPanelNorth
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelNorth() {
        if (jPanelCenter == null) {
            jLabelBigTitle = new JLabel();
            jLabelBigTitle.setText("General");
            jLabelBigTitle.setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.BOLD, 14));
            jLabelBigTitle.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
            jLabelBigTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jPanelCenter = new JPanel();
            JPanel jPanelPageTitle = new JPanel(new BorderLayout());
            JPanel jPanelAux = new JPanel(new BorderLayout());
            jPanelAux.add(jLabelBigTitle, java.awt.BorderLayout.NORTH);
            jPanelPageTitle.add(jPanelAux, java.awt.BorderLayout.WEST);
            jPanelPageTitle.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
            jPanelCenter.setLayout(new BorderLayout());
            jPanelCenter.add(jPanelPageTitle, BorderLayout.NORTH);
            jPanelCenter.add(getJPanelContainer(), java.awt.BorderLayout.CENTER);
        }
        return jPanelCenter;
    }

    /**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setPreferredSize(new java.awt.Dimension(140, 322));
            jScrollPane.setViewportView(getJTreePlugins());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jSplitPaneCenter
	 *
	 * @return javax.swing.JSplitPane
	 */
    private JSplitPane getJSplitPaneCenter() {
        if (jSplitPaneCenter == null) {
            jSplitPaneCenter = new JSplitPane();
            jSplitPaneCenter.setResizeWeight(0.2);
            jSplitPaneCenter.setDividerLocation(200);
        }
        return jSplitPaneCenter;
    }

    /**
	 * This method initializes jPanelContainer
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelContainer() {
        if (jPanelContainer == null) {
            jPanelContainer = new JPanel();
        }
        return jPanelContainer;
    }

    /**
	 *
	 */
    public void setActivePage(IPreference page) {
        activePreference = page;
        jLabelBigTitle.setText(activePreference.getTitle());
        JPanel prefPanel = activePreference.getPanel();
        jLabelBigTitle.setIcon(activePreference.getIcon());
        jPanelContainer.removeAll();
        if ((prefPanel instanceof AbstractPreferencePage) && ((AbstractPreferencePage) prefPanel).isResizeable()) {
            jPanelContainer.setLayout(new BorderLayout());
            jPanelContainer.add(prefPanel, BorderLayout.CENTER);
        } else {
            jPanelContainer.setLayout(new FlowLayout());
            jPanelContainer.add(prefPanel);
        }
        prefPanel.setVisible(true);
        repaint();
    }

    public Object getWindowProfile() {
        return WindowInfo.DIALOG_PROFILE;
    }
}
