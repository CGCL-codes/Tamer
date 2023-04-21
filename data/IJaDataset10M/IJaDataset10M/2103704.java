package gov.sns.apps.jeri.apps.dbdimport;

import gov.sns.apps.jeri.apps.dbimport.ImportResultsTreeCellRenderer;
import gov.sns.apps.jeri.apps.dbimport.ImportResultsTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import gov.sns.apps.jeri.data.SignalFieldType;
import gov.sns.apps.jeri.data.SignalFieldMenu;
import gov.sns.apps.jeri.data.EpicsRecordType;
import gov.sns.apps.jeri.application.JeriDialog;

public class DBDImportResultsDialog extends JeriDialog {

    private JSplitPane splitPane = new JSplitPane();

    private BorderLayout dialogLayout = new BorderLayout();

    private JPanel outerButtonPanel = new JPanel();

    private JPanel innerButtonPanel = new JPanel();

    private BorderLayout outerButtonPanelLayout = new BorderLayout();

    private JButton okButton = new JButton();

    private JButton cancelButton = new JButton();

    private GridLayout innerButtonPanelLayout = new GridLayout();

    private JScrollPane fileScrollPane = new JScrollPane();

    private JScrollPane databaseScrollPane = new JScrollPane();

    private JTree databaseTree = new JTree();

    private JTree fileTree = new JTree();

    private JTextArea errorTextArea = new JTextArea();

    /**
   * <CODE>int</CODE> returned by the <CODE>getResult</CODE> method if ok was 
   * clicked to close the dialog.
   */
    public static final int OK = 1;

    /**
   * <CODE>int</CODE> returned by the <CODE>getResult</CODE> method if cancel 
   * was clicked to close the dialog.
   */
    public static final int CANCEL = 0;

    /**
   * Holds the value of the result property.
   */
    private int result = CANCEL;

    private ImportResultsTreeCellRenderer fileRenderer = new ImportResultsTreeCellRenderer();

    private ImportResultsTreeCellRenderer databaseRenderer = new ImportResultsTreeCellRenderer();

    private JPanel optionPanel = new JPanel();

    private JCheckBox removeMenusCheckBox = new JCheckBox();

    public DBDImportResultsDialog() {
        this(null, "", false);
    }

    public DBDImportResultsDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            fileRenderer.setClosedIcon(null);
            fileRenderer.setOpenIcon(null);
            fileRenderer.setLeafIcon(null);
            fileTree.setCellRenderer(fileRenderer);
            fileTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

                public void valueChanged(TreeSelectionEvent e) {
                    fileTree_selectionChanged(e);
                }
            });
            databaseRenderer.setClosedIcon(null);
            databaseRenderer.setOpenIcon(null);
            databaseRenderer.setLeafIcon(null);
            databaseTree.setCellRenderer(databaseRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 300));
        this.getContentPane().setLayout(dialogLayout);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        dialogLayout.setVgap(5);
        outerButtonPanel.setLayout(outerButtonPanelLayout);
        outerButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        innerButtonPanel.setLayout(innerButtonPanelLayout);
        outerButtonPanelLayout.setVgap(5);
        okButton.setText("OK");
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        innerButtonPanelLayout.setHgap(5);
        fileScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                fileScrollPane_componentResized(e);
            }
        });
        errorTextArea.setEditable(false);
        errorTextArea.setOpaque(false);
        removeMenusCheckBox.setText("Remove Existing Menu Items");
        removeMenusCheckBox.setMnemonic('R');
        fileScrollPane.getViewport().add(fileTree, null);
        splitPane.add(fileScrollPane, JSplitPane.TOP);
        databaseScrollPane.getViewport().add(databaseTree, null);
        splitPane.add(databaseScrollPane, JSplitPane.BOTTOM);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        innerButtonPanel.add(okButton, null);
        innerButtonPanel.add(cancelButton, null);
        outerButtonPanel.add(innerButtonPanel, BorderLayout.EAST);
        outerButtonPanel.add(errorTextArea, BorderLayout.NORTH);
        optionPanel.add(removeMenusCheckBox, null);
        outerButtonPanel.add(optionPanel, BorderLayout.CENTER);
        this.getContentPane().add(outerButtonPanel, BorderLayout.SOUTH);
    }

    public void setFileData(ArrayList fileData, ArrayList invalidFieldTypes) {
        ImportResultsTreeNode root = new ImportResultsTreeNode("Data From Files");
        insertDataIntoRoot(root, fileData, invalidFieldTypes);
        fileTree.setModel(new DefaultTreeModel(root));
    }

    public void setDatabaseData(ArrayList databaseData) {
        ImportResultsTreeNode root = new ImportResultsTreeNode("Data From RDB");
        insertDataIntoRoot(root, databaseData, null);
        databaseTree.setModel(new DefaultTreeModel(root));
    }

    private void insertDataIntoRoot(ImportResultsTreeNode root, ArrayList data, ArrayList invalidFieldTypes) {
        Iterator recordIterator = data.iterator();
        while (recordIterator.hasNext()) {
            Object currentRecord = recordIterator.next();
            if (currentRecord instanceof SignalFieldMenu) {
                SignalFieldMenu currentMenu = (SignalFieldMenu) currentRecord;
                ImportResultsTreeNode currentNode = new ImportResultsTreeNode("Menu: " + currentMenu.getID());
                if (!currentMenu.isInDatabase()) currentNode.setDefaultColor(Color.blue);
                int menuItemCount = currentMenu.getSize();
                for (int i = 0; i < menuItemCount; i++) {
                    String currentMenuItem = currentMenu.getMenuItemAt(i);
                    ImportResultsTreeNode menuItemNode = new ImportResultsTreeNode(currentMenuItem);
                    if (!currentMenu.isMenuItemInDatabase(i)) menuItemNode.setDefaultColor(Color.blue);
                    currentNode.add(menuItemNode);
                }
                root.add(currentNode);
            } else if (currentRecord instanceof SignalFieldType) {
                SignalFieldType currentSignalFieldType = (SignalFieldType) currentRecord;
                ImportResultsTreeNode currentNode = new ImportResultsTreeNode("Field: " + currentSignalFieldType.getID() + " Record Type: " + currentSignalFieldType.getRecordType().getID());
                if (!currentSignalFieldType.isInDatabase()) currentNode.setDefaultColor(Color.blue);
                SignalFieldMenu menu = currentSignalFieldType.getMenu();
                if (menu != null) currentNode.add(new ImportResultsTreeNode("Menu: " + menu.getID()));
                String value = currentSignalFieldType.getEpicsFieldTypeID();
                if (value != null) currentNode.add(new ImportResultsTreeNode("Field Type ID: " + value));
                currentNode.add(new ImportResultsTreeNode("Prompt Order: " + currentSignalFieldType.getPromptOrder()));
                value = currentSignalFieldType.getDescription();
                if (value != null) currentNode.add(new ImportResultsTreeNode("Description: " + value));
                value = currentSignalFieldType.getInitial();
                if (value != null) currentNode.add(new ImportResultsTreeNode("Initial: " + value));
                value = currentSignalFieldType.getPromptGroup();
                if (value != null) currentNode.add(new ImportResultsTreeNode("Prompt Group: " + value));
                if (invalidFieldTypes != null && !currentSignalFieldType.isInDatabase()) {
                    int invalidFieldTypeCount = invalidFieldTypes.size();
                    for (int i = 0; i < invalidFieldTypeCount; i++) {
                        String currentRecordTypeID = currentSignalFieldType.getRecordType().getID();
                        if (invalidFieldTypes.get(i).toString().equals(currentRecordTypeID)) {
                            currentNode.setErrorMessage("Record type '" + currentRecordTypeID + "' is invalid.");
                            break;
                        }
                    }
                }
                root.add(currentNode);
            } else if (currentRecord instanceof EpicsRecordType) {
                EpicsRecordType currentRecordType = (EpicsRecordType) currentRecord;
                ImportResultsTreeNode currentNode = new ImportResultsTreeNode("Record Type: " + currentRecordType.getID());
                if (!currentRecordType.isInDatabase()) currentNode.setDefaultColor(Color.blue);
                currentNode.add(new ImportResultsTreeNode(currentRecordType.getCode()));
                currentNode.add(new ImportResultsTreeNode(currentRecordType.getDescription()));
                root.add(currentNode);
            }
        }
    }

    /**
   * Returns an <CODE>int</CODE> used to determine which button the dialog was
   * closed with.
   *
   * @return Returns <CODE>COMMIT</CODE> if the commit button was clicked, returns <CODE>CANCEL</CODE> otherwise.
   */
    public int getResult() {
        return result;
    }

    private void this_windowClosing(WindowEvent e) {
        result = CANCEL;
    }

    private void okButton_actionPerformed(ActionEvent e) {
        result = OK;
        setVisible(false);
    }

    private void cancelButton_actionPerformed(ActionEvent e) {
        result = CANCEL;
        setVisible(false);
    }

    public boolean isRemoveExistingMenus() {
        return removeMenusCheckBox.isSelected();
    }

    private void fileTree_selectionChanged(TreeSelectionEvent e) {
        TreePath selectedPath = e.getNewLeadSelectionPath();
        if (selectedPath != null) {
            ImportResultsTreeNode selectedNode = (ImportResultsTreeNode) selectedPath.getLastPathComponent();
            String errorMessage = selectedNode.getErrorMessage();
            if (errorMessage == null || errorMessage.trim().equals("")) errorTextArea.setText(selectedNode.getWarningMessage()); else errorTextArea.setText(errorMessage);
        }
    }

    @Override
    public void setApplicationProperties(Properties applicationProperties) {
        String locationString = applicationProperties.getProperty("dbdImportResultsDialog.divider", "175");
        splitPane.setDividerLocation(Integer.parseInt(locationString));
        super.setApplicationProperties(applicationProperties);
    }

    /**
   * Called when the splitter is moved. This method records it's new position in 
   * the applications properties file.
   * 
   * @param e The <CODE>ComponentEvent</CODE> that caused the invocation of this method.
   */
    private void fileScrollPane_componentResized(ComponentEvent e) {
        String location = String.valueOf(splitPane.getDividerLocation());
        getApplicationProperties().setProperty("dbdImportResultsDialog.divider", location);
    }
}
