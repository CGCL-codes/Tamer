package es.eucm.eadventure.editor.gui.elementpanels.assessment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.config.SCORMConfigData;
import es.eucm.eadventure.editor.control.controllers.assessment.AssessmentRuleDataControl;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;

/**
 * This class is the panel used to display and edit nodes. It holds node
 * operations, like adding and removing lines, editing end effects, remove links
 * and reposition lines and children
 */
public class AssessmentPropertiesPanel extends JPanel implements Updateable {

    /**
     * Required
     */
    private static final long serialVersionUID = 1L;

    /**
     * Assessment rule data controller.
     */
    private AssessmentRuleDataControl assessmentRuleDataControl;

    /**
     * Border of the panel
     */
    private TitledBorder border;

    /**
     * Table in which the node lines are represented
     */
    private JTable propertiesTable;

    /**
     * Scroll panel that holds the table
     */
    private JScrollPane tableScrollPanel;

    /**
     * Move property up ( /\ ) button
     */
    private JButton movePropertyUpButton;

    /**
     * Move property down ( \/ ) button
     */
    private JButton movePropertyDownButton;

    /**
     * "Insert property" button
     */
    private JButton insertPropertyButton;

    /**
     * "Delete property" button
     */
    private JButton deletePropertyButton;

    private int currentIndex;

    /**
     * Constructor
     * 
     * @param assessmentRuleDataControl
     *            Data controller to edit the lines
     * 
     * @param scorm12
     *            Show if it is a Scorm 1.2 profile, to take its data model
     * 
     * @param scorm2004
     *            Show if it is a Scorm 2004 profile, to take its data model
     */
    public AssessmentPropertiesPanel(AssessmentRuleDataControl assDataControl, boolean scorm12, boolean scorm2004) {
        this.assessmentRuleDataControl = assDataControl;
        border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), TC.get("AssessmentRule.Effect.PropertiesTitle"), TitledBorder.CENTER, TitledBorder.TOP);
        setBorder(border);
        setLayout(new BorderLayout());
        propertiesTable = new JTable(new NodeTableModel());
        propertiesTable.setAutoCreateColumnsFromModel(false);
        propertiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                propertiesTable.setRowHeight(22);
                if (propertiesTable.getSelectedRow() != -1) propertiesTable.setRowHeight(propertiesTable.getSelectedRow(), 32);
            }
        });
        propertiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        propertiesTable.setCellSelectionEnabled(false);
        propertiesTable.setColumnSelectionAllowed(false);
        propertiesTable.setRowSelectionAllowed(true);
        if (scorm12) {
            JComboBox actionValuesCB = new JComboBox(SCORMConfigData.getPartsOfModel12(SCORMConfigData.WRITE).toArray());
            propertiesTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(actionValuesCB));
        }
        if (scorm2004) {
            JComboBox actionValuesCB = new JComboBox(SCORMConfigData.getPartsOfModel2004(SCORMConfigData.WRITE).toArray());
            propertiesTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(actionValuesCB));
        }
        propertiesTable.getColumnModel().getColumn(1).setCellEditor(new AssessmentPropertyCellRenderer());
        propertiesTable.getColumnModel().getColumn(1).setCellRenderer(new AssessmentPropertyCellRenderer());
        propertiesTable.setIntercellSpacing(new Dimension(1, 1));
        propertiesTable.getSelectionModel().addListSelectionListener(new NodeTableSelectionListener());
        tableScrollPanel = new JScrollPane(propertiesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        movePropertyUpButton = new JButton(new ImageIcon("img/icons/moveNodeUp.png"));
        movePropertyUpButton.setContentAreaFilled(false);
        movePropertyUpButton.setMargin(new Insets(0, 0, 0, 0));
        movePropertyUpButton.setBorder(BorderFactory.createEmptyBorder());
        movePropertyUpButton.setToolTipText(TC.get("AssessmentProperties.MoveUp"));
        movePropertyUpButton.addActionListener(new ListenerButtonMoveLineUp());
        movePropertyUpButton.setEnabled(false);
        movePropertyDownButton = new JButton(new ImageIcon("img/icons/moveNodeDown.png"));
        movePropertyDownButton.setContentAreaFilled(false);
        movePropertyDownButton.setMargin(new Insets(0, 0, 0, 0));
        movePropertyDownButton.setBorder(BorderFactory.createEmptyBorder());
        movePropertyDownButton.setToolTipText(TC.get("AssessmentProperties.MoveDown"));
        movePropertyDownButton.addActionListener(new ListenerButtonMoveLineDown());
        movePropertyDownButton.setEnabled(false);
        insertPropertyButton = new JButton(new ImageIcon("img/icons/addNode.png"));
        insertPropertyButton.setContentAreaFilled(false);
        insertPropertyButton.setMargin(new Insets(0, 0, 0, 0));
        insertPropertyButton.setBorder(BorderFactory.createEmptyBorder());
        insertPropertyButton.setToolTipText(TC.get("AssessmentProperties.InsertProperty"));
        insertPropertyButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                JPopupMenu menu = getAddChildPopupMenu();
                menu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        });
        deletePropertyButton = new JButton(new ImageIcon("img/icons/deleteNode.png"));
        deletePropertyButton.setContentAreaFilled(false);
        deletePropertyButton.setMargin(new Insets(0, 0, 0, 0));
        deletePropertyButton.setBorder(BorderFactory.createEmptyBorder());
        deletePropertyButton.setToolTipText(TC.get("AssessmentProperties.DeleteProperty"));
        deletePropertyButton.addActionListener(new ListenerButtonDeleteLine());
        addComponents();
    }

    /**
     * Returns a popup menu with the add operations.
     * 
     * @return Popup menu with child adding operations
     */
    public JPopupMenu getAddChildPopupMenu() {
        JPopupMenu addChildPopupMenu = new JPopupMenu();
        JMenuItem addChildMenuItem = new JMenuItem("Add value property");
        addChildMenuItem.setEnabled(true);
        addChildMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedRow = propertiesTable.getSelectedRow();
                if (selectedRow == -1) selectedRow = propertiesTable.getRowCount() - 1;
                if (assessmentRuleDataControl.addBlankProperty(selectedRow + 1, currentIndex)) {
                    propertiesTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                    propertiesTable.scrollRectToVisible(propertiesTable.getCellRect(selectedRow + 1, 0, true));
                    propertiesTable.revalidate();
                }
            }
        });
        addChildPopupMenu.add(addChildMenuItem);
        addChildMenuItem = new JMenuItem("Add var/flag property");
        addChildMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedRow = propertiesTable.getSelectedRow();
                if (selectedRow == -1) selectedRow = propertiesTable.getRowCount() - 1;
                String varName = "No flags/vars";
                if (Controller.getInstance().getVarFlagSummary().getFlagCount() != 0) varName = Controller.getInstance().getVarFlagSummary().getFlag(0); else if (Controller.getInstance().getVarFlagSummary().getVarCount() != 0) varName = Controller.getInstance().getVarFlagSummary().getVar(0);
                if (assessmentRuleDataControl.addBlankProperty(selectedRow + 1, currentIndex, varName)) {
                    propertiesTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                    propertiesTable.scrollRectToVisible(propertiesTable.getCellRect(selectedRow + 1, 0, true));
                    propertiesTable.revalidate();
                }
            }
        });
        addChildPopupMenu.add(addChildMenuItem);
        return addChildPopupMenu;
    }

    /**
     * Removes all elements in the panel, and sets a dialogue node panel
     */
    private void addComponents() {
        removeAll();
        movePropertyUpButton.setEnabled(false);
        movePropertyDownButton.setEnabled(false);
        deletePropertyButton.setEnabled(false);
        add(tableScrollPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add(insertPropertyButton, c);
        c.gridy = 1;
        buttonsPanel.add(movePropertyUpButton, c);
        c.gridy = 2;
        buttonsPanel.add(movePropertyDownButton, c);
        c.gridy = 4;
        buttonsPanel.add(deletePropertyButton, c);
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 2.0;
        buttonsPanel.add(new JFiller(), c);
        add(buttonsPanel, BorderLayout.EAST);
    }

    /**
     * Listener for the move line up ( /\ ) button
     */
    private class ListenerButtonMoveLineUp implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int selectedRow = propertiesTable.getSelectedRow();
            if (assessmentRuleDataControl.movePropertyUp(selectedRow, currentIndex)) {
                propertiesTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                propertiesTable.scrollRectToVisible(propertiesTable.getCellRect(selectedRow - 1, 0, true));
            }
        }
    }

    /**
     * Listener for the move line down ( \/ ) button
     */
    private class ListenerButtonMoveLineDown implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int selectedRow = propertiesTable.getSelectedRow();
            if (assessmentRuleDataControl.movePropertyDown(selectedRow, currentIndex)) {
                propertiesTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                propertiesTable.scrollRectToVisible(propertiesTable.getCellRect(selectedRow + 1, 0, true));
            }
        }
    }

    /**
     * Listener for the "Insert property" button
     */
    private class ListenerButtonInsertLine implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int selectedRow = propertiesTable.getSelectedRow();
            if (selectedRow == -1) selectedRow = propertiesTable.getRowCount() - 1;
            if (assessmentRuleDataControl.addBlankProperty(selectedRow + 1, currentIndex)) {
                propertiesTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                propertiesTable.scrollRectToVisible(propertiesTable.getCellRect(selectedRow + 1, 0, true));
                propertiesTable.revalidate();
            }
        }
    }

    /**
     * Listener for the "Delete line" button
     */
    private class ListenerButtonDeleteLine implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int selectedRow = propertiesTable.getSelectedRow();
            assessmentRuleDataControl.deleteProperty(selectedRow, currentIndex);
            if (assessmentRuleDataControl.getPropertyCount(currentIndex) == 0) propertiesTable.clearSelection(); else if (assessmentRuleDataControl.getPropertyCount(currentIndex) == selectedRow) propertiesTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            propertiesTable.revalidate();
        }
    }

    /**
     * Private class managing the selection listener of the table
     */
    private class NodeTableSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty()) {
                movePropertyUpButton.setEnabled(false);
                movePropertyDownButton.setEnabled(false);
                deletePropertyButton.setEnabled(false);
            } else {
                movePropertyUpButton.setEnabled(true);
                movePropertyDownButton.setEnabled(true);
                deletePropertyButton.setEnabled(true);
            }
        }
    }

    /**
     * Private class containing the model for the line table
     */
    private class NodeTableModel extends AbstractTableModel {

        /**
         * Required
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor
         */
        public NodeTableModel() {
        }

        @Override
        public String getColumnName(int columnIndex) {
            String name = "";
            if (columnIndex == 0) name = "Id"; else if (columnIndex == 1) name = "Value/Var/Flag";
            return name;
        }

        public int getRowCount() {
            int rowCount = 0;
            if (assessmentRuleDataControl != null) rowCount = assessmentRuleDataControl.getPropertyCount(currentIndex);
            return rowCount;
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if (value != null) {
                if (!value.toString().trim().equals("")) {
                    if (columnIndex == 0) {
                        AssessmentPropertiesPanel.this.propertiesTable.updateUI();
                        assessmentRuleDataControl.setPropertyId(rowIndex, currentIndex, value.toString());
                    }
                    fireTableCellUpdated(rowIndex, columnIndex);
                }
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Object value = null;
            switch(columnIndex) {
                case 0:
                    value = assessmentRuleDataControl.getPropertyId(rowIndex, currentIndex);
                    break;
                case 1:
                    value = assessmentRuleDataControl;
                    break;
            }
            return value;
        }
    }

    private class AssessmentPropertyCellRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Data Control
         */
        private AssessmentRuleDataControl value;

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.value = (AssessmentRuleDataControl) value;
            if (this.value.isRegular(row, currentIndex)) return prepareRegular(row, isSelected, table); else return prepareValue(row, isSelected, table);
        }

        public Object getCellEditorValue() {
            return value;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.value = (AssessmentRuleDataControl) value;
            if (this.value.isRegular(row, currentIndex)) return prepareRegular(row, isSelected, table); else return prepareValue(row, isSelected, table);
        }

        private JComboBox prepareValue(int rowIndex, boolean isSelected, JTable table) {
            JComboBox values = null;
            String selectedFlagVar = value.getPropertyValue(rowIndex, currentIndex);
            String[] flagsvars = Controller.getInstance().getVarFlagSummary().getVarsAndFlags();
            values = new JComboBox(flagsvars);
            values.setEditable(false);
            values.setSelectedItem(selectedFlagVar);
            values.addActionListener(new ComboListener(rowIndex, currentIndex, values));
            if (isSelected) values.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, table.getSelectionBackground()));
            return values;
        }

        private JPanel prepareRegular(int rowIndex, boolean isSelected, JTable table) {
            JPanel component = new JPanel();
            component.setLayout(new BorderLayout());
            JTextField value = new JTextField(this.value.getPropertyValue(rowIndex, currentIndex));
            value.addActionListener(new ModifyRegular(rowIndex, currentIndex, value));
            component.add(value, BorderLayout.CENTER);
            if (isSelected) component.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, table.getSelectionBackground()));
            return component;
        }

        private class ComboListener implements ActionListener {

            private int rowIndex;

            private int currentIndex;

            private JComboBox combo;

            public ComboListener(int rowIndex, int currentIndex, JComboBox combo) {
                this.rowIndex = rowIndex;
                this.currentIndex = currentIndex;
                this.combo = combo;
            }

            public void actionPerformed(ActionEvent e) {
                value.setPropertyValue(rowIndex, currentIndex, combo.getSelectedItem().toString());
            }
        }

        private class ModifyRegular implements ActionListener {

            private int rowIndex;

            private int currentIndex;

            private JTextField textField;

            public ModifyRegular(int rowIndex, int currentIndex, JTextField textField) {
                this.rowIndex = rowIndex;
                this.currentIndex = currentIndex;
                this.textField = textField;
            }

            public void actionPerformed(ActionEvent e) {
                value.setPropertyValue(rowIndex, currentIndex, textField.getText());
            }
        }
    }

    /**
     * @return the currentIndex
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * @param currentIndex
     *            the currentIndex to set
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        propertiesTable.setEditingRow(-1);
        propertiesTable.setEditingColumn(-1);
        propertiesTable.updateUI();
        propertiesTable.clearSelection();
    }

    public boolean updateFields() {
        int selected = propertiesTable.getSelectedRow();
        if (propertiesTable.getCellEditor() != null) {
            propertiesTable.getCellEditor().cancelCellEditing();
        }
        ((AbstractTableModel) propertiesTable.getModel()).fireTableDataChanged();
        propertiesTable.getSelectionModel().setSelectionInterval(selected, selected);
        return true;
    }
}
