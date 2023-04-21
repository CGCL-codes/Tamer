package com.hifiremote.jp1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * The Class FunctionImportDialog.
 */
public class FunctionImportDialog extends JDialog implements ActionListener {

    /**
   * Instantiates a new function import dialog.
   * 
   * @param owner the owner
   * @param upgrade the upgrade
   */
    public FunctionImportDialog(JFrame owner, DeviceUpgrade upgrade) {
        super(owner, "Import Functions", true);
        setLocationRelativeTo(owner);
        Container contentPane = getContentPane();
        JLabel instructions = new JLabel("Select the functions to be imported.");
        contentPane.add(instructions, BorderLayout.NORTH);
        for (Function f : upgrade.getFunctions()) {
            if ((f.getName() != null) && (f.getName().length() > 0) && (f.getHex() != null) && (f.getHex().length() > 0)) data.add(new SelectHolder(f));
        }
        for (Function f : upgrade.getExternalFunctions()) {
            if ((f.getName() != null) && (f.getName().length() > 0) && (f.getHex() != null) && (f.getHex().length() > 0)) data.add(new SelectHolder(f));
        }
        model = new AbstractTableModel() {

            public String getColumnName(int col) {
                switch(col) {
                    case 0:
                        return " ";
                    case 1:
                        return "Function";
                    case 2:
                        return "Notes";
                    case 3:
                        return "Hex";
                    default:
                        return null;
                }
            }

            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class; else return String.class;
            }

            public int getRowCount() {
                return data.size();
            }

            public int getColumnCount() {
                return 4;
            }

            public Object getValueAt(int row, int col) {
                SelectHolder h = data.get(row);
                if (col == 0) {
                    if (h.isSelected()) return Boolean.TRUE; else return Boolean.FALSE;
                }
                Function f = h.getData();
                if (col == 1) return f.getName(); else if (col == 2) return f.getNotes(); else return f.getHex().toString();
            }

            public boolean isCellEditable(int row, int col) {
                return (col == 0);
            }

            public void setValueAt(Object value, int row, int col) {
                SelectHolder h = (SelectHolder) data.get(row);
                h.setSelected(((Boolean) value).booleanValue());
            }
        };
        JTableX table = new JTableX(model);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setShowGrid(false);
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        JCheckBox box = new JCheckBox();
        box.setSelected(true);
        column.setMaxWidth(box.getPreferredSize().width);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        Box buttonPanel = Box.createHorizontalBox();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        selectAll = new JButton("Select All");
        selectAll.addActionListener(this);
        buttonPanel.add(selectAll);
        buttonPanel.add(Box.createHorizontalStrut(5));
        selectNone = new JButton("Select None");
        selectNone.addActionListener(this);
        buttonPanel.add(selectNone);
        buttonPanel.add(Box.createHorizontalStrut(5));
        selectToggle = new JButton("Toggle");
        selectToggle.addActionListener(this);
        buttonPanel.add(selectToggle);
        buttonPanel.add(Box.createHorizontalGlue());
        ok = new JButton("OK");
        ok.addActionListener(this);
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(5));
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        buttonPanel.add(cancel);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        pack();
        Rectangle rect = getBounds();
        int x = rect.x - rect.width / 2;
        int y = rect.y - rect.height / 2;
        setLocation(x, y);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if ((source == selectAll) || (source == selectNone)) {
            boolean flag = (source == selectAll);
            for (SelectHolder h : data) {
                h.setSelected(flag);
            }
            model.fireTableDataChanged();
        } else if (source == selectToggle) {
            for (SelectHolder h : data) {
                h.setSelected(!h.isSelected());
            }
            model.fireTableDataChanged();
        } else if (source == cancel) {
            userAction = JOptionPane.CANCEL_OPTION;
            setVisible(false);
            dispose();
        } else if (source == ok) {
            userAction = JOptionPane.OK_OPTION;
            setVisible(false);
            dispose();
        }
    }

    /**
   * Gets the selected functions.
   * 
   * @return the selected functions
   */
    public java.util.List<Function> getSelectedFunctions() {
        java.util.List<Function> v = new ArrayList<Function>();
        for (SelectHolder h : data) {
            if (h.isSelected()) v.add(h.getData());
        }
        return v;
    }

    /**
   * Gets the user action.
   * 
   * @return the user action
   */
    public int getUserAction() {
        return userAction;
    }

    /**
   * The Class SelectionRenderer.
   */
    public class SelectionRenderer extends JCheckBox implements ListCellRenderer {

        /**
     * Instantiates a new selection renderer.
     */
        public SelectionRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setText(value.toString());
            setSelected(isSelected);
            return this;
        }
    }

    /**
   * The Class SelectHolder.
   */
    public class SelectHolder {

        /**
     * Instantiates a new select holder.
     * 
     * @param f the f
     */
        public SelectHolder(Function f) {
            this.data = f;
        }

        /**
     * Checks if is selected.
     * 
     * @return true, if is selected
     */
        public boolean isSelected() {
            return selected;
        }

        /**
     * Sets the selected.
     * 
     * @param flag the new selected
     */
        public void setSelected(boolean flag) {
            selected = flag;
        }

        /**
     * Gets the data.
     * 
     * @return the data
     */
        public Function getData() {
            return data;
        }

        /** The selected. */
        private boolean selected = false;

        /** The data. */
        private Function data = null;
    }

    /** The data. */
    private java.util.List<SelectHolder> data = new ArrayList<SelectHolder>();

    /** The model. */
    private AbstractTableModel model = null;

    /** The select all. */
    private JButton selectAll = null;

    /** The select none. */
    private JButton selectNone = null;

    /** The select toggle. */
    private JButton selectToggle = null;

    /** The ok. */
    private JButton ok = null;

    /** The cancel. */
    private JButton cancel = null;

    /** The user action. */
    private int userAction = JOptionPane.CANCEL_OPTION;
}
