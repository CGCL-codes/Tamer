package org.tcpfile.gui.settingsmanager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.VariableHeightLayoutCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.gui.settingsmanager.settings.Setting;

/**
 * Manages the options / settings dialog of this program.
 * @author manveru-mh
 *
 */
public class Dialog {

    private JFrame jFrameDialog = null;

    private JPanel dialogContentPane = null;

    private JTree dTree = null;

    private DefaultMutableTreeNode dTreeRootNode = new DefaultMutableTreeNode("Settings");

    private DefaultTreeModel dTreeModel = new DefaultTreeModel(dTreeRootNode);

    private HashMap<String, JComponent> hashPanels = new HashMap<String, JComponent>();

    private JSplitPane jSplitPaneTreeContent = null;

    private JScrollPane jScrollPaneTree = null;

    private JScrollPane jScrollPaneContent = null;

    private static Logger log = LoggerFactory.getLogger(Dialog.class);

    private JPanel jPanelDialogButtons = null;

    private JButton jButtonDialogButtonsSave = null;

    private JButton jButtonDialogButtonsCancel = null;

    private JButton jButtonDialogButtonsImport = null;

    private JButton jButtonDialogButtonsExport = null;

    private boolean showImportExportButton = true;

    private Stack<TreePath> pathToBeExpanded = new Stack<TreePath>();

    private SettingsManager sm;

    public Dialog(SettingsManager sm) {
        this.sm = sm;
        initialize();
    }

    /**
	 * Initializes this class.
	 *
	 */
    private void initialize() {
        initjFrame();
        log.debug("Dialog initialized");
    }

    JPanel getPanel() {
        return getDialogContentPane();
    }

    /**
	 * Makes the jFrame dialog visible depending on the parameter.
	 * @param visible true -> visible, false -> hide
	 */
    public void setVisible(boolean visible) {
        jFrameDialog.setVisible(visible);
    }

    /**
	 * TODO: shows only visible nodes atm
	 * @param name
	 * @return
	 */
    public boolean showTreeItem(String name) {
        TreePath tp = dTree.getNextMatch(name, 0, Position.Bias.Forward);
        if (tp == null) {
            return false;
        }
        setSelectedTreeItem(tp);
        return true;
    }

    public void setSelectedTreeItem(TreePath tp) {
        ArrayList objectTp = new ArrayList(Arrays.asList(tp.getPath()));
        objectTp.remove(objectTp.size() - 1);
        Object[] array = objectTp.toArray();
        ArrayList partArray = new ArrayList();
        partArray.add(array[0]);
        TreePath tpByAdding = new TreePath(array[0]);
        TreePath bla = dTree.getPathForRow(4);
        for (int i = 1; i < array.length; i++) {
            partArray.add(array[i]);
            tpByAdding = tpByAdding.pathByAddingChild(array[i]);
            TreePath newTp = new TreePath(partArray.toArray());
            int row = dTree.getRowForPath(newTp);
            int row2 = dTree.getRowForPath(bla);
            int row3 = dTree.getRowForPath(tpByAdding);
            dTree.expandRow(row);
        }
        dTree.setSelectionPath(tp);
        return;
    }

    /**
	 * Loads last saved settings.
	 *
	 */
    synchronized void loadSettings(ArrayList<SettingContainer> sc, String lastSelectedTreeItem) {
        clear();
        parseOptions(sc);
        jScrollPaneContent.setViewportView(null);
        if (dTree.getRowCount() > 0) {
            if (lastSelectedTreeItem == null) {
                dTree.setSelectionRow(0);
                return;
            }
            TreePath path = null;
            do {
                path = dTree.getNextMatch(lastSelectedTreeItem, 0, Position.Bias.Forward);
                if (path == null) {
                    break;
                }
            } while (!path.getLastPathComponent().toString().equals(lastSelectedTreeItem));
            if (path != null) {
                dTree.setSelectionPath(path);
            }
            if (dTree.getSelectionPath() == null) {
                dTree.setSelectionRow(0);
            }
        }
    }

    private void clear() {
        dTreeRootNode.removeAllChildren();
        dTreeModel.reload();
        hashPanels.clear();
    }

    private void parseOptions(ArrayList<SettingContainer> topSC) {
        dTreeRootNode.removeAllChildren();
        if (topSC != null) {
            buildTreeRecursively(dTreeRootNode, topSC, 0);
        }
        dTreeModel.reload();
        while (pathToBeExpanded.size() > 0) {
            dTree.expandPath(pathToBeExpanded.pop());
        }
    }

    private synchronized void buildTreeRecursively(DefaultMutableTreeNode parent, ArrayList<SettingContainer> parentSC, int level) {
        if (level > SettingsManager.LEVELLIMIT) {
            log.warn("Tree build reached limit of " + SettingsManager.LEVELLIMIT + ". There might be a loop in the definition of the containers.");
            return;
        }
        for (SettingContainer sc : parentSC) {
            if (sc.isTreeItem() && sc.isVisible()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(sc.getName());
                parent.add(node);
                createPanel(sc);
                if (sc.getSettingContainer() != null) {
                    buildTreeRecursively(node, sc.getSettingContainer(), ++level);
                    if (sc.isExpanded()) {
                        pathToBeExpanded.push(new TreePath(node.getPath()));
                    }
                }
            }
        }
    }

    JComponent createPanel(SettingContainer topSC) {
        if (topSC.isVisible() == false || (topSC.getSettingContainer() == null && topSC.getSettings() == null)) {
            return null;
        }
        Box currentBox = new Box(BoxLayout.Y_AXIS);
        hashPanels.put(topSC.getName(), currentBox);
        for (Setting s : topSC.getSettings()) {
            if (s.isVisible()) {
                currentBox.add(s.getRepresentation());
                currentBox.add(Box.createVerticalStrut(5));
            }
        }
        for (SettingContainer sc : topSC.getSettingContainer()) {
            if (sc.isVisible() && !sc.isTreeItem()) {
                JPanel borderPanel = new JPanel();
                borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
                Border b = BorderFactory.createTitledBorder(sc.getName());
                borderPanel.setBorder(b);
                for (Setting s : sc.getSettings()) {
                    if (s.isVisible()) {
                        borderPanel.add(s.getRepresentation());
                        borderPanel.add(Box.createVerticalStrut(5));
                    }
                }
                currentBox.add(borderPanel);
            }
        }
        SwingUtilities.updateComponentTreeUI(currentBox);
        return currentBox;
    }

    /**
	 * Saves the current settings from the OptionsDialog to the program.
	 *
	 */
    private void saveSettings() {
        sm.saveToXML(null);
    }

    /**
	 * Prepares and opens the OptionsDialog
	 *
	 */
    void open(ArrayList<SettingContainer> sc) {
        sm.updateRepresentations();
        setVisible(true);
    }

    /**
	 * Set default jFrame options for the main jFrame (dialog).
	 *
	 */
    private void initjFrame() {
        jFrameDialog = new JFrame();
        jFrameDialog.setTitle("tcpfile: Settings");
        jFrameDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrameDialog.setSize(new Dimension(638, 424));
        jFrameDialog.setContentPane(getDialogContentPane());
        jFrameDialog.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
            }
        });
    }

    /**
	 * This method initializes dialogContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getDialogContentPane() {
        if (dialogContentPane == null) {
            dialogContentPane = new JPanel();
            dialogContentPane.setName("Settings");
            dialogContentPane.setLayout(new BorderLayout());
            dialogContentPane.setPreferredSize(new Dimension(660, 345));
            dialogContentPane.add(getJSplitPaneTreeContent(), BorderLayout.CENTER);
            dialogContentPane.add(getJPanelDialogButtons(), BorderLayout.SOUTH);
        }
        return dialogContentPane;
    }

    /**
	 * This method initializes odTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
    private JTree getTree() {
        if (dTree == null) {
            DefaultTreeSelectionModel defaultTreeSelectionModel1 = new DefaultTreeSelectionModel();
            defaultTreeSelectionModel1.setSelectionMode(0);
            VariableHeightLayoutCache variableHeightLayoutCache = new VariableHeightLayoutCache();
            variableHeightLayoutCache.setSelectionModel(defaultTreeSelectionModel1);
            DefaultTreeSelectionModel defaultTreeSelectionModel = new DefaultTreeSelectionModel();
            defaultTreeSelectionModel.setSelectionMode(1);
            defaultTreeSelectionModel.setRowMapper(variableHeightLayoutCache);
            dTree = new JTree(dTreeModel);
            dTree.setRootVisible(false);
            dTree.setEditable(false);
            dTree.setRowHeight(22);
            dTree.setFont(new Font("Dialog", Font.PLAIN, 12));
            dTree.setShowsRootHandles(true);
            dTree.setSelectionModel(defaultTreeSelectionModel);
            dTree.setExpandsSelectedPaths(true);
            dTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    try {
                        TreePath tp = e.getNewLeadSelectionPath();
                        if (tp != null) {
                            jScrollPaneContent.setViewportView(hashPanels.get(tp.getLastPathComponent().toString()));
                            SettingsManager.settingsManager.lastSelectedTreeItem = tp.getLastPathComponent().toString();
                        }
                    } catch (RuntimeException e1) {
                        log.warn("Failed to respond to tree selection change");
                    }
                }
            });
            dTree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {

                public void treeExpanded(javax.swing.event.TreeExpansionEvent e) {
                    String name = e.getPath().getLastPathComponent().toString();
                    SettingContainer sc = sm.findSettingContainer(name);
                    if (sc != null) {
                        sc.setExpanded(true);
                    }
                }

                public void treeCollapsed(javax.swing.event.TreeExpansionEvent e) {
                    String name = e.getPath().getLastPathComponent().toString();
                    SettingContainer sc = sm.findSettingContainer(name);
                    if (sc != null) {
                        sc.setExpanded(false);
                    }
                }
            });
        }
        return dTree;
    }

    /**
	 * This method initializes jSplitPaneTreeContent	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
    private JSplitPane getJSplitPaneTreeContent() {
        if (jSplitPaneTreeContent == null) {
            jSplitPaneTreeContent = new JSplitPane();
            jSplitPaneTreeContent.setDividerSize(5);
            jSplitPaneTreeContent.setDividerLocation(150);
            jSplitPaneTreeContent.setRightComponent(getJScrollPaneContent());
            jSplitPaneTreeContent.setLeftComponent(getJScrollPaneodTree());
        }
        return jSplitPaneTreeContent;
    }

    /**
	 * This method initializes jScrollPaneodTree	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneodTree() {
        if (jScrollPaneTree == null) {
            jScrollPaneTree = new JScrollPane();
            jScrollPaneTree.setViewportView(getTree());
        }
        return jScrollPaneTree;
    }

    /**
	 * This method initializes jScrollPaneContent	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneContent() {
        if (jScrollPaneContent == null) {
            jScrollPaneContent = new JScrollPane();
        }
        return jScrollPaneContent;
    }

    /**
	 * This method initializes jPanelDialogButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelDialogButtons() {
        if (jPanelDialogButtons == null) {
            jPanelDialogButtons = new JPanel();
            jPanelDialogButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jPanelDialogButtons.add(getJButtonDialogButtonsSave());
            jPanelDialogButtons.add(getJButtonDialogButtonsCancel());
            jPanelDialogButtons.add(getJButtonDialogButtonsImport());
            jPanelDialogButtons.add(getJButtonDialogButtonsExport());
        }
        return jPanelDialogButtons;
    }

    /**
	 * This method initializes jButtonDialogButtonsSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonDialogButtonsSave() {
        if (jButtonDialogButtonsSave == null) {
            jButtonDialogButtonsSave = new JButton();
            jButtonDialogButtonsSave.setText("Save");
            jButtonDialogButtonsSave.setToolTipText("Save the changes. Write them also to the default file.");
            jButtonDialogButtonsSave.setMnemonic(KeyEvent.VK_S);
            jButtonDialogButtonsSave.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveSettings();
                }
            });
        }
        return jButtonDialogButtonsSave;
    }

    /**
	 * This method initializes jButtonDialogButtonsCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonDialogButtonsCancel() {
        if (jButtonDialogButtonsCancel == null) {
            jButtonDialogButtonsCancel = new JButton();
            jButtonDialogButtonsCancel.setText("Cancel");
            jButtonDialogButtonsCancel.setToolTipText("Revert all changes to the last saved state.");
            jButtonDialogButtonsCancel.setMnemonic(KeyEvent.VK_C);
            jButtonDialogButtonsCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    sm.updateRepresentations();
                }
            });
        }
        return jButtonDialogButtonsCancel;
    }

    /**
	 * This method initializes jButtonDialogButtonsImport	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonDialogButtonsImport() {
        if (jButtonDialogButtonsImport == null) {
            jButtonDialogButtonsImport = new JButton();
            jButtonDialogButtonsImport.setName("");
            jButtonDialogButtonsImport.setText("Import...");
            jButtonDialogButtonsImport.setToolTipText("Import settings from a file.");
            jButtonDialogButtonsImport.setEnabled(true);
            jButtonDialogButtonsImport.setVisible(showImportExportButton);
            jButtonDialogButtonsImport.setMnemonic(KeyEvent.VK_I);
            jButtonDialogButtonsImport.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFrame dialog = new JFrame("JFileChooser Popup");
                    dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Container contentPane = dialog.getContentPane();
                    final JFileChooser fc = new JFileChooser();
                    fc.setControlButtonsAreShown(true);
                    contentPane.add(fc, BorderLayout.CENTER);
                    File currentDir = new File(new File("").getAbsolutePath());
                    if (currentDir.exists()) {
                        fc.setCurrentDirectory(currentDir);
                    }
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setFileFilter(new FileFilter() {

                        public boolean accept(File file) {
                            if (file.isDirectory()) {
                                return true;
                            }
                            return (file.getName().endsWith(".xml"));
                        }

                        public String getDescription() {
                            return "XML Settings (*.xml)";
                        }
                    });
                    fc.setApproveButtonText("Import");
                    fc.setDialogTitle("Import tcpfile settings.");
                    int returnVal = fc.showOpenDialog(dialog);
                    if (returnVal == 0) {
                        File f = fc.getSelectedFile();
                        if (f != null) {
                            SettingsManager sm = SettingsManager.loadFromXML(f.getAbsolutePath());
                            if (sm != null) {
                                SettingsManager.settingsManager.takeOverSettings(sm);
                            } else {
                                log.info("Failed to load settings.xml");
                            }
                        }
                    }
                }
            });
        }
        return jButtonDialogButtonsImport;
    }

    /**
	 * This method initializes jButtonDialogButtonsExport	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonDialogButtonsExport() {
        if (jButtonDialogButtonsExport == null) {
            jButtonDialogButtonsExport = new JButton();
            jButtonDialogButtonsExport.setText("Export...");
            jButtonDialogButtonsExport.setToolTipText("Save and export the current settings to a file.");
            jButtonDialogButtonsExport.setMnemonic(KeyEvent.VK_E);
            jButtonDialogButtonsExport.setVisible(showImportExportButton);
            jButtonDialogButtonsExport.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveSettings();
                    JFrame dialog = new JFrame("JFileChooser Popup");
                    dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Container contentPane = dialog.getContentPane();
                    final JFileChooser fc = new JFileChooser();
                    fc.setControlButtonsAreShown(true);
                    contentPane.add(fc, BorderLayout.CENTER);
                    File currentDir = new File(new File("").getAbsolutePath());
                    if (currentDir.exists()) {
                        fc.setCurrentDirectory(currentDir);
                    }
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setFileFilter(new FileFilter() {

                        public boolean accept(File file) {
                            if (file.isDirectory()) {
                                return true;
                            }
                            return (file.getName().endsWith(".xml"));
                        }

                        public String getDescription() {
                            return "XML Settings (*.xml)";
                        }
                    });
                    fc.setApproveButtonText("Export");
                    fc.setDialogTitle("Export tcpfile settings.");
                    int returnVal = fc.showOpenDialog(dialog);
                    if (returnVal == 0) {
                        File f = fc.getSelectedFile();
                        if (f != null) {
                            if (f.exists()) {
                                if (JOptionPane.showConfirmDialog(null, "Overwrite '" + f.getName() + "'?", "File already exists", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                                    return;
                                }
                            }
                            try {
                                sm.saveToXML(f.getCanonicalPath());
                            } catch (IOException e1) {
                                log.error("", e1);
                            }
                        }
                    }
                }
            });
        }
        return jButtonDialogButtonsExport;
    }

    public DefaultMutableTreeNode getDTreeRootNode() {
        return dTreeRootNode;
    }

    public boolean isShowImportExportButton() {
        return showImportExportButton;
    }

    public void setShowImportExportButton(boolean showImportExportButton) {
        this.showImportExportButton = showImportExportButton;
        if (jButtonDialogButtonsImport != null) {
            jButtonDialogButtonsImport.setVisible(showImportExportButton);
        }
        if (jButtonDialogButtonsExport != null) {
            jButtonDialogButtonsExport.setVisible(showImportExportButton);
        }
    }
}
