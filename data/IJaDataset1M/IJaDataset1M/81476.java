package LabDBComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class LabDBSettingsTable extends JPanel {

    private JTable table;

    private JButton addBtn, delBtn;

    private SettingsTableActionListener al;

    private String itemID;

    private JTextField aliasNameTF;

    private Color bg;

    public LabDBSettingsTable(String title, String itemID, Color bg) {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setMinimumSize(new Dimension(200, 80));
        if (bg != null) {
            this.bg = bg;
            this.setBackground(bg);
        }
        if (itemID.isEmpty()) {
            this.itemID = null;
        } else {
            this.itemID = itemID;
        }
        al = new SettingsTableActionListener();
        aliasNameTF = new JTextField();
        layoutGUI();
    }

    public LabDBSettingsTable(String title, String itemID, String alias, Color bg) {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setMinimumSize(new Dimension(200, 80));
        if (itemID.isEmpty()) {
            this.itemID = null;
        } else {
            this.itemID = itemID;
        }
        if (bg != null) {
            this.bg = bg;
            this.setBackground(bg);
        }
        aliasNameTF = new JTextField(alias);
        al = new SettingsTableActionListener();
        layoutGUI();
    }

    public void setEnabled(boolean enabled) {
        aliasNameTF.setEditable(enabled);
        addBtn.setEnabled(enabled);
        delBtn.setEnabled(enabled);
        table.setEnabled(enabled);
    }

    private void layoutGUI() {
        table = new JTable();
        SettingsTableModel m = new SettingsTableModel();
        table.setModel(m);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(new DefaultListSelectionModel());
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    delBtn.setEnabled(true);
                } else {
                    delBtn.setEnabled(false);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        if (bg != null) {
            tableScrollPane.setBackground(bg);
        }
        tableScrollPane.setPreferredSize(new Dimension(300, 100));
        tableScrollPane.setAutoscrolls(true);
        addBtn = new JButton("add");
        addBtn.setMargin(new Insets(1, 1, 1, 1));
        addBtn.setToolTipText("add an additional property");
        addBtn.setActionCommand("addBtn");
        addBtn.setPreferredSize(new Dimension(60, 20));
        addBtn.addActionListener(al);
        delBtn = new JButton("delete");
        delBtn.setMargin(new Insets(1, 1, 1, 1));
        delBtn.setToolTipText("delete slected property");
        delBtn.setActionCommand("delBtn");
        delBtn.setEnabled(false);
        delBtn.setPreferredSize(new Dimension(60, 20));
        delBtn.addActionListener(al);
        aliasNameTF.setFont(new Font("SansSerif", Font.PLAIN, 9));
        aliasNameTF.setToolTipText("you may enter an alias name for this device. In case of multiple references to the same device this alias must be given!");
        aliasNameTF.setPreferredSize(new Dimension(70, 20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (bg != null) {
            buttonPanel.setBackground(bg);
        }
        buttonPanel.setPreferredSize(new Dimension(75, 20));
        buttonPanel.add(new JLabel("alias"));
        buttonPanel.add(aliasNameTF);
        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
    }

    private void addRowBtnPressed() {
        ((DefaultTableModel) table.getModel()).insertRow(table.getRowCount(), new Object[3]);
    }

    private void deleteRowBtnPressed() {
        if (table.getSelectedRow() != -1) {
            ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
        }
    }

    public Vector<Vector<String>> getValues() {
        if (table.getSelectedRow() != -1 && table.getSelectedColumn() != -1) {
            if (table.getCellEditor() != null) {
                table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn()).stopCellEditing();
            }
        }
        Vector<Vector<String>> values = new Vector<Vector<String>>();
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            if (m.getValueAt(i, 0) != null) {
                if (m.getValueAt(i, 0).toString().isEmpty()) {
                    continue;
                }
                Vector<String> tmp = new Vector<String>();
                tmp.add(m.getValueAt(i, 0).toString());
                if (m.getValueAt(i, 1) != null) {
                    tmp.add(m.getValueAt(i, 1).toString());
                } else {
                    tmp.add("");
                }
                if (m.getValueAt(i, 2) != null) {
                    tmp.add(m.getValueAt(i, 2).toString());
                } else {
                    tmp.add("");
                }
                if (itemID == null) {
                    tmp.add("");
                } else {
                    tmp.add(itemID);
                }
                if (aliasNameTF.getText().isEmpty()) {
                    tmp.add("");
                } else {
                    tmp.add(aliasNameTF.getText());
                }
                values.add(tmp);
            }
        }
        return values;
    }

    public String getItemID() {
        return itemID;
    }

    public int getNonEmptyRowCount() {
        int rowCount = table.getRowCount();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 0) == null) {
                rowCount = rowCount - 1;
            }
        }
        return rowCount;
    }

    public String getItemAlias() {
        if (aliasNameTF.getText().isEmpty()) {
            return null;
        } else {
            return aliasNameTF.getText();
        }
    }

    public void setValues(Object[] names) {
        SettingsTableModel m = new SettingsTableModel(names);
        table.setModel(m);
    }

    public void setValues(Object[] names, Object[] values, Object[] units) {
        SettingsTableModel m = new SettingsTableModel(names, values, units);
        table.setModel(m);
    }

    public void setValues(Object[] names, Object[] units) {
        SettingsTableModel m = new SettingsTableModel(names, units);
        table.setModel(m);
    }

    public void setRow(Object[] values) {
        if (table.getModel().getValueAt(0, 0) == null) {
            ((SettingsTableModel) table.getModel()).removeRow(0);
        }
        ((SettingsTableModel) table.getModel()).addRow(values);
    }

    public void changeTitle(String title) {
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.aliasNameTF.setText(title);
    }

    class SettingsTableActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("addBtn")) {
                addRowBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("delBtn")) {
                deleteRowBtnPressed();
            }
        }
    }

    class SettingsTableModel extends DefaultTableModel {

        public SettingsTableModel() {
            super(1, 3);
            this.setColumnIdentifiers(new Object[] { "property", "value", "unit" });
        }

        public SettingsTableModel(Object[] names) {
            super(names.length, 3);
            this.setColumnIdentifiers(new Object[] { "property", "value", "unit" });
            for (int i = 0; i < names.length; i++) {
                this.setValueAt(names[i], i, 0);
            }
        }

        public SettingsTableModel(Object[] names, Object[] units) {
            super(names.length, 3);
            this.setColumnIdentifiers(new Object[] { "property", "value", "unit" });
            if (names.length == units.length) {
                for (int i = 0; i < names.length; i++) {
                    this.setValueAt(names[i], i, 0);
                    this.setValueAt(units[i], i, 2);
                }
            }
        }

        public SettingsTableModel(Object[] names, Object[] values, Object[] units) {
            super(names.length, 3);
            this.setColumnIdentifiers(new Object[] { "property", "value", "unit" });
            if (names.length == units.length && names.length == values.length) {
                for (int i = 0; i < names.length; i++) {
                    this.setValueAt(names[i], i, 0);
                    this.setValueAt(values[i], i, 1);
                    this.setValueAt(units[i], i, 2);
                }
            }
        }

        public boolean isCellEditable(int row, int col) {
            if (row == 0) {
                return true;
            } else {
                if ((table.getValueAt(row - 1, 0) != null)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
