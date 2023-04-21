package lablog.gui.comp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import lablog.app.Application;
import lablog.gui.comp.ProjectSelection.MyIntComparator;
import lablog.gui.dialog.SearchExport;
import lablog.util.PropertiesManager;
import lablog.util.orm.DatabaseHelper;
import lablog.util.orm.auto.DatabaseQuery;
import lablog.util.orm.auto.Person;

@SuppressWarnings("serial")
public class RetrieveDataPanel extends MainContentPanel {

    private Person user;

    private EntityManager em;

    private JPanel commandPanel, resultsPanel;

    private JTextArea sqlCommandField;

    private String[] connectors = new String[] { " AND ", " OR ", " XOR ", " NOT ", " = ", " <=> ", " < ", " > ", " <> ", " like " };

    private Object[] tables;

    private JTable fieldSelectorTable, conditionTable, resultsTable;

    private FindDataActionListener al;

    private Vector<String> tableSet = new Vector<String>();

    private String[] excludedTables = { "diary", "grants", "grantinvolvements" };

    public RetrieveDataPanel() {
        super("Retrieve datasets", NO_BACKGROUND);
        al = new FindDataActionListener();
        initGUI();
    }

    public void pushCommand(String query) {
        sqlCommandField.setText("");
        sqlCommandField.setText(query);
        findDataBtnPressed();
    }

    private void initGUI() {
        JLabel decLabel = new JLabel(new ImageIcon(PropertiesManager.instance().getResource("iconData_small")));
        decLabel.setOpaque(false);
        setLeftDecoration(decLabel);
        getContentContainer().setLayout(new BorderLayout());
        commandPanel = setCommandPanel();
        resultsPanel = setResultsPanel();
        getContentContainer().add(commandPanel, BorderLayout.NORTH);
        getContentContainer().add(resultsPanel, BorderLayout.CENTER);
    }

    private JPanel setCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(this.getWidth(), 250));
        JPanel cmdEditor = new JPanel(new BorderLayout());
        cmdEditor.setPreferredSize(new Dimension(500, 250));
        JPanel fieldSelector = new JPanel(new BorderLayout());
        fieldSelector.setPreferredSize(new Dimension(250, 250));
        fieldSelector.setBorder(BorderFactory.createTitledBorder("field selection"));
        fieldSelectorTable = new JTable();
        FieldSelectorTableModel model = new FieldSelectorTableModel();
        fieldSelectorTable.setModel(model);
        fieldSelectorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fieldSelectorTable.setSelectionModel(new DefaultListSelectionModel());
        MyPopupMenu fieldSelectorPopupMenu = new MyPopupMenu();
        fieldSelectorPopupMenu.setInsertRowAfterActionCommand("fieldInsertRowAfter");
        fieldSelectorPopupMenu.addInsertRowAfterActionListener(al);
        fieldSelectorPopupMenu.setInsertRowBeforeActionCommand("fieldInsertRowBefore");
        fieldSelectorPopupMenu.addInsertRowBeforeActionListener(al);
        fieldSelectorPopupMenu.setDeleteRowActionCommand("fieldDeleteRow");
        fieldSelectorPopupMenu.addDeleteRowActionListener(al);
        fieldSelectorPopupMenu.setClearTableActionCommand("fieldClearTable");
        fieldSelectorPopupMenu.addClearTableActionListener(al);
        fieldSelectorTable.addMouseListener(new PopupListener(fieldSelectorPopupMenu));
        fieldSelectorTable.setPreferredSize(new Dimension(180, 220));
        JScrollPane fieldSelectorScrollPane = new JScrollPane(fieldSelectorTable);
        fieldSelectorScrollPane.setAutoscrolls(true);
        fieldSelector.add(fieldSelectorScrollPane, BorderLayout.CENTER);
        JPanel conditionSelector = new JPanel(new BorderLayout());
        conditionSelector.setPreferredSize(new Dimension(250, 250));
        conditionSelector.setBorder(BorderFactory.createTitledBorder("condition editor"));
        conditionTable = new JTable();
        ConditionSelectorTableModel m = new ConditionSelectorTableModel();
        conditionTable.setModel(m);
        conditionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conditionTable.setSelectionModel(new DefaultListSelectionModel());
        conditionTable.getColumn("connector").setMaxWidth(60);
        conditionTable.getColumn("table").setMaxWidth(120);
        conditionTable.getColumn("field").setCellEditor(new ConditionSelectComboBoxCellEditor());
        conditionTable.getColumn("field").setMaxWidth(120);
        conditionTable.setPreferredSize(new Dimension(180, 220));
        MyPopupMenu conditionPopupMenu = new MyPopupMenu();
        conditionPopupMenu.setInsertRowAfterActionCommand("conditionInsertRowAfter");
        conditionPopupMenu.addInsertRowAfterActionListener(al);
        conditionPopupMenu.setInsertRowBeforeActionCommand("conditionInsertRowBefore");
        conditionPopupMenu.addInsertRowBeforeActionListener(al);
        conditionPopupMenu.setDeleteRowActionCommand("conditionDeleteRow");
        conditionPopupMenu.addDeleteRowActionListener(al);
        conditionPopupMenu.setClearTableActionCommand("conditionClearTable");
        conditionPopupMenu.addClearTableActionListener(al);
        conditionTable.addMouseListener(new PopupListener(conditionPopupMenu));
        JScrollPane conditionTableScrollPane = new JScrollPane(conditionTable);
        conditionTableScrollPane.setAutoscrolls(true);
        conditionSelector.add(conditionTableScrollPane, BorderLayout.CENTER);
        cmdEditor.add(fieldSelector, BorderLayout.WEST);
        cmdEditor.add(conditionSelector, BorderLayout.CENTER);
        JPanel sqlCommandPanel = new JPanel(new BorderLayout());
        sqlCommandPanel.setPreferredSize(new Dimension(this.getWidth(), 140));
        sqlCommandPanel.setBorder(BorderFactory.createTitledBorder("sql command"));
        sqlCommandField = new JTextArea("SELECT * FROM datasets WHERE datasets.datasetid > 0");
        sqlCommandField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sqlCommandField.setToolTipText("enter a sql select-command to the database");
        sqlCommandField.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (sqlCommandField.getText().endsWith(";")) {
                        findDataBtnPressed();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        JScrollPane commandScrollPane = new JScrollPane(sqlCommandField);
        commandScrollPane.setAutoscrolls(true);
        JButton findBtn = new JButton("go!");
        findBtn.setPreferredSize(new Dimension(70, 20));
        findBtn.setToolTipText("execute command to find the data matching the query");
        findBtn.setActionCommand("findBtn");
        findBtn.setMargin(new Insets(2, 2, 2, 2));
        findBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        findBtn.addActionListener(al);
        JButton builtCmdBtn = new JButton("build");
        builtCmdBtn.setPreferredSize(new Dimension(70, 20));
        builtCmdBtn.setToolTipText("build the select query from the table values");
        builtCmdBtn.setMargin(new Insets(2, 2, 2, 2));
        builtCmdBtn.setActionCommand("buildBtn");
        builtCmdBtn.setFont(new Font("SansSerif", Font.BOLD, 9));
        builtCmdBtn.addActionListener(al);
        JButton openCmdBtn = new JButton("open");
        openCmdBtn.setPreferredSize(new Dimension(70, 20));
        openCmdBtn.setToolTipText("select a stored query");
        openCmdBtn.setMargin(new Insets(2, 2, 2, 2));
        openCmdBtn.setActionCommand("openCmdBtn");
        openCmdBtn.setFont(new Font("SansSerif", Font.BOLD, 9));
        openCmdBtn.addActionListener(al);
        JButton storeCmdBtn = new JButton("store");
        storeCmdBtn.setPreferredSize(new Dimension(70, 20));
        storeCmdBtn.setMargin(new Insets(2, 2, 2, 2));
        storeCmdBtn.setToolTipText("stored this query as template");
        storeCmdBtn.setActionCommand("storeCmdBtn");
        storeCmdBtn.setFont(new Font("SansSerif", Font.BOLD, 9));
        storeCmdBtn.addActionListener(al);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setPreferredSize(new Dimension(80, 50));
        ((FlowLayout) btnPanel.getLayout()).setVgap(2);
        btnPanel.add(builtCmdBtn);
        btnPanel.add(openCmdBtn);
        btnPanel.add(storeCmdBtn);
        btnPanel.add(findBtn);
        sqlCommandPanel.add(commandScrollPane, BorderLayout.CENTER);
        sqlCommandPanel.add(btnPanel, BorderLayout.EAST);
        panel.add(cmdEditor, BorderLayout.CENTER);
        panel.add(sqlCommandPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel setResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("search results"));
        resultsTable = new JTable();
        resultsTable.setAutoCreateRowSorter(true);
        DefaultTableModel m = new DefaultTableModel(2, 2);
        resultsTable.setModel(m);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setAutoscrolls(true);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
        JButton exportBtn = new JButton("export");
        exportBtn.setPreferredSize(new Dimension(70, 20));
        exportBtn.setToolTipText("export these search results to text-file");
        exportBtn.setActionCommand("exportBtn");
        exportBtn.addActionListener(al);
        btnPanel.add(exportBtn);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshResults(List<?> values) {
        try {
            DefaultTableModel m = new DefaultTableModel();
            for (int i = 0; i < ((Object[]) values.get(i)).length; i++) {
                m.addColumn(i);
            }
            for (int i = 0; i < values.size(); i++) {
                m.addRow((Object[]) values.get(i));
            }
            resultsTable.setModel(m);
            resultsTable.doLayout();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQLException: " + e.getMessage(), "SQL Exception in refreshResults", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void buildSelectCommand() {
        if (fieldSelectorTable.getSelectedRow() != -1) {
            fieldSelectorTable.getCellEditor(fieldSelectorTable.getSelectedRow(), fieldSelectorTable.getSelectedColumn()).stopCellEditing();
        }
        if (conditionTable.getSelectedRow() != -1) {
            conditionTable.getCellEditor(conditionTable.getSelectedRow(), conditionTable.getSelectedColumn()).stopCellEditing();
        }
        String selectPart = "SELECT ";
        String fromPart = " FROM ";
        String wherePart = " WHERE ";
        for (int i = 0; i < fieldSelectorTable.getRowCount(); i++) {
            Object tableValue = fieldSelectorTable.getValueAt(i, 0);
            Object fieldValue = fieldSelectorTable.getValueAt(i, 1);
            if (i > 0 && fieldSelectorTable.getValueAt(i, 0) != null) {
                selectPart = selectPart + ",";
            }
            if (tableValue != null && tableValue != "" && fieldValue != null && fieldValue != "") {
                if (!tableSet.contains(tableValue)) {
                    tableSet.add(tableValue.toString());
                }
                selectPart = selectPart + tableValue + "." + fieldValue;
            } else {
                break;
            }
        }
        for (int i = 0; i < conditionTable.getRowCount(); i++) {
            Object connectorValue = conditionTable.getValueAt(i, 0);
            Object tableValue = conditionTable.getValueAt(i, 1);
            Object fieldValue = conditionTable.getValueAt(i, 2);
            Object conditionValue = conditionTable.getValueAt(i, 3);
            boolean ok = false;
            if (i == 0) {
                ok = ((tableValue != null && tableValue != "") && (fieldValue != null && fieldValue != "")) || conditionValue != null;
                connectorValue = "";
            } else {
                ok = connectorValue != null && ((tableValue != null && fieldValue != null) || conditionValue != null);
            }
            if (ok) {
                if (conditionValue == null) conditionValue = "";
                String tableField = "";
                if (tableValue != null && tableValue != "") {
                    tableField = tableValue + "." + fieldValue;
                    if (!tableSet.contains(tableValue)) {
                        tableSet.add(tableValue.toString());
                    }
                }
                wherePart = wherePart + connectorValue + tableField + " " + conditionValue;
            } else {
                break;
            }
        }
        for (int i = 0; i < tableSet.size(); i++) {
            fromPart = fromPart + tableSet.get(i);
            if (i != tableSet.size() - 1) {
                fromPart = fromPart + ", ";
            }
        }
        sqlCommandField.setText(selectPart + "\n" + fromPart + "\n" + wherePart + ";");
    }

    private void openCommandBtnPressed() {
        SQLQueryStorage storage = new SQLQueryStorage();
        if (storage.getQuery() != null) {
            sqlCommandField.setText(storage.getQuery().getQuery());
        } else {
            sqlCommandField.setText("");
        }
    }

    private void storeCommandBtnPressed() {
        new SQLQueryStorage(sqlCommandField.getText());
    }

    private void findDataBtnPressed() {
        for (int i = 0; i < excludedTables.length; i++) {
            if (sqlCommandField.getText().toLowerCase().contains("," + excludedTables[i].toLowerCase() + " ") || sqlCommandField.getText().toLowerCase().contains(" " + excludedTables[i].toLowerCase() + " ")) {
                JOptionPane.showMessageDialog(this, "At least one of the tables you want to search may not be searched!", "Invald table given!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (sqlCommandField.getText().toLowerCase().indexOf("select") != 0) {
            JOptionPane.showMessageDialog(this, "the sql command must start with 'select'!", "invalid command", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            if (em == null || !em.isOpen()) {
                em = DatabaseHelper.instance().getEntityManager();
            }
            Query q = em.createNativeQuery(sqlCommandField.getText());
            List<?> values = q.getResultList();
            refreshResults(values);
        }
    }

    private void exportBtnPressed() {
        new SearchExport((DefaultTableModel) resultsTable.getModel());
    }

    private void clearSelectTable() {
        fieldSelectorTable.setModel(new FieldSelectorTableModel());
    }

    private void deleteSelectRow() {
        ((DefaultTableModel) fieldSelectorTable.getModel()).removeRow(fieldSelectorTable.getSelectedRow());
    }

    private void insertSelectRowBefore() {
        ((DefaultTableModel) fieldSelectorTable.getModel()).insertRow(fieldSelectorTable.getSelectedRow(), new Object[3]);
    }

    private void insertSelectRowAfter() {
        ((DefaultTableModel) fieldSelectorTable.getModel()).insertRow(fieldSelectorTable.getSelectedRow() + 1, new Object[3]);
    }

    private void clearConditionTable() {
        conditionTable.setModel(new ConditionSelectorTableModel());
    }

    private void deleteConditionRow() {
        ((DefaultTableModel) conditionTable.getModel()).removeRow(conditionTable.getSelectedRow());
        conditionTable.setValueAt("", 0, 0);
    }

    private void insertConditionRowBefore() {
        ((DefaultTableModel) conditionTable.getModel()).insertRow(conditionTable.getSelectedRow(), new Object[3]);
    }

    private void insertConditionRowAfter() {
        ((DefaultTableModel) conditionTable.getModel()).insertRow(conditionTable.getSelectedRow() + 1, new Object[3]);
    }

    class FindDataActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("findBtn")) {
                findDataBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("exportBtn")) {
                exportBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("buildBtn")) {
                buildSelectCommand();
            } else if (e.getActionCommand().equalsIgnoreCase("openCmdBtn")) {
                openCommandBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("storeCmdBtn")) {
                storeCommandBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("conditionClearTable")) {
                clearConditionTable();
            } else if (e.getActionCommand().equalsIgnoreCase("conditionDeleteRow")) {
                deleteConditionRow();
            } else if (e.getActionCommand().equalsIgnoreCase("conditionInsertRowAfter")) {
                insertConditionRowAfter();
            } else if (e.getActionCommand().equalsIgnoreCase("conditionInsertRowBefore")) {
                insertConditionRowBefore();
            } else if (e.getActionCommand().equalsIgnoreCase("fieldClearTable")) {
                clearSelectTable();
            } else if (e.getActionCommand().equalsIgnoreCase("fieldInsertRowAfter")) {
                insertSelectRowAfter();
            } else if (e.getActionCommand().equalsIgnoreCase("fieldInsertRowBefore")) {
                insertSelectRowBefore();
            } else if (e.getActionCommand().equalsIgnoreCase("fieldDeleteRow")) {
                deleteSelectRow();
            }
        }
    }

    class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    @SuppressWarnings("serial")
    class FieldSelectComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

        private JComboBox component;

        private boolean valueUpdate = false;

        private FieldSelectComboBoxCellEditorItemListener il = new FieldSelectComboBoxCellEditorItemListener();

        public FieldSelectComboBoxCellEditor() {
            component = new JComboBox();
            setValues();
            component.addItemListener(il);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public FieldSelectComboBoxCellEditor(Object[] values) {
            component = new JComboBox();
            component.setPreferredSize(new Dimension(80, 45));
            component.addItemListener(il);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
            setValues(values);
        }

        public FieldSelectComboBoxCellEditor(Vector<String> values) {
            component = new JComboBox();
            setValues(values);
            component.setPreferredSize(new Dimension(80, 45));
            component.addItemListener(il);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public void setValues() {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            valueUpdate = false;
        }

        public void setValues(Object[] newValues) {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            for (int i = 0; i < newValues.length; i++) {
                m.addElement(newValues[i]);
            }
            valueUpdate = false;
        }

        public void setValues(Vector<String> newValues) {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            for (int i = 0; i < newValues.size(); i++) {
                m.addElement(newValues.get(i));
            }
            valueUpdate = false;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            TableColumn tCol = table.getColumn(table.getColumnName(col));
            tCol.setMinWidth(component.getPreferredSize().width);
            return component;
        }

        public Object getCellEditorValue() {
            return component.getSelectedItem();
        }

        class FieldSelectComboBoxCellEditorItemListener implements ItemListener {

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                int col = fieldSelectorTable.getSelectedColumn();
                int row = fieldSelectorTable.getSelectedRow();
                fieldSelectorTable.revalidate();
            }
        }
    }

    @SuppressWarnings("serial")
    class ConditionSelectComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

        private JComboBox component;

        private ConditionSelectComboBoxCellEditorItemListener il = new ConditionSelectComboBoxCellEditorItemListener();

        private boolean valueUpdate = false;

        public ConditionSelectComboBoxCellEditor() {
            component = new JComboBox();
            component.addItemListener(il);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public ConditionSelectComboBoxCellEditor(Object[] values) {
            component = new JComboBox();
            component.addItemListener(il);
            setValues(values);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public ConditionSelectComboBoxCellEditor(Vector<String> values) {
            component = new JComboBox();
            component.addItemListener(il);
            setValues(values);
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public void setValues() {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            valueUpdate = false;
        }

        public void setValues(Object[] newValues) {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            for (int i = 0; i < newValues.length; i++) {
                m.addElement(newValues[i]);
            }
            valueUpdate = false;
        }

        public void setValues(Vector<String> newValues) {
            valueUpdate = true;
            DefaultComboBoxModel m = (DefaultComboBoxModel) component.getModel();
            m.removeAllElements();
            m.addElement("");
            for (int i = 0; i < newValues.size(); i++) {
                m.addElement(newValues.get(i));
            }
            valueUpdate = false;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            TableColumn tCol = table.getColumn(table.getColumnName(col));
            tCol.setMinWidth(component.getPreferredSize().width);
            return component;
        }

        public Object getCellEditorValue() {
            return component.getSelectedItem();
        }

        class ConditionSelectComboBoxCellEditorItemListener implements ItemListener {

            public void itemStateChanged(ItemEvent arg0) {
                int col = conditionTable.getSelectedColumn();
                int row = conditionTable.getSelectedRow();
                conditionTable.revalidate();
            }
        }
    }

    @SuppressWarnings("serial")
    class FieldSelectorTableModel extends DefaultTableModel {

        public FieldSelectorTableModel() {
            super(8, 2);
            this.setColumnIdentifiers(new Object[] { "table", "field" });
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && row == 0) {
                return true;
            } else if (col == 0 && row != 0) if (this.getValueAt(row - 1, col) != null && getValueAt(row - 1, col) != "") return true; else return false; else if (col == 1) if ((this.getValueAt(row, 0) != null) && (this.getValueAt(row, 0) != "")) return true; else return false; else return false;
        }
    }

    @SuppressWarnings("serial")
    class ConditionSelectorTableModel extends DefaultTableModel {

        public ConditionSelectorTableModel() {
            super(8, 4);
            this.setColumnIdentifiers(new Object[] { "connector", "table", "field", "condition" });
        }

        public boolean isCellEditable(int row, int col) {
            if (row == 0) {
                if (col == 0) {
                    return false;
                } else if (col == 1) {
                    return true;
                } else if (col == 2) {
                    if ((this.getValueAt(row, col - 1) != null) && (this.getValueAt(row, col - 1) != "")) return true; else {
                        return false;
                    }
                } else {
                    if ((this.getValueAt(row, col - 2) != null) ^ (this.getValueAt(row, col - 1) != null)) return false; else {
                        return true;
                    }
                }
            } else {
                if (col == 0) {
                    if (this.getValueAt(row - 1, 1) != null || getValueAt(row - 1, 3) != null) return true; else return false;
                } else if (col == 1) {
                    if ((this.getValueAt(row, 0) != null)) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (col == 2) {
                    if ((this.getValueAt(row, col - 1) != null) && (this.getValueAt(row, col - 1) != "")) return true; else {
                        return false;
                    }
                } else {
                    if (this.getValueAt(row - 1, 1) != null || (getValueAt(row - 1, 3) != "" && getValueAt(row - 1, 3) != null)) return true; else return false;
                }
            }
        }
    }
}

@SuppressWarnings("serial")
class SQLQueryStorage extends JDialog {

    private JDialog w;

    private Container cp;

    private JTextArea queryTextArea, queryDescriptionArea;

    private String query;

    private JButton editBtn, deleteBtn;

    private JTable queryTable;

    private boolean editQuery = false;

    private SQLQueryStorageActionListener al = new SQLQueryStorageActionListener();

    private static final int OK_STATE = 0, CANCEL_STATE = 1;

    private int state = -1;

    private DatabaseQuery selectedQuery = null;

    private EntityManager em;

    public SQLQueryStorage() {
        this("");
    }

    public SQLQueryStorage(String sqlCmd) {
        query = sqlCmd;
        em = DatabaseHelper.instance().getEntityManager();
        setGUI();
        editQuery = false;
        queryTextArea.setEditable(true);
        queryDescriptionArea.setEditable(true);
        w.setVisible(true);
    }

    private void setGUI() {
        w = new JDialog();
        w.setModal(true);
        w.setTitle("stored database queries");
        w.setLocationRelativeTo(Application.instance().getMainWindow());
        cp = w.getContentPane();
        w.setSize(500, 500);
        cp.setLayout(new BorderLayout());
        cp.add(setCommandPanel(), BorderLayout.SOUTH);
        cp.add(setTablePanel(), BorderLayout.CENTER);
        listQueries();
        queryTextArea.setText(query);
    }

    private JPanel setTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 200));
        panel.setBorder(BorderFactory.createTitledBorder("stored SQL-queries"));
        queryTable = new JTable();
        queryTable.setModel(new QueryTableModel());
        queryTable.getColumn("author").setMaxWidth(150);
        queryTable.getColumn("id").setMaxWidth(40);
        queryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (queryTable.getSelectedRow() != -1) {
                    int row = queryTable.getSelectedRow();
                    if (queryTable.getValueAt(row, 0) != null && queryTable.getValueAt(row, 0) != "") {
                        selectedQuery = (DatabaseQuery) queryTable.getModel().getValueAt(queryTable.convertRowIndexToModel(queryTable.getSelectedRow()), 3);
                        em.merge(selectedQuery);
                        queryDescriptionArea.setText(selectedQuery.getDescription());
                        queryTextArea.setText(selectedQuery.getQuery());
                        queryTextArea.setEditable(false);
                        queryDescriptionArea.setEditable(false);
                        editBtn.setEnabled(true);
                        deleteBtn.setEnabled(true);
                    }
                } else {
                    editBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                    if (editQuery) {
                        queryDescriptionArea.setText("");
                        queryTextArea.setText("");
                    }
                    selectedQuery = null;
                }
            }
        });
        try {
            queryTable.getColumnModel().removeColumn(queryTable.getColumn("entity"));
        } catch (Exception e) {
        }
        TableRowSorter<QueryTableModel> sorter = new TableRowSorter<QueryTableModel>((QueryTableModel) queryTable.getModel());
        sorter.setComparator(queryTable.getColumn("id").getModelIndex(), new MyIntComparator());
        queryTable.setRowSorter(sorter);
        JScrollPane tablePane = new JScrollPane(queryTable);
        panel.add(tablePane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel setCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 300));
        queryTextArea = new JTextArea();
        queryTextArea.setWrapStyleWord(true);
        queryTextArea.setLineWrap(true);
        queryTextArea.setEditable(false);
        JScrollPane queryPane = new JScrollPane(queryTextArea);
        JPanel queryPanel = new JPanel(new BorderLayout());
        queryPanel.setBorder(BorderFactory.createTitledBorder("SQL-query"));
        queryPanel.add(queryPane);
        queryPanel.setPreferredSize(new Dimension(400, 150));
        queryDescriptionArea = new JTextArea();
        queryDescriptionArea.setWrapStyleWord(true);
        queryDescriptionArea.setLineWrap(true);
        queryDescriptionArea.setEditable(false);
        JScrollPane descrPane = new JScrollPane(queryDescriptionArea);
        JPanel descrPanel = new JPanel(new BorderLayout());
        descrPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descrPanel.setPreferredSize(new Dimension(400, 150));
        descrPanel.add(descrPane);
        panel.add(descrPanel, BorderLayout.CENTER);
        panel.add(queryPanel, BorderLayout.NORTH);
        panel.add(setBtnPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel setBtnPanel() {
        JButton okBtn = new JButton("ok");
        okBtn.setToolTipText("save changes, and close window");
        okBtn.setPreferredSize(new Dimension(75, 20));
        okBtn.setActionCommand("okBtn");
        okBtn.addActionListener(al);
        editBtn = new JButton("edit");
        editBtn.setEnabled(false);
        editBtn.setToolTipText("edit query");
        editBtn.setPreferredSize(new Dimension(75, 20));
        editBtn.setActionCommand("editBtn");
        editBtn.addActionListener(al);
        deleteBtn = new JButton("delete");
        deleteBtn.setToolTipText("edit query");
        deleteBtn.setPreferredSize(new Dimension(75, 20));
        deleteBtn.setActionCommand("deleteBtn");
        deleteBtn.addActionListener(al);
        deleteBtn.setEnabled(false);
        JButton cancelBtn = new JButton("cancel");
        cancelBtn.setToolTipText("exit window discarding changes");
        cancelBtn.setPreferredSize(new Dimension(75, 20));
        cancelBtn.setActionCommand("cancelBtn");
        cancelBtn.addActionListener(al);
        JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(okBtn);
        panel.add(cancelBtn);
        return panel;
    }

    @SuppressWarnings("unchecked")
    private void listQueries() {
        Vector<DatabaseQuery> queries = new Vector<DatabaseQuery>();
        if (em == null || !em.isOpen()) {
            em = DatabaseHelper.instance().getEntityManager();
        }
        Query query = em.createQuery("Select q FROM DatabaseQuery q", DatabaseQuery.class);
        queries.addAll(query.getResultList());
        QueryTableModel m = (QueryTableModel) queryTable.getModel();
        for (int i = m.getRowCount() - 1; i > -1; i--) {
            m.removeRow(i);
        }
        for (int i = 0; i < queries.size(); i++) {
            Object[] temp = new Object[4];
            temp[0] = queries.get(i).getId();
            temp[1] = queries.get(i).getDescription();
            Person p = em.find(Person.class, queries.get(i).getAuthorId());
            temp[2] = p.getLastName() + ", " + p.getFirstName();
            temp[3] = queries.get(i);
            m.insertRow(i, temp);
        }
    }

    private void editBtnPressed() {
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        queryTextArea.setEditable(true);
        queryDescriptionArea.setEditable(true);
        editQuery = true;
    }

    private void submitBtnPressed() {
        if (!queryTextArea.getText().equals("") || !queryDescriptionArea.getText().equals("")) {
            if (selectedQuery != null) {
                selectedQuery.setQuery(textSanitiser(queryTextArea.getText()));
                selectedQuery.setDescription(textSanitiser(queryDescriptionArea.getText()));
                selectedQuery.setAuthorId(DatabaseHelper.instance().getUserId());
                em.persist(selectedQuery);
                editBtn.setEnabled(false);
                queryTable.getSelectionModel().setSelectionInterval(-1, -1);
            } else {
                DatabaseQuery query = new DatabaseQuery();
                query.setQuery(textSanitiser(queryTextArea.getText()));
                query.setDescription(textSanitiser(queryDescriptionArea.getText()));
                query.setAuthorId(DatabaseHelper.instance().getUserId());
                em.persist(query);
                listQueries();
            }
        }
    }

    private void okBtnPressed() {
        state = OK_STATE;
        submitBtnPressed();
        w.setVisible(false);
    }

    private void cancelBtnPressed() {
        state = CANCEL_STATE;
        w.setVisible(false);
    }

    private void deleteBtnPressed() {
        if (queryTable.getSelectedRow() != -1) {
            em.remove(selectedQuery);
            queryDescriptionArea.setText("");
            queryTextArea.setText("");
            listQueries();
        }
    }

    /**
	 * Returns the selected query.
	 * @return
	 */
    public DatabaseQuery getQuery() {
        if (state == OK_STATE) {
            return selectedQuery;
        } else return null;
    }

    class QueryTableModel extends DefaultTableModel {

        public QueryTableModel() {
            super();
            setColumnIdentifiers(new Object[] { "id", "description", "author", "entity" });
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 || col == 3 || row == 2) {
                return false;
            }
            if (editQuery) {
                if (queryTable.getSelectedRow() == row) return true; else return false;
            } else return false;
        }
    }

    class SQLQueryStorageActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("okBtn")) {
                okBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("cancelBtn")) {
                cancelBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("submitBtn")) {
                submitBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("editBtn")) {
                editBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("deleteBtn")) {
                deleteBtnPressed();
            }
        }
    }

    private String textSanitiser(String text) {
        text = text.replace("\\", "\\\\");
        text = text.replace("\'", "\\\'");
        return text;
    }
}

@SuppressWarnings("serial")
class MyPopupMenu extends JPopupMenu {

    private JMenuItem deleteRowItem, insertRowBeforeItem, insertRowAfterItem, clearTableItem;

    public MyPopupMenu() {
        super();
        deleteRowItem = new JMenuItem("delete row");
        insertRowBeforeItem = new JMenuItem("insert row before");
        insertRowAfterItem = new JMenuItem("insert row after");
        clearTableItem = new JMenuItem("clear table");
        add(insertRowBeforeItem);
        add(insertRowAfterItem);
        addSeparator();
        add(deleteRowItem);
        addSeparator();
        add(clearTableItem);
    }

    public void setInsertRowBeforeActionCommand(String cmd) {
        insertRowBeforeItem.setActionCommand(cmd);
    }

    public void setInsertRowAfterActionCommand(String cmd) {
        insertRowAfterItem.setActionCommand(cmd);
    }

    public void setDeleteRowActionCommand(String cmd) {
        deleteRowItem.setActionCommand(cmd);
    }

    public void setClearTableActionCommand(String cmd) {
        clearTableItem.setActionCommand(cmd);
    }

    public void addInsertRowBeforeActionListener(ActionListener al) {
        insertRowBeforeItem.addActionListener(al);
    }

    public void addInsertRowAfterActionListener(ActionListener al) {
        insertRowAfterItem.addActionListener(al);
    }

    public void addDeleteRowActionListener(ActionListener al) {
        deleteRowItem.addActionListener(al);
    }

    public void addClearTableActionListener(ActionListener al) {
        clearTableItem.addActionListener(al);
    }
}
