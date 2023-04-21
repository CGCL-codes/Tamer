package org.pointrel.pointrel20090201.examples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.pointrel.pointrel20090201.core.Query;
import org.pointrel.pointrel20090201.core.ResultSet;
import org.pointrel.pointrel20090201.core.Session;
import org.pointrel.pointrel20090201.core.Triple;
import org.pointrel.pointrel20090201.core.TypeAndDataPair;
import org.pointrel.pointrel20090201.configuration.AuthorUtilities;
import org.pointrel.pointrel20090201.configuration.LicenseUtilities;
import org.pointrel.pointrel20090201.configuration.SessionConfigurationChooser;
import org.pointrel.pointrel20090201.configuration.VersionsListViewer;

public class SimpleToDoListApplication extends JFrame {

    class TripleWrapper {

        public Triple triple;

        TripleWrapper(Triple triple) {
            this.triple = triple;
        }

        public String toString() {
            return triple.valueData;
        }
    }

    private static final long serialVersionUID = 1L;

    private JPanel mainContentPane = null;

    private JPanel addPanel = null;

    private JList itemsList = null;

    private JButton addButton = null;

    private JTextField addTextField = null;

    private JButton reloadButton = null;

    private JScrollPane itemsListScrollPane = null;

    private JTextArea descriptionTextArea = null;

    private JPanel descriptionPanel = null;

    private JLabel descriptionLabel = null;

    private JButton saveButton = null;

    private JScrollPane descriptionTextAreaScrollPane = null;

    private JPanel bottomPanel = null;

    private JButton authorsButton = null;

    private JButton licensesButton = null;

    private JPanel saveAndVersionsPanel = null;

    private JButton versionsButton = null;

    private JPanel itemsPanel = null;

    private JLabel itemsLabel = null;

    private JButton deleteButton = null;

    private DefaultListModel listModel;

    private JCheckBox showDeletedItemsCheckBox = null;

    private JCheckBox deleteItemCheckBox = null;

    String contextTypeToUse = "org.pointrel.pointrel20090201.examples.SimpleToDoList";

    Session session;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Session session = SessionConfigurationChooser.askUserForSessionDetails();
                if (session == null) System.exit(0);
                SimpleToDoListApplication thisClass = new SimpleToDoListApplication(session);
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    public SimpleToDoListApplication(Session session) {
        super();
        this.session = session;
        listModel = new DefaultListModel();
        initialize();
        reloadList();
    }

    private void initialize() {
        this.setSize(418, 271);
        this.setContentPane(getMainContentPane());
        this.setTitle("Simple To Do List");
    }

    private void reloadList() {
        listModel.clear();
        Query query = session.queryForMatchingTriples(contextTypeToUse, "test", null, "todolist", null, "entry", null, null, !this.showDeletedItemsCheckBox.getModel().isSelected());
        Iterator<Triple> iterator = query.getMatchingTriples().iterator();
        while (iterator.hasNext()) {
            Triple triple = iterator.next();
            listModel.addElement(new TripleWrapper(triple));
        }
    }

    private JPanel getMainContentPane() {
        if (mainContentPane == null) {
            mainContentPane = new JPanel();
            mainContentPane.setLayout(new BorderLayout());
            mainContentPane.add(getAddPanel(), BorderLayout.NORTH);
            mainContentPane.add(getDescriptionPanel(), BorderLayout.EAST);
            mainContentPane.add(getBottomPanel(), BorderLayout.SOUTH);
            mainContentPane.add(getItemsPanel(), BorderLayout.CENTER);
        }
        return mainContentPane;
    }

    private JPanel getAddPanel() {
        if (addPanel == null) {
            addPanel = new JPanel();
            addPanel.setLayout(new BorderLayout());
            addPanel.add(getAddButton(), BorderLayout.EAST);
            addPanel.add(getAddTextField(), BorderLayout.CENTER);
        }
        return addPanel;
    }

    private JButton getAddButton() {
        if (addButton == null) {
            addButton = new JButton();
            addButton.setText("Add");
            addButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addPressed();
                }
            });
        }
        return addButton;
    }

    private void addPressed() {
        String newItem = addTextField.getText();
        if (newItem.equals("")) return;
        Triple triple = session.addTripleForFields(contextTypeToUse, "test", "", "todolist", "", "entry", "", newItem);
        listModel.addElement(new TripleWrapper(triple));
        addTextField.setText("");
    }

    private JTextField getAddTextField() {
        if (addTextField == null) {
            addTextField = new JTextField();
        }
        return addTextField;
    }

    private JButton getReloadButton() {
        if (reloadButton == null) {
            reloadButton = new JButton();
            reloadButton.setText("Reload");
            reloadButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reloadPressed();
                }
            });
        }
        return reloadButton;
    }

    protected void reloadPressed() {
        session.reloadArchives();
        reloadList();
    }

    private JTextArea getDescriptionTextArea() {
        if (descriptionTextArea == null) {
            descriptionTextArea = new JTextArea();
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setSize(new Dimension(185, 81));
            descriptionTextArea.setWrapStyleWord(true);
        }
        return descriptionTextArea;
    }

    private JPanel getDescriptionPanel() {
        if (descriptionPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.ipadx = 5;
            gridBagConstraints5.ipady = 5;
            gridBagConstraints5.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            descriptionLabel = new JLabel();
            descriptionLabel.setText("Description");
            descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new GridBagLayout());
            descriptionPanel.add(descriptionLabel, gridBagConstraints1);
            descriptionPanel.add(getDescriptionTextAreaScrollPane(), gridBagConstraints5);
            descriptionPanel.add(getSaveAndVersionsPanel(), gridBagConstraints3);
        }
        return descriptionPanel;
    }

    private JScrollPane getDescriptionTextAreaScrollPane() {
        if (descriptionTextAreaScrollPane == null) {
            descriptionTextAreaScrollPane = new JScrollPane();
            descriptionTextAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            descriptionTextAreaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            descriptionTextAreaScrollPane.setPreferredSize(new Dimension(180, 80));
            descriptionTextAreaScrollPane.setViewportView(getDescriptionTextArea());
        }
        return descriptionTextAreaScrollPane;
    }

    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(getReloadButton(), null);
            bottomPanel.add(getAuthorsButton(), null);
            bottomPanel.add(getLicensesButton(), null);
        }
        return bottomPanel;
    }

    private JButton getAuthorsButton() {
        if (authorsButton == null) {
            authorsButton = new JButton();
            authorsButton.setText("Authors...");
            authorsButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AuthorUtilities.editAuthorsList(SimpleToDoListApplication.this, session);
                }
            });
        }
        return authorsButton;
    }

    private JButton getLicensesButton() {
        if (licensesButton == null) {
            licensesButton = new JButton();
            licensesButton.setText("Licenses...");
            licensesButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    LicenseUtilities.editLicenseList(SimpleToDoListApplication.this, session);
                }
            });
        }
        return licensesButton;
    }

    private JPanel getItemsPanel() {
        if (itemsPanel == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 5;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridy = 4;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridy = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.weighty = 1.0;
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.weightx = 1.0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 3;
            itemsLabel = new JLabel();
            itemsLabel.setText("Items");
            itemsPanel = new JPanel();
            itemsPanel.setLayout(new GridBagLayout());
            itemsPanel.add(itemsLabel, gridBagConstraints7);
            itemsPanel.add(getItemsListScrollPane(), gridBagConstraints6);
            itemsPanel.add(getDeleteButton(), gridBagConstraints4);
            itemsPanel.add(getShowDeletedItemsCheckBox(), gridBagConstraints8);
            itemsPanel.add(getDeleteItemCheckBox(), gridBagConstraints9);
        }
        return itemsPanel;
    }

    private JScrollPane getItemsListScrollPane() {
        if (itemsListScrollPane == null) {
            itemsListScrollPane = new JScrollPane();
            itemsListScrollPane.setViewportView(getItemsList());
        }
        return itemsListScrollPane;
    }

    private JList getItemsList() {
        if (itemsList == null) {
            itemsList = new JList();
            itemsList.setModel(listModel);
            itemsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    listItemSelected();
                }
            });
        }
        return itemsList;
    }

    private void listItemSelected() {
        String newText = "";
        boolean isDeleted = false;
        TripleWrapper selectedItem = getSelectedItem();
        if (selectedItem != null) {
            Query query = makeQueryForSelectedItemDescriptions(selectedItem.triple.valueData);
            Triple mostRecentDescriptionTriple = query.getMostRecentMatchingTriple();
            if (mostRecentDescriptionTriple != null) {
                newText = mostRecentDescriptionTriple.valueData;
            }
            isDeleted = session.isTripleDeleted(selectedItem.triple);
        }
        descriptionTextArea.setText(newText);
        this.deleteItemCheckBox.setSelected(isDeleted);
        if (isDeleted) this.deleteButton.setText("Undelete"); else this.deleteButton.setText("Delete");
    }

    private JCheckBox getShowDeletedItemsCheckBox() {
        if (showDeletedItemsCheckBox == null) {
            showDeletedItemsCheckBox = new JCheckBox();
            showDeletedItemsCheckBox.setText("Show deleted items");
            showDeletedItemsCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    showDeletedItemsChanged();
                }
            });
        }
        return showDeletedItemsCheckBox;
    }

    protected void showDeletedItemsChanged() {
        this.reloadList();
    }

    private JCheckBox getDeleteItemCheckBox() {
        if (deleteItemCheckBox == null) {
            deleteItemCheckBox = new JCheckBox();
            deleteItemCheckBox.setText("deleted item");
            deleteItemCheckBox.setEnabled(false);
        }
        return deleteItemCheckBox;
    }

    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton();
            deleteButton.setText("Delete");
            deleteButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    deletePressed();
                }
            });
        }
        return deleteButton;
    }

    protected void deletePressed() {
        TripleWrapper tripleWrapper = getSelectedItem();
        if (tripleWrapper == null) return;
        if (session.isTripleDeleted(tripleWrapper.triple)) {
            session.undeleteTriple(tripleWrapper.triple);
            this.deleteItemCheckBox.setSelected(false);
        } else {
            if (!this.showDeletedItemsCheckBox.isSelected()) listModel.removeElement(tripleWrapper); else this.deleteItemCheckBox.setSelected(true);
            session.deleteTriple(tripleWrapper.triple);
        }
    }

    private JPanel getSaveAndVersionsPanel() {
        if (saveAndVersionsPanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            saveAndVersionsPanel = new JPanel();
            saveAndVersionsPanel.setLayout(new GridBagLayout());
            saveAndVersionsPanel.add(getSaveButton(), gridBagConstraints);
            saveAndVersionsPanel.add(getVersionsButton(), gridBagConstraints2);
        }
        return saveAndVersionsPanel;
    }

    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton();
            saveButton.setText("Save");
            saveButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    savePressed();
                }
            });
        }
        return saveButton;
    }

    private void savePressed() {
        session.addTripleForFields(contextTypeToUse, "test", "", getSelectedItemValue(), "", "description", "", descriptionTextArea.getText());
    }

    private JButton getVersionsButton() {
        if (versionsButton == null) {
            versionsButton = new JButton();
            versionsButton.setText("Versions...");
            versionsButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    versionsPressed();
                }
            });
        }
        return versionsButton;
    }

    protected void versionsPressed() {
        TreeMap<String, TypeAndDataPair> treeMap = new TreeMap<String, TypeAndDataPair>();
        String selectedItem = getSelectedItemValue();
        if (selectedItem != null && !selectedItem.equals("")) {
            Query query = makeQueryForSelectedItemDescriptions(selectedItem);
            ResultSet list = query.getMatchingTriplesSortedByTimestamp();
            if (list.size() == 0) return;
            Iterator<Triple> iterator = list.iterator();
            while (iterator.hasNext()) {
                Triple triple = iterator.next();
                String timestamp = session.getTimeStampForTriple(triple);
                treeMap.put(timestamp, new TypeAndDataPair(triple.valueType, triple.valueData));
            }
            VersionsListViewer versionsListViewer = new VersionsListViewer(treeMap);
            versionsListViewer.setVisible(true);
        }
    }

    private TripleWrapper getSelectedItem() {
        TripleWrapper tripleWrapper = ((TripleWrapper) itemsList.getSelectedValue());
        return tripleWrapper;
    }

    private String getSelectedItemValue() {
        TripleWrapper tripleWrapper = ((TripleWrapper) itemsList.getSelectedValue());
        if (tripleWrapper != null) return tripleWrapper.triple.valueData;
        return "";
    }

    private Query makeQueryForSelectedItemDescriptions(String selectedItem) {
        Query query = session.queryForMatchingTriples(contextTypeToUse, "test", null, selectedItem, null, "description", null, null, true);
        return query;
    }
}
