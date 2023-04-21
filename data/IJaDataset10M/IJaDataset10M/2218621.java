package LabDBComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import LabDB.LabDBAccess;
import LabDBHelperFunctions.LabDBTextHelpers;

public class LabDBSetupsPanel extends JPanel {

    private LabDBAccess db;

    private ImageIcon hardwareLeafIcon, setupLeafIcon;

    private JTree hardwareTree, setupTree;

    private JTextField hardwareNameTF, hardwarePurposeTF, hardwareBuilderTF, hardwareOwnerTF, hardwareInventoryTF;

    private JComboBox hardwareCategoryCombo, hardwareStatusCombo, hardwareFundingCombo;

    private JTextArea hardwareDescriptionArea;

    private JButton submitHardwareBtn, deleteHardwareBtn, editHardwareBtn, newSetupBtn, editSetupBtn, deleteSetupBtn, submitSetupBtn;

    private String[] hardwareColumns = { "name", "category", "purpose", "builtBy", "owner", "inventoryNo", "description", "status", "fundingID" };

    private String[] setupColumns = { "name", "description" };

    private LabDBAttachments hardwareAttachments, setupAttachments;

    private LabDBSetupsPanelActionListener al;

    private JTextField setupNameTF;

    private JTextArea setupDescriptionArea;

    private JTabbedPane tabPane;

    private String selectedHardwareID = "-1", selectedSetupID = "-1";

    private boolean newHardwareEntry = false, hardwareEntryEdited = false, newSetupEntry = false, setupEntryEdited = false;

    private boolean cntrlPressed = false;

    /**
	 * Constructor 
	 * @param db - the database connection
	 * @param hardwareIcon - icon used to highlight hardware nodes in the trees 
	 * @param setupIcon - icon used to highlight setup nodes in the tress
	 */
    public LabDBSetupsPanel(LabDBAccess db, ImageIcon hardwareIcon, ImageIcon setupIcon) {
        super(new BorderLayout());
        this.setBackground(Color.white);
        this.db = db;
        hardwareLeafIcon = hardwareIcon;
        setupLeafIcon = setupIcon;
        al = new LabDBSetupsPanelActionListener();
        setGUI(800, 610);
        setVisible(true);
    }

    private void setGUI(int width, int height) {
        JSplitPane treeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        treeSplitPane.add(setSetupTreePanel(), 0);
        treeSplitPane.add(setHardwareTreePanel(), 1);
        treeSplitPane.setDividerLocation(200);
        tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
        tabPane.setBackground(Color.white);
        tabPane.setBorder(BorderFactory.createTitledBorder("descriptions"));
        tabPane.addTab("Setup description", null, setSetupDescriptionPanel());
        tabPane.addTab("Hardware description", null, setHardwareDescriptionPanel());
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.add(treeSplitPane, 0);
        mainSplitPane.add(tabPane, 1);
        mainSplitPane.setDividerLocation(400);
        this.add(mainSplitPane, BorderLayout.CENTER);
    }

    private JPanel setSetupDescriptionPanel() {
        SetupEditorKeyListener skl = new SetupEditorKeyListener();
        setupNameTF = new JTextField();
        setupNameTF.addKeyListener(skl);
        setupNameTF.setEditable(false);
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(Color.white);
        topPanel.add(new JLabel("setup name"));
        topPanel.add(setupNameTF);
        setupDescriptionArea = new JTextArea();
        setupDescriptionArea.setWrapStyleWord(true);
        setupDescriptionArea.setLineWrap(true);
        setupDescriptionArea.setEditable(false);
        setupDescriptionArea.setToolTipText("enter a setup description here. press ctrl-s to finish entering and submit");
        setupDescriptionArea.addKeyListener(skl);
        JScrollPane setupDescrScrollPane = new JScrollPane(setupDescriptionArea);
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(Color.white);
        descriptionPanel.add(new JLabel("setup description"), BorderLayout.NORTH);
        descriptionPanel.add(setupDescrScrollPane, BorderLayout.CENTER);
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setBackground(Color.white);
        center.add(topPanel);
        center.add(descriptionPanel);
        setupAttachments = new LabDBAttachments(db, "setupAttachments", "setupID", Color.white);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setFocusable(false);
        panel.add(center, BorderLayout.CENTER);
        panel.add(setupAttachments, BorderLayout.EAST);
        panel.setBackground(Color.white);
        setupNameTF.setEditable(false);
        deleteSetupBtn.setEnabled(false);
        setupDescriptionArea.setEditable(false);
        submitSetupBtn.setEnabled(false);
        return panel;
    }

    private JPanel setSetupTreePanel() {
        setupSetupTree();
        JScrollPane setupTreePane = new JScrollPane(setupTree);
        newSetupBtn = new JButton("new");
        newSetupBtn.setActionCommand("newSetupBtn");
        newSetupBtn.setToolTipText("enter new setup description");
        newSetupBtn.addActionListener(al);
        deleteSetupBtn = new JButton("delete");
        deleteSetupBtn.setActionCommand("deleteSetupBtn");
        deleteSetupBtn.setEnabled(false);
        deleteSetupBtn.setToolTipText("delete a setup");
        deleteSetupBtn.addActionListener(al);
        submitSetupBtn = new JButton("submit");
        submitSetupBtn.setActionCommand("submitSetupBtn");
        submitSetupBtn.setEnabled(true);
        submitSetupBtn.setEnabled(false);
        submitSetupBtn.setToolTipText("submit changes to database");
        submitSetupBtn.addActionListener(al);
        editSetupBtn = new JButton("edit");
        editSetupBtn.setActionCommand("editSetupBtn");
        editSetupBtn.setEnabled(false);
        editSetupBtn.setToolTipText("edit this entry");
        editSetupBtn.addActionListener(al);
        JPanel southPanel = new JPanel(new GridLayout(2, 2));
        southPanel.setBackground(Color.white);
        southPanel.add(newSetupBtn);
        southPanel.add(editSetupBtn);
        southPanel.add(deleteSetupBtn);
        southPanel.add(submitSetupBtn);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createTitledBorder("setups"));
        panel.add(setupTreePane, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void setupSetupTree() {
        setupTree = new JTree();
        setupTree.setToolTipText("select items to view information. Use drag'n'drop to add hardware to setups.");
        setupTree.setDropMode(DropMode.ON);
        final Component temp = this;
        setupTree.addKeyListener(new SetupTreeKeyListener());
        setupTree.setTransferHandler(new TransferHandler() {

            private DataFlavor NodeInfoFlavor = new DataFlavor(NodeInfo.class, "Hardware or setup nodeInfo");

            @Override
            public boolean canImport(TransferSupport supp) {
                if (!supp.isDataFlavorSupported(NodeInfoFlavor)) {
                    return false;
                }
                DropLocation loc = supp.getDropLocation();
                JTree temp = (JTree) setupTree.getComponentAt(loc.getDropPoint());
                TreePath path = temp.getClosestPathForLocation(loc.getDropPoint().x, loc.getDropPoint().y);
                temp.setSelectionPath(path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) temp.getLastSelectedPathComponent();
                if (node.getUserObject().getClass() == NodeInfo.class) {
                    if (((NodeInfo) node.getUserObject()).type == NodeInfo.SETUP_NODE) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            @Override
            public boolean importData(TransferSupport supp) {
                if (!canImport(supp)) {
                    return false;
                }
                Transferable t = supp.getTransferable();
                NodeInfo data = null;
                try {
                    data = (NodeInfo) t.getTransferData(NodeInfoFlavor);
                } catch (Exception e) {
                    return false;
                }
                JTree.DropLocation loc = setupTree.getDropLocation();
                if (loc != null) {
                    setupTree.setSelectionPath(loc.getPath());
                    DefaultMutableTreeNode temp = (DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent();
                    if (temp.getUserObject().getClass() == NodeInfo.class) {
                        if (requestSetupEdit(((NodeInfo) temp.getUserObject()).id)) {
                            addHardware(data, (NodeInfo) temp.getUserObject());
                            return true;
                        } else {
                            return false;
                        }
                    } else return false;
                }
                {
                    return false;
                }
            }

            ;

            private void addHardware(NodeInfo data, NodeInfo destinationNode) {
                Vector<String> values = new Vector<String>();
                values.add(destinationNode.id);
                values.add(data.id);
                db.insertNewRow("hardwareRegister", values, new String[] { "setupID", "hardwareID" });
                refreshSetupTree();
            }
        });
        ToolTipManager.sharedInstance().registerComponent(setupTree);
        setupTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setupTree.setCellRenderer(new SetupTreeRenderer(setupLeafIcon, hardwareLeafIcon));
        setupTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (setupTree.getSelectionCount() != 0) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent();
                    if (node == null) {
                        displaySetup("-1");
                        displayHardware("-1");
                        deleteSetupBtn.setEnabled(false);
                        editSetupBtn.setEnabled(false);
                        return;
                    }
                    Object nodeInfo = node.getUserObject();
                    if (node.getLevel() > 0) {
                        NodeInfo info = (NodeInfo) nodeInfo;
                        deleteSetupBtn.setEnabled(true);
                        editSetupBtn.setEnabled(true);
                        if (info.type == NodeInfo.HARDWARE_NODE) {
                            if (!checkHardwareUpdates()) return;
                            hardwareAttachments.refreshAttachmentList(info.id);
                            displayHardware(info.id);
                        } else if (info.type == NodeInfo.SETUP_NODE) {
                            if (!checkSetupUpdates()) return;
                            setupAttachments.refreshAttachmentList(info.id);
                            displaySetup(info.id);
                        }
                    } else {
                        if (!checkSetupUpdates()) return;
                        displaySetup("-1");
                        setupAttachments.disableAttachmentList();
                        hardwareAttachments.disableAttachmentList();
                        deleteSetupBtn.setEnabled(false);
                        editSetupBtn.setEnabled(false);
                    }
                } else {
                    editSetupBtn.setEnabled(false);
                    deleteSetupBtn.setEnabled(false);
                    setupAttachments.refreshAttachmentList("-1");
                    hardwareAttachments.refreshAttachmentList("-1");
                    displaySetup("-1");
                }
            }
        });
        refreshSetupTree();
    }

    private TreeModel createSetupModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Setups");
        String[] cols = { "setupid", "name", "description" };
        Object[][] setups = db.getColumnValues("setups", cols, "setupID > 0");
        DefaultMutableTreeNode[] setupNodes = new DefaultMutableTreeNode[setups.length];
        for (int i = 0; i < setups.length; i++) {
            Object[] temp = setups[i];
            setupNodes[i] = new DefaultMutableTreeNode(new NodeInfo(temp[1].toString(), temp[0].toString(), temp[2].toString(), NodeInfo.SETUP_NODE));
            setupNodes[i].setAllowsChildren(true);
            root.add(setupNodes[i]);
            Object[][] hardware = db.getColumnValues("hardware h, hardwareRegister hr", new String[] { "h.hardwareID", "h.name" }, "h.hardwareID = hr.hardwareID AND hr.setupID = '" + temp[0] + "'");
            if (hardware != null) {
                for (int j = 0; j < hardware.length; j++) {
                    Object[] temp2 = hardware[j];
                    DefaultMutableTreeNode hardwareNode = new DefaultMutableTreeNode(new NodeInfo(temp2[1].toString(), temp2[0].toString(), NodeInfo.HARDWARE_NODE));
                    hardwareNode.setAllowsChildren(false);
                    setupNodes[i].add(hardwareNode);
                }
            }
        }
        if (root == null) {
            return null;
        }
        return new DefaultTreeModel(root);
    }

    private void refreshSetupTree() {
        Enumeration<TreePath> expanded = null;
        boolean refresh = false;
        if (setupTree.getSelectionCount() > 0) {
            expanded = setupTree.getExpandedDescendants(setupTree.getPathForRow(0));
            refresh = true;
        }
        DefaultTreeModel model = (DefaultTreeModel) createSetupModel();
        model.setAsksAllowsChildren(true);
        setupTree.setModel(model);
        if (refresh) {
            expandTree(setupTree, expanded);
        } else {
            setupTree.setSelectionInterval(-1, -1);
        }
    }

    private void displaySetup(String id) {
        setupDescriptionArea.setEditable(false);
        setupNameTF.setEditable(false);
        if (id == "-1") {
            setupNameTF.setText("");
            setupNameTF.setCaretPosition(0);
            setupDescriptionArea.setText("");
            setupDescriptionArea.setCaretPosition(0);
            setupDescriptionArea.setEditable(false);
            setupNameTF.setEditable(false);
        } else {
            Object[] values = db.getSingleRowValues("setups", new String[] { "name", "description" }, "setupID = '" + id + "'");
            setupNameTF.setText(values[0].toString());
            setupNameTF.setCaretPosition(0);
            setupDescriptionArea.setText(values[1].toString());
            setupDescriptionArea.setCaretPosition(0);
        }
        tabPane.setSelectedIndex(0);
    }

    private boolean requestSetupEdit(String setupID) {
        if (db.existsEntryInTable("experiments", "setupID", setupID)) {
            int ans = JOptionPane.showConfirmDialog(this, "This setup is used (referenced in some experiments)! Edit Original (Yes)?, Make a copy(No)? Or cancel? ", "Waring! Setup in use!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (ans == JOptionPane.NO_OPTION) {
                copySetup(setupID);
                JOptionPane.showMessageDialog(this, "Setup copied. Now select the new one to edit.");
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void newSetupBtnPressed() {
        if (!checkSetupUpdates()) {
            return;
        }
        setupDescriptionArea.setEditable(true);
        setupDescriptionArea.setText("");
        setupNameTF.setEditable(true);
        setupNameTF.setText("");
        submitSetupBtn.setEnabled(true);
        newSetupEntry = true;
        selectedSetupID = "-1";
        setupAttachments.disableAttachmentList();
        tabPane.setSelectedIndex(0);
        setupNameTF.requestFocusInWindow();
    }

    private void editSetupBtnPressed() {
        if (((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).type == NodeInfo.HARDWARE_NODE) {
            if (!checkHardwareUpdates()) {
                return;
            }
            hardwareTree.setSelectionInterval(-1, -1);
            displayHardware(((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id);
            editHardware(((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id);
        } else if (((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).type == NodeInfo.SETUP_NODE) {
            if (!requestSetupEdit(((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id)) {
                return;
            }
            if (!checkSetupUpdates()) {
                return;
            }
            setupDescriptionArea.setEditable(true);
            setupNameTF.setEditable(true);
            deleteSetupBtn.setEnabled(false);
            submitSetupBtn.setEnabled(true);
            setupEntryEdited = true;
            selectedSetupID = ((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id;
            tabPane.setSelectedIndex(0);
            setupNameTF.requestFocusInWindow();
        }
    }

    private boolean checkSetupUpdates() {
        if (setupEntryEdited || newSetupEntry) {
            int ans = JOptionPane.showConfirmDialog(this, "Changes have been made! Submit(Yes) or discard (No)? ", "Unsubmitted changes!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.YES_OPTION) {
                submitSetupBtnPressed();
                return true;
            } else {
                setupEntryEdited = false;
                newSetupEntry = false;
                selectedSetupID = "-1";
                return true;
            }
        } else {
            return true;
        }
    }

    private void submitSetupBtnPressed() {
        Vector<String> values = new Vector<String>();
        values.add(LabDBTextHelpers.textSanitiser(setupNameTF.getText()));
        values.add(LabDBTextHelpers.textSanitiser(setupDescriptionArea.getText()));
        String setupID;
        if (newSetupEntry) {
            setupID = Integer.toString(db.insertNewRow("setups", values, setupColumns));
            newSetupEntry = false;
            editSetupBtn.setEnabled(true);
        } else {
            setupID = selectedSetupID;
            db.updateRow("setups", values, setupColumns, "setupID = '" + setupID + "'");
            setupEntryEdited = false;
            newSetupBtn.setEnabled(true);
        }
        setupNameTF.setEditable(false);
        setupDescriptionArea.setEditable(false);
        submitSetupBtn.setEnabled(false);
        refreshSetupTree();
        setupTree.requestFocusInWindow();
    }

    private boolean copySetup(String originalID) {
        Vector<String> values = new Vector<String>();
        String newName = "";
        do {
            newName = JOptionPane.showInputDialog("Please give a new name:");
            if (newName == null) {
                return false;
            }
        } while (db.existsEntryInTable("setups", "name", newName) || newName.isEmpty());
        values.add(newName);
        values.add(db.getColumnValue("setups", "description", "setupID = '" + originalID + "'").toString());
        int newID = db.insertNewRow("setups", values, new String[] { "name", "description" });
        if (newID < 0) {
            return false;
        }
        Object[] hardwareIDs = db.getColumnValues("hardwareRegister", "hardwareID", "setupID = '" + originalID + "'");
        for (int i = 0; i < hardwareIDs.length; i++) {
            Vector<String> hrValues = new Vector<String>();
            hrValues.add(Integer.toString(newID));
            hrValues.add(hardwareIDs[i].toString());
            db.insertNewRow("hardwareRegister", hrValues, new String[] { "setupID", "hardwareID" });
        }
        refreshSetupTree();
        return true;
    }

    private void deleteFromSetup(String hardwareID, String setupID) {
        if (!requestSetupEdit(setupID)) {
            return;
        }
        db.removeEntryFromTable("hardwareRegister", "hardwareID = '" + hardwareID + "'" + " AND setupID = '" + setupID + "'");
        refreshSetupTree();
    }

    private void deleteSetupBtnPressed() {
        if (((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).type == NodeInfo.HARDWARE_NODE) {
            deleteFromSetup(((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id, ((NodeInfo) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getParent()).getUserObject()).id);
        } else {
            int ans = JOptionPane.showConfirmDialog(this, "Finally remove this setup?", "Confirm deletion!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ans == JOptionPane.NO_OPTION) return;
            deleteSetup(((NodeInfo) ((DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent()).getUserObject()).id);
        }
    }

    private void deleteSetup(String setupID) {
        if (db.existsEntryInTable("experiments", "setupID", setupID)) {
            JOptionPane.showMessageDialog(this, "Setup is used in at least one experiment. Deleting forbidden!");
            return;
        } else {
            db.setTransactionBegin();
            setupAttachments.deleteAllAttachments();
            if (!db.removeEntryFromTable("hardwareRegister", "setupID='" + setupID + "'")) {
                db.setTransactionRollback();
                JOptionPane.showConfirmDialog(this, "An error occurred during delete.", "Error!", JOptionPane.OK_OPTION);
                return;
            }
            if (!db.removeEntryFromTable("setups", "setupID='" + setupID + "'")) {
                db.setTransactionRollback();
                JOptionPane.showConfirmDialog(this, "An error occurred during delete.", "Error!", JOptionPane.OK_OPTION);
                return;
            }
            db.setTransactionCommit();
            refreshSetupTree();
        }
    }

    private void setupHardwareTree() {
        final Component temp = this;
        hardwareTree = new JTree();
        hardwareTree.setToolTipText("select items to view information. Use drag'n'drop to add hardware to setups.");
        hardwareTree.setDragEnabled(true);
        hardwareTree.setTransferHandler(new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) hardwareTree.getLastSelectedPathComponent();
                if (node.getUserObject().getClass() == NodeInfo.class) {
                    NodeInfo tmp = (NodeInfo) node.getUserObject();
                    return tmp;
                } else return null;
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                super.exportDone(source, data, action);
            }
        });
        hardwareTree.addKeyListener(new HardwareTreeKeyListener());
        ToolTipManager.sharedInstance().registerComponent(hardwareTree);
        hardwareTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        hardwareTree.setCellRenderer(new SetupTreeRenderer(setupLeafIcon, hardwareLeafIcon));
        hardwareTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (hardwareTree.getSelectionCount() != 0) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) hardwareTree.getLastSelectedPathComponent();
                    if (node == null) {
                        displayHardware("-1");
                        deleteHardwareBtn.setEnabled(false);
                        editHardwareBtn.setEnabled(false);
                        return;
                    }
                    Object nodeInfo = node.getUserObject();
                    if (node.isLeaf()) {
                        if (node.getLevel() > 1) {
                            NodeInfo info = (NodeInfo) nodeInfo;
                            deleteHardwareBtn.setEnabled(true);
                            editHardwareBtn.setEnabled(true);
                            hardwareAttachments.refreshAttachmentList(info.id);
                            displayHardware(info.id);
                        }
                    } else {
                        displayHardware("-1");
                        hardwareAttachments.disableAttachmentList();
                        deleteHardwareBtn.setEnabled(false);
                        editHardwareBtn.setEnabled(false);
                    }
                } else {
                    editHardwareBtn.setEnabled(false);
                    deleteHardwareBtn.setEnabled(false);
                    hardwareAttachments.refreshAttachmentList("-1");
                    displayHardware("-1");
                }
            }
        });
        refreshHardwareTree();
    }

    private JPanel setHardwareDescriptionPanel() {
        HardwareEditorKeyListener hkl = new HardwareEditorKeyListener();
        hardwareNameTF = new JTextField();
        hardwareNameTF.addKeyListener(hkl);
        hardwareNameTF.setEditable(false);
        hardwarePurposeTF = new JTextField();
        hardwarePurposeTF.addKeyListener(hkl);
        hardwarePurposeTF.setEditable(false);
        hardwareFundingCombo = new JComboBox(db.getColumnValues("funds", "name"));
        hardwareFundingCombo.setEnabled(false);
        hardwareFundingCombo.addKeyListener(hkl);
        hardwareOwnerTF = new JTextField();
        hardwareOwnerTF.addKeyListener(hkl);
        hardwareOwnerTF.setEditable(false);
        hardwareBuilderTF = new JTextField();
        hardwareBuilderTF.addKeyListener(hkl);
        hardwareBuilderTF.setEditable(false);
        hardwareInventoryTF = new JTextField();
        hardwareInventoryTF.addKeyListener(hkl);
        hardwareInventoryTF.setEditable(false);
        Vector<String> statusOptions = db.getEnumerations("hardware", "status");
        hardwareStatusCombo = new JComboBox(statusOptions);
        hardwareStatusCombo.addKeyListener(hkl);
        hardwareStatusCombo.setEditable(false);
        Vector<String> enums = db.getEnumerations("hardware", "category");
        hardwareCategoryCombo = new JComboBox(enums);
        hardwareCategoryCombo.addKeyListener(hkl);
        hardwareCategoryCombo.setEnabled(false);
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setBackground(Color.white);
        propertiesPanel.setLayout(new GridLayout(9, 2, 10, 2));
        propertiesPanel.add(new JLabel("name"));
        propertiesPanel.add(hardwareNameTF);
        propertiesPanel.add(new JLabel("category"));
        propertiesPanel.add(hardwareCategoryCombo);
        propertiesPanel.add(new JLabel("purpose"));
        propertiesPanel.add(hardwarePurposeTF);
        propertiesPanel.add(new JLabel("manufacturer"));
        propertiesPanel.add(hardwareBuilderTF);
        propertiesPanel.add(new JLabel("funding"));
        propertiesPanel.add(hardwareFundingCombo);
        propertiesPanel.add(new JLabel("owner"));
        propertiesPanel.add(hardwareOwnerTF);
        propertiesPanel.add(new JLabel("inventory number"));
        propertiesPanel.add(hardwareInventoryTF);
        propertiesPanel.add(new JLabel("status"));
        propertiesPanel.add(hardwareStatusCombo);
        propertiesPanel.add(new JLabel("description text:"));
        hardwareAttachments = new LabDBAttachments(db, "hardwareAttachments", "hardwareID", Color.white);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.white);
        northPanel.add(propertiesPanel, BorderLayout.CENTER);
        northPanel.add(hardwareAttachments, BorderLayout.EAST);
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBackground(Color.white);
        descriptionPanel.setLayout(new BorderLayout());
        hardwareDescriptionArea = new JTextArea();
        hardwareDescriptionArea.setLineWrap(true);
        hardwareDescriptionArea.setWrapStyleWord(true);
        hardwareDescriptionArea.setEditable(false);
        hardwareDescriptionArea.addKeyListener(hkl);
        JScrollPane hardwareDescrScrollPane = new JScrollPane(hardwareDescriptionArea);
        descriptionPanel.add(hardwareDescrScrollPane, BorderLayout.CENTER);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.white);
        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(descriptionPanel, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(descriptionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel setHardwareTreePanel() {
        setupHardwareTree();
        JScrollPane hardwareTreePane = new JScrollPane(hardwareTree);
        JButton newHardwareBtn = new JButton("new");
        newHardwareBtn.setActionCommand("newHardwareBtn");
        newHardwareBtn.setToolTipText("enter new hardware description");
        newHardwareBtn.addActionListener(al);
        deleteHardwareBtn = new JButton("delete");
        deleteHardwareBtn.setActionCommand("deleteHardwareBtn");
        deleteHardwareBtn.setEnabled(false);
        deleteHardwareBtn.setToolTipText("delete a hardware entry");
        deleteHardwareBtn.addActionListener(al);
        submitHardwareBtn = new JButton("submit");
        submitHardwareBtn.setActionCommand("submitHardwareBtn");
        submitHardwareBtn.setEnabled(true);
        submitHardwareBtn.setEnabled(false);
        submitHardwareBtn.setToolTipText("submit changes to database");
        submitHardwareBtn.addActionListener(al);
        editHardwareBtn = new JButton("edit");
        editHardwareBtn.setActionCommand("editHardwareBtn");
        editHardwareBtn.setEnabled(false);
        editHardwareBtn.setToolTipText("edit this entry");
        editHardwareBtn.addActionListener(al);
        JPanel southPanel = new JPanel(new GridLayout(2, 2));
        southPanel.setBackground(Color.white);
        southPanel.add(newHardwareBtn);
        southPanel.add(editHardwareBtn);
        southPanel.add(deleteHardwareBtn);
        southPanel.add(submitHardwareBtn);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createTitledBorder("known hardware"));
        panel.add(hardwareTreePane, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    private TreeModel createHardwareModel() {
        DefaultMutableTreeNode root = null;
        root = new DefaultMutableTreeNode("Hardware");
        Vector<String> categories = db.getEnumerations("hardware", "category");
        DefaultMutableTreeNode[] hardwareCategoryNodes = new DefaultMutableTreeNode[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            hardwareCategoryNodes[i] = new DefaultMutableTreeNode(categories.get(i));
            hardwareCategoryNodes[i].setAllowsChildren(true);
            root.add(hardwareCategoryNodes[i]);
            String[] cols = { "hardwareId", "name", "description" };
            Object[][] hardwareInfo = db.getColumnValues("hardware", cols, "category = '" + categories.get(i) + "'");
            for (int j = 0; j < hardwareInfo.length; j++) {
                Object[] temp = hardwareInfo[j];
                DefaultMutableTreeNode hardware = new DefaultMutableTreeNode(new NodeInfo(temp[1].toString(), temp[0].toString(), temp[2].toString(), NodeInfo.HARDWARE_NODE));
                hardware.setAllowsChildren(false);
                hardwareCategoryNodes[i].add(hardware);
            }
        }
        if (root == null) {
            return null;
        }
        return new DefaultTreeModel(root);
    }

    private void refreshHardwareTree() {
        Enumeration<TreePath> expanded = null;
        boolean refresh = false;
        if (hardwareTree.getSelectionCount() > 0) {
            expanded = hardwareTree.getExpandedDescendants(hardwareTree.getPathForRow(0));
            refresh = true;
        }
        DefaultTreeModel model = (DefaultTreeModel) createHardwareModel();
        model.setAsksAllowsChildren(true);
        hardwareTree.setModel(model);
        if (refresh) {
            expandTree(hardwareTree, expanded);
        } else {
            hardwareTree.setSelectionInterval(-1, -1);
        }
    }

    private void displayHardware(String id) {
        if (!checkHardwareUpdates()) {
            return;
        }
        submitHardwareBtn.setEnabled(false);
        hardwareStatusCombo.setEnabled(false);
        hardwareFundingCombo.setEnabled(false);
        hardwareBuilderTF.setEditable(false);
        hardwareDescriptionArea.setEditable(false);
        hardwareInventoryTF.setEditable(false);
        hardwareNameTF.setEditable(false);
        hardwareNameTF.setCaretPosition(0);
        hardwareOwnerTF.setEditable(false);
        hardwarePurposeTF.setEditable(false);
        hardwarePurposeTF.setCaretPosition(0);
        hardwareCategoryCombo.setSelectedIndex(-1);
        hardwareCategoryCombo.setEnabled(false);
        if (Integer.valueOf(id) > 0) {
            Object[] values = db.getSingleRowValues("hardware", hardwareColumns, "hardwareID = '" + id + "'");
            hardwareNameTF.setText(values[0].toString());
            hardwareNameTF.setCaretPosition(0);
            hardwareNameTF.setToolTipText(hardwareNameTF.getText());
            hardwareCategoryCombo.setSelectedItem(values[1]);
            hardwarePurposeTF.setText(values[2].toString());
            hardwarePurposeTF.setCaretPosition(0);
            hardwarePurposeTF.setToolTipText(hardwarePurposeTF.getText());
            hardwareBuilderTF.setText(values[3].toString());
            hardwareBuilderTF.setCaretPosition(0);
            hardwareBuilderTF.setToolTipText(hardwareBuilderTF.getText());
            hardwareOwnerTF.setText(values[4].toString());
            hardwareOwnerTF.setCaretPosition(0);
            hardwareOwnerTF.setToolTipText(hardwareOwnerTF.getText());
            hardwareInventoryTF.setText(values[5].toString());
            hardwareDescriptionArea.setText(values[6].toString());
            hardwareDescriptionArea.setCaretPosition(0);
            hardwareStatusCombo.setSelectedItem(values[7].toString());
            hardwareFundingCombo.setSelectedItem(db.getColumnValue("funds", "name", "id = '" + values[8] + "'"));
        } else {
            hardwareNameTF.setText("");
            hardwareCategoryCombo.setSelectedIndex(-1);
            hardwareFundingCombo.setSelectedIndex(-1);
            hardwarePurposeTF.setText("");
            hardwareBuilderTF.setText("");
            hardwareBuilderTF.setCaretPosition(0);
            hardwareOwnerTF.setText("");
            hardwareOwnerTF.setCaretPosition(0);
            hardwareInventoryTF.setText("");
            hardwareDescriptionArea.setText("");
            hardwareDescriptionArea.setCaretPosition(0);
            hardwareStatusCombo.setSelectedIndex(-1);
        }
        tabPane.setSelectedIndex(1);
    }

    private void editHardwareBtnPressed() {
        if (!checkHardwareUpdates()) {
            return;
        }
        editHardware(((NodeInfo) ((DefaultMutableTreeNode) hardwareTree.getLastSelectedPathComponent()).getUserObject()).id);
    }

    private void editHardware(String hardwareID) {
        hardwareStatusCombo.setEnabled(true);
        hardwareFundingCombo.setEnabled(true);
        hardwareBuilderTF.setEditable(true);
        hardwareDescriptionArea.setEditable(true);
        hardwareInventoryTF.setEditable(true);
        hardwareNameTF.setEditable(true);
        hardwareOwnerTF.setEditable(true);
        hardwareCategoryCombo.setEnabled(true);
        hardwarePurposeTF.setEditable(true);
        hardwareDescriptionArea.setEditable(true);
        deleteHardwareBtn.setEnabled(false);
        submitHardwareBtn.setEnabled(true);
        hardwareEntryEdited = true;
        tabPane.setSelectedIndex(1);
        selectedHardwareID = hardwareID;
        hardwareNameTF.requestFocusInWindow();
    }

    private void newHardwareBtnPressed() {
        if (!checkHardwareUpdates()) {
            return;
        }
        hardwareStatusCombo.setEnabled(true);
        hardwareStatusCombo.setSelectedIndex(-1);
        hardwareBuilderTF.setEditable(true);
        hardwareBuilderTF.setText("");
        hardwareDescriptionArea.setEditable(true);
        hardwareDescriptionArea.setText("");
        hardwareInventoryTF.setEditable(true);
        hardwareInventoryTF.setText("");
        hardwareNameTF.setEditable(true);
        hardwareNameTF.setText("");
        hardwareFundingCombo.setEnabled(true);
        hardwareFundingCombo.setSelectedIndex(-1);
        hardwareOwnerTF.setEditable(true);
        hardwareOwnerTF.setText("");
        hardwarePurposeTF.setEditable(true);
        hardwarePurposeTF.setText("");
        hardwareCategoryCombo.setEnabled(true);
        hardwareCategoryCombo.setSelectedIndex(-1);
        submitHardwareBtn.setEnabled(true);
        newHardwareEntry = true;
        hardwareAttachments.disableAttachmentList();
        tabPane.setSelectedIndex(1);
        hardwareNameTF.requestFocusInWindow();
    }

    private void submitHardwareBtnPressed() {
        Vector<String> values = new Vector<String>();
        if (hardwareNameTF.getText().isEmpty()) {
            String ans = JOptionPane.showInputDialog("A new Device must have a name!");
            if (ans == null) {
                return;
            } else {
                values.add(ans);
            }
        } else {
            values.add(LabDBTextHelpers.textSanitiser(hardwareNameTF.getText()));
        }
        if (hardwareCategoryCombo.getSelectedIndex() == -1) {
            values.add("");
        } else {
            values.add(hardwareCategoryCombo.getSelectedItem().toString());
        }
        values.add(LabDBTextHelpers.textSanitiser(hardwarePurposeTF.getText()));
        values.add(LabDBTextHelpers.textSanitiser(hardwareBuilderTF.getText()));
        values.add(LabDBTextHelpers.textSanitiser(hardwareOwnerTF.getText()));
        values.add(LabDBTextHelpers.textSanitiser(hardwareInventoryTF.getText()));
        values.add(LabDBTextHelpers.textSanitiser(hardwareDescriptionArea.getText()));
        if (hardwareStatusCombo.getSelectedIndex() == -1) {
            values.add("");
        } else {
            values.add(hardwareStatusCombo.getSelectedItem().toString());
        }
        if (hardwareFundingCombo.getSelectedIndex() == -1) {
            values.add("");
        } else {
            values.add(db.getColumnValue("funds", "id", "name ='" + hardwareFundingCombo.getSelectedItem() + "'").toString());
        }
        if (newHardwareEntry) {
            db.insertNewRow("hardware", values, hardwareColumns);
            newHardwareEntry = false;
        } else if (hardwareEntryEdited) {
            db.updateRow("hardware", values, hardwareColumns, "hardwareID = '" + selectedHardwareID + "'");
            hardwareEntryEdited = false;
            selectedHardwareID = "-1";
        }
        submitHardwareBtn.setEnabled(false);
        hardwareStatusCombo.setEnabled(false);
        hardwareStatusCombo.setSelectedIndex(-1);
        hardwareBuilderTF.setEditable(false);
        hardwareBuilderTF.setText("");
        hardwareDescriptionArea.setEditable(false);
        hardwareDescriptionArea.setText("");
        hardwareInventoryTF.setEditable(false);
        hardwareInventoryTF.setText("");
        hardwareNameTF.setEditable(false);
        hardwareNameTF.setText("");
        hardwareFundingCombo.setEnabled(false);
        hardwareFundingCombo.setSelectedIndex(-1);
        hardwareOwnerTF.setEditable(false);
        hardwareOwnerTF.setText("");
        hardwarePurposeTF.setEditable(false);
        hardwarePurposeTF.setText("");
        hardwareCategoryCombo.setEnabled(false);
        hardwareCategoryCombo.setSelectedIndex(-1);
        submitHardwareBtn.setEnabled(false);
        refreshHardwareTree();
        refreshSetupTree();
        hardwareTree.requestFocusInWindow();
    }

    private boolean checkHardwareUpdates() {
        if (hardwareEntryEdited || newHardwareEntry) {
            int ans = JOptionPane.showConfirmDialog(this, "Changes have been made! Submit(Yes) or discard (no) changes? ", "Unsubmitted changes!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.YES_OPTION) {
                submitHardwareBtnPressed();
                return true;
            } else {
                hardwareEntryEdited = false;
                newHardwareEntry = false;
                selectedHardwareID = "-1";
                return true;
            }
        } else {
            return true;
        }
    }

    private void deleteHardwareBtnPressed() {
        int ans = JOptionPane.showConfirmDialog(this, "Finally remove this hardware?", "Confirm deletion!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.NO_OPTION) return;
        String hardwareID = ((NodeInfo) ((DefaultMutableTreeNode) hardwareTree.getLastSelectedPathComponent()).getUserObject()).id;
        deleteHardware(hardwareID);
    }

    private void deleteHardware(String hardwareID) {
        if (!db.existsEntryInTable("hardwareRegister", "hardwareID", hardwareID)) {
            hardwareAttachments.deleteAllAttachments();
            db.removeEntryFromTable("hardware", "hardwareID ='" + hardwareID + "'");
            refreshHardwareTree();
        } else {
            JOptionPane.showConfirmDialog(this, "can't delete another setup uses this hardware", "Error!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void expandTree(JTree tree, Enumeration<TreePath> expanded) {
        Vector<Object> oldPaths = sort(expanded);
        for (int i = 0; i < oldPaths.size(); ++i) {
            TreePath element = (TreePath) oldPaths.get(i);
            tree.expandPath(getPathByName(tree, element.toString()));
        }
    }

    public TreePath getPathByName(JTree tree, String name) {
        TreePath[] tp = new TreePath[tree.getRowCount()];
        for (int i = 0; i < tree.getRowCount(); i++) {
            tp[i] = tree.getPathForRow(i);
        }
        for (int i = 0; i < tp.length; i++) {
            if (tp[i].toString().equals(name)) {
                return tp[i];
            }
        }
        return null;
    }

    public Vector<Object> sort(Enumeration<TreePath> expanded) {
        Vector<TreePath> paths = new Vector<TreePath>();
        Vector<Integer> levels = new Vector<Integer>();
        int maxLevel = -1;
        int actualLevel = 0;
        while (expanded.hasMoreElements()) {
            TreePath tp = (TreePath) expanded.nextElement();
            paths.add(tp);
            StringTokenizer st = new StringTokenizer(tp.toString(), ",");
            levels.add(paths.indexOf(tp), new Integer(st.countTokens()));
            if (st.countTokens() > maxLevel) maxLevel = st.countTokens();
        }
        Object[] pathsV = paths.toArray();
        Object[] levelsV = levels.toArray();
        Vector<Object> sorted = new Vector<Object>();
        while (actualLevel <= maxLevel) {
            for (int i = 0; i < levelsV.length; ++i) {
                if (((Integer) levelsV[i]).intValue() == actualLevel) {
                    sorted.add(pathsV[i]);
                }
            }
            actualLevel++;
        }
        return sorted;
    }

    private class NodeInfo implements Transferable {

        public String name;

        public String id;

        public String description;

        public int type;

        private static final int HARDWARE_NODE = 0, SETUP_NODE = 1, ROOT_NODE = 2;

        public NodeInfo(String name, String id, int type) {
            this.name = name;
            this.id = id;
            this.type = type;
            this.description = "";
        }

        public NodeInfo(String name, String id, String descr, int type) {
            this.name = name;
            this.id = id;
            this.type = type;
            this.description = descr;
        }

        public String toString() {
            return name;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return this;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] temp = new DataFlavor[1];
            temp[0] = new DataFlavor(NodeInfo.class, "Hardware or setup nodeInfo");
            return temp;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return false;
        }
    }

    class LabDBSetupsPanelActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("newHardwareBtn")) {
                newHardwareBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("editHardwareBtn")) {
                editHardwareBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("submitHardwareBtn")) {
                submitHardwareBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("deleteHardwareBtn")) {
                deleteHardwareBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("newSetupBtn")) {
                newSetupBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("editSetupBtn")) {
                editSetupBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("deleteSetupBtn")) {
                deleteSetupBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("submitSetupBtn")) {
                submitSetupBtnPressed();
            }
        }
    }

    class SetupTreeRenderer extends DefaultTreeCellRenderer {

        Icon setupIcon, hardwareIcon;

        public SetupTreeRenderer(Icon setupIcon, Icon hardwareIcon) {
            this.setupIcon = setupIcon;
            this.hardwareIcon = hardwareIcon;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                NodeInfo nodeInfo = (NodeInfo) (node.getUserObject());
                String descr = "";
                try {
                    descr = "  -  " + nodeInfo.description.substring(0, 50) + "...";
                } catch (Exception e) {
                    descr = nodeInfo.description.substring(0);
                }
                if (nodeInfo.type == NodeInfo.HARDWARE_NODE) {
                    setIcon(hardwareIcon);
                    setToolTipText(nodeInfo.toString() + descr);
                } else if (nodeInfo.type == NodeInfo.SETUP_NODE) {
                    setIcon(setupIcon);
                    setToolTipText(nodeInfo.toString() + descr);
                } else {
                    setToolTipText(null);
                }
            } catch (Exception e) {
            }
            return this;
        }
    }

    private class HardwareEditorKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = true;
            }
            if (cntrlPressed && e.getKeyCode() == 83) {
                submitHardwareBtnPressed();
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = false;
            }
        }

        ;

        public void keyTyped(KeyEvent e) {
        }
    }

    private class HardwareTreeKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = true;
            }
            if (cntrlPressed) {
                switch(e.getKeyCode()) {
                    case 83:
                        submitHardwareBtnPressed();
                        break;
                    case 78:
                        newHardwareBtnPressed();
                        break;
                    case 69:
                        editHardwareBtnPressed();
                        break;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                DefaultMutableTreeNode temp = (DefaultMutableTreeNode) hardwareTree.getLastSelectedPathComponent();
                if (!(temp.getUserObject().getClass() == NodeInfo.class)) {
                    return;
                }
                if (((NodeInfo) temp.getUserObject()).type == NodeInfo.HARDWARE_NODE) {
                    deleteHardware(((NodeInfo) temp.getUserObject()).id);
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = false;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    private class SetupEditorKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = true;
            }
            if (cntrlPressed && e.getKeyCode() == 83) {
                submitSetupBtnPressed();
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = false;
            }
        }

        ;

        public void keyTyped(KeyEvent e) {
        }
    }

    private class SetupTreeKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = true;
            }
            if (cntrlPressed) {
                switch(e.getKeyCode()) {
                    case 83:
                        submitSetupBtnPressed();
                        break;
                    case 78:
                        newSetupBtnPressed();
                        break;
                    case 69:
                        editSetupBtnPressed();
                        break;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                DefaultMutableTreeNode temp = (DefaultMutableTreeNode) setupTree.getLastSelectedPathComponent();
                if (!(temp.getUserObject().getClass() == NodeInfo.class)) {
                    return;
                }
                if (((NodeInfo) temp.getUserObject()).type == NodeInfo.HARDWARE_NODE) {
                    String setupID = ((NodeInfo) ((DefaultMutableTreeNode) temp.getParent()).getUserObject()).id;
                    deleteFromSetup(((NodeInfo) temp.getUserObject()).id, setupID);
                } else if (((NodeInfo) temp.getUserObject()).type == NodeInfo.SETUP_NODE) {
                    deleteSetup(((NodeInfo) temp.getUserObject()).id);
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 17) {
                cntrlPressed = false;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}
