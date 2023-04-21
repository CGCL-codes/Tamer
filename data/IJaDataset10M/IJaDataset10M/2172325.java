package freets.gui.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.BevelBorder;
import freets.gui.design.*;

/**
 * A GUI component for displaying and modifying global project properties.
 * 
 * @author T. F�rster
 * @version $Revision: 1.1.1.1 $
 */
public class ProjectPropertiesDialog extends JDialog implements ActionListener, CaretListener {

    protected static ResourceBundle res = ProjectWorkSuite.resources;

    private JTabbedPane tabbedPane;

    private JPanel buttonPanel;

    private JPanel generalPanel;

    private JPanel historyPanel;

    private JPanel specialPanel, linePanel, groupHierarchyPanel;

    private JButton okButton, cancelButton, helpButton;

    private JLabel projectNameLabel, projectNumberLabel, contractNumberLabel;

    private JLabel customerAddressLabel, projectAddressLabel, contactAddressLabel;

    private JLabel startDateLabel, endDateLabel;

    private JTextField projectNameField, projectNumberField, contractNumberField;

    private JTextField customerAddressField, projectAddressField, contactAddressField;

    private JTextField startDateField, endDateField;

    private ConfigurableTable historyTable;

    private DefaultTableModel historyTableModel;

    private JPanel historyButtonPanel;

    private JButton historyNewButton, historyChangeButton, historyDeleteButton;

    private JLabel lineNameLabel, lineAddressLabel, systemIDLabel, mediumTypeLabel;

    private JTextField lineNameField, lineAddressField, systemIDField;

    private JComboBox mediumTypeComboBox;

    private JLabel groupHierarchyLabel;

    private JComboBox groupHierarchyComboBox;

    private ProjectDataManager projectDataManager;

    private boolean createNew;

    protected boolean cancelled;

    /**
     * Creates a new ProjectPropertiesDialog for displaying and setting up
     * global project properties.
     * @param dataManager the associated ProjectDataManager
     * @param createNew <i>true</i>, if project shall be started from scratch
     */
    public ProjectPropertiesDialog(ProjectDataManager dataManager, boolean createNew) {
        super();
        this.projectDataManager = dataManager;
        this.createNew = createNew;
        cancelled = true;
        initialize();
        if (!createNew) {
            projectNameField.setText(dataManager.getName());
            projectNumberField.setText(dataManager.getNumber());
            contractNumberField.setText(dataManager.getContractNumber());
            startDateField.setText(dataManager.getStartDate());
            endDateField.setText(dataManager.getEndDate());
            projectAddressField.setText(dataManager.getProjectAddress());
            contactAddressField.setText(dataManager.getContactAddress());
            customerAddressField.setText(dataManager.getCustomerAddress());
            startDateField.setEditable(false);
            groupHierarchyComboBox.setSelectedItem(new Integer(dataManager.getGroupLevels()).toString());
        }
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        startDateField.setText(format.format(new Date()));
        ;
        pack();
    }

    /**
     * Checks if user pressed the cancel-button
     * @return <i>true</i>, if user cancelled dialog
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Setup GUI.
     */
    private void initialize() {
        setModal(true);
        setTitle(res.getString("suite.props.title"));
        tabbedPane = new JTabbedPane();
        okButton = new JButton(res.getString("suite.label.ok"));
        cancelButton = new JButton(res.getString("suite.label.cancel"));
        helpButton = new JButton(res.getString("suite.label.help"));
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        helpButton.addActionListener(this);
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(helpButton);
        if (createNew) okButton.setEnabled(false);
        generalPanel = new JPanel(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.insets = new Insets(10, 5, 10, 5);
        con.anchor = GridBagConstraints.WEST;
        generalPanel.setBorder(BorderFactory.createEtchedBorder());
        projectNameLabel = new JLabel(res.getString("suite.props.projectname"));
        con.gridwidth = 2;
        generalPanel.add(projectNameLabel, con);
        projectNameField = new JTextField(30);
        projectNameField.addCaretListener(this);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(projectNameField, con);
        projectNumberLabel = new JLabel(res.getString("suite.props.projectnumber"));
        con.gridwidth = 2;
        generalPanel.add(projectNumberLabel, con);
        projectNumberField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(projectNumberField, con);
        contractNumberLabel = new JLabel(res.getString("suite.props.contractnumber"));
        con.gridwidth = 2;
        generalPanel.add(contractNumberLabel, con);
        contractNumberField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(contractNumberField, con);
        customerAddressLabel = new JLabel(res.getString("suite.props.customeraddress"));
        con.gridwidth = 2;
        generalPanel.add(customerAddressLabel, con);
        customerAddressField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(customerAddressField, con);
        projectAddressLabel = new JLabel(res.getString("suite.props.projectaddress"));
        con.gridwidth = 2;
        generalPanel.add(projectAddressLabel, con);
        projectAddressField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(projectAddressField, con);
        contactAddressLabel = new JLabel(res.getString("suite.props.contactaddress"));
        con.gridwidth = 2;
        generalPanel.add(contactAddressLabel, con);
        contactAddressField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(contactAddressField, con);
        startDateLabel = new JLabel(res.getString("suite.props.startdate"));
        con.gridwidth = 2;
        generalPanel.add(startDateLabel, con);
        startDateField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(startDateField, con);
        endDateLabel = new JLabel(res.getString("suite.props.enddate"));
        con.gridwidth = 2;
        generalPanel.add(endDateLabel, con);
        endDateField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        generalPanel.add(endDateField, con);
        historyPanel = new JPanel(new BorderLayout());
        historyTableModel = new DefaultTableModel(projectDataManager.getHistory(), Constants.HISTORY.getIds());
        historyTable = new ConfigurableTable(historyTableModel, Constants.HISTORY);
        historyTable.setSortingEnabled(false);
        historyTable.setDefaultRenderer(java.util.Date.class, new DateTableCellRenderer());
        historyPanel.setPreferredSize(new Dimension(300, 100));
        historyButtonPanel = new JPanel(new FlowLayout());
        historyNewButton = new JButton(res.getString("suite.props.history.new"));
        historyChangeButton = new JButton(res.getString("suite.props.history.edit"));
        historyDeleteButton = new JButton(res.getString("suite.props.history.delete"));
        historyNewButton.addActionListener(this);
        historyChangeButton.addActionListener(this);
        historyDeleteButton.addActionListener(this);
        historyButtonPanel.add(historyNewButton);
        historyButtonPanel.add(historyChangeButton);
        historyButtonPanel.add(historyDeleteButton);
        historyPanel.add(new JScrollPane(historyTable), "Center");
        historyPanel.add(historyButtonPanel, "South");
        specialPanel = new JPanel(new GridBagLayout());
        linePanel = new JPanel(new GridBagLayout());
        linePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Area line"));
        lineNameLabel = new JLabel("Name");
        con.gridwidth = 1;
        linePanel.add(lineNameLabel, con);
        lineNameField = new JTextField("backbone");
        lineNameField.setEditable(false);
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = GridBagConstraints.REMAINDER;
        linePanel.add(lineNameField, con);
        lineAddressLabel = new JLabel("Address");
        con.gridwidth = 1;
        con.fill = GridBagConstraints.NONE;
        linePanel.add(lineAddressLabel, con);
        lineAddressField = new JTextField("0");
        lineAddressField.setEditable(false);
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridwidth = GridBagConstraints.REMAINDER;
        linePanel.add(lineAddressField, con);
        systemIDLabel = new JLabel("System ID");
        con.gridwidth = 1;
        con.fill = GridBagConstraints.NONE;
        linePanel.add(systemIDLabel, con);
        systemIDField = new JTextField(30);
        con.gridwidth = GridBagConstraints.REMAINDER;
        linePanel.add(systemIDField, con);
        mediumTypeLabel = new JLabel("Medium type");
        con.gridwidth = 1;
        linePanel.add(mediumTypeLabel, con);
        mediumTypeComboBox = new JComboBox(new String[] { "Twisted Pair", "Power Line" });
        ;
        con.gridwidth = GridBagConstraints.REMAINDER;
        linePanel.add(mediumTypeComboBox, con);
        groupHierarchyPanel = new JPanel(new GridBagLayout());
        groupHierarchyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("suite.props.grouphierarchy")));
        groupHierarchyLabel = new JLabel(res.getString("suite.props.hierarchylevels"));
        con.gridwidth = 1;
        groupHierarchyPanel.add(groupHierarchyLabel, con);
        groupHierarchyComboBox = new JComboBox(new String[] { "2", "3" });
        ;
        con.gridwidth = GridBagConstraints.REMAINDER;
        groupHierarchyPanel.add(groupHierarchyComboBox, con);
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.gridheight = 1;
        specialPanel.add(linePanel, con);
        specialPanel.add(groupHierarchyPanel, con);
        tabbedPane.addTab(res.getString("suite.props.general"), generalPanel);
        tabbedPane.addTab(res.getString("suite.props.history.title"), historyPanel);
        tabbedPane.addTab(res.getString("suite.props.special"), specialPanel);
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(tabbedPane, "Center");
        getContentPane().add(buttonPanel, "South");
    }

    /**
     * An action event has occurred. User has pressed a button
     * @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            cancelled = false;
            endDialog();
        } else if (e.getSource() == helpButton) {
        } else if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == historyNewButton) {
            String text = JOptionPane.showInputDialog(this, res.getString("suite.props.history.enter"), res.getString("suite.props.history.newentry"), JOptionPane.QUESTION_MESSAGE);
            if (text != null) {
                projectDataManager.addHistoryEntry(new Date(), text);
                refreshHistoryTable();
            }
        } else if (e.getSource() == historyDeleteButton) {
            int row = historyTable.getSelectedRow();
            if (row == -1) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                int ans = JOptionPane.showConfirmDialog(this, res.getString("suite.props.history.delentry"), res.getString("suite.props.history.ack"), JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    projectDataManager.deleteHistoryEntry(row);
                    refreshHistoryTable();
                }
            }
        } else if (e.getSource() == historyChangeButton) {
            int row = historyTable.getSelectedRow();
            if (row == -1) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                JOptionPane jop = new JOptionPane(res.getString("suite.props.history.enter"), JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                jop.setInitialValue(projectDataManager.getHistory()[row][1]);
                jop.setWantsInput(true);
                JDialog dialog = jop.createDialog(this, res.getString("suite.props.history.histentry"));
                dialog.show();
                if (jop.getInputValue() != null) {
                    projectDataManager.getHistory()[row][1] = jop.getValue();
                    refreshHistoryTable();
                }
            }
        }
    }

    /**
     * End (close) the dialog. Commit changes to the project data manager.
     */
    private void endDialog() {
        projectDataManager.setName(projectNameField.getText().trim());
        projectDataManager.setNumber(projectNumberField.getText().trim());
        projectDataManager.setContractNumber(contractNumberField.getText().trim());
        projectDataManager.setStartDate(startDateField.getText().trim());
        projectDataManager.setEndDate(endDateField.getText().trim());
        projectDataManager.setCustomerAddress(customerAddressField.getText().trim());
        projectDataManager.setContactAddress(contactAddressField.getText().trim());
        projectDataManager.setProjectAddress(projectAddressField.getText().trim());
        projectDataManager.setGroupLevels(((String) groupHierarchyComboBox.getSelectedItem()).equals("2") ? 2 : 3);
        dispose();
    }

    /**
     * Redisplay the history table.
     */
    private void refreshHistoryTable() {
        for (int i = historyTable.getRowCount() - 1; i >= 0; i--) {
            historyTableModel.removeRow(i);
        }
        for (int i = 0; i < projectDataManager.getHistory().length; i++) {
            historyTableModel.addRow(projectDataManager.getHistory()[i]);
        }
    }

    /**
     * Enable/disable OK-button, if user has entered/not entered project name
     */
    public void caretUpdate(CaretEvent e) {
        if (projectNameField.getText().length() == 0) {
            okButton.setEnabled(false);
        } else {
            okButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new ProjectPropertiesDialog(null, true).show();
    }
}
