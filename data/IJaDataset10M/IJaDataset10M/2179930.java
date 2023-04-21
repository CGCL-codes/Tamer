package sts.gui.importing;

import sts.gui.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author ken
 */
public class ManualColumnMappingPanel extends javax.swing.JPanel {

    AbstractCSVMappingImportWizard wizard;

    MappingTable table;

    MappingTableModel model;

    int exampleIndex = 0;

    /** Creates new form ManualColumnMappingPanel */
    public ManualColumnMappingPanel(AbstractWizard wiz) {
        wizard = (AbstractCSVMappingImportWizard) wiz;
        model = new MappingTableModel();
        table = new MappingTable(model);
        initComponents();
        this.exampleNextButton.setEnabled(wizard.getRows().size() > 1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = table;
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        exampleNavigationLabel = new javax.swing.JLabel();
        examplePrevButton = new javax.swing.JButton();
        exampleNextButton = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 300));
        jScrollPane1.setViewportView(jTable1);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel1.setLayout(new java.awt.BorderLayout());
        exampleNavigationLabel.setText("Scroll Data");
        jPanel2.add(exampleNavigationLabel);
        examplePrevButton.setText("<");
        examplePrevButton.setEnabled(false);
        examplePrevButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                examplePrevButtonActionPerformed(evt);
            }
        });
        jPanel2.add(examplePrevButton);
        exampleNextButton.setText(">");
        exampleNextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exampleNextButtonActionPerformed(evt);
            }
        });
        jPanel2.add(exampleNextButton);
        jPanel1.add(jPanel2, java.awt.BorderLayout.EAST);
        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }

    private void exampleNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        exampleIndex += 1;
        examplePrevButton.setEnabled(true);
        exampleNextButton.setEnabled(exampleIndex < wizard.getRows().size() - 1);
        model.fireTableDataChanged();
    }

    private void examplePrevButtonActionPerformed(java.awt.event.ActionEvent evt) {
        exampleIndex -= 1;
        exampleNextButton.setEnabled(true);
        examplePrevButton.setEnabled(exampleIndex != 0);
        model.fireTableDataChanged();
    }

    class MappingTable extends JTable {

        MetaRenderer metaRenderer = new MetaRenderer();

        DefaultCellEditor metaEditor;

        public MappingTable(TableModel model) {
            super(model);
            List items = new ArrayList();
            items.add(null);
            items.addAll(wizard.getMeta());
            JComboBox cb = new JComboBox(items.toArray());
            cb.setRenderer(new MetaListRenderer());
            metaEditor = new DefaultCellEditor(cb);
            getColumnModel().getColumn(0).setPreferredWidth(2);
        }

        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 1) return metaEditor; else return super.getCellEditor(row, column);
        }

        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 1) return metaRenderer; else return super.getCellRenderer(row, column);
        }
    }

    class MetaListRenderer extends DefaultListCellRenderer {

        public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value == null) value = "<not mapped>"; else value = ((ColumnMeta) value).getName();
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    class MetaRenderer extends DefaultTableCellRenderer {

        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) value = "<not mapped>"; else value = ((ColumnMeta) value).getName();
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    class MappingTableModel extends AbstractTableModel {

        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Integer.class; else if (columnIndex == 1) return ColumnMeta.class; else return String.class;
        }

        public String getColumnName(int column) {
            if (column == 0) return "Column"; else if (column == 1) return "Mapped Field"; else return "Data";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return new Integer(rowIndex + 1); else if (columnIndex == 1) return wizard.getColumns()[rowIndex]; else return wizard.getRows().get(exampleIndex).get(rowIndex);
        }

        public int getRowCount() {
            return wizard.getRows().get(0).size();
        }

        public int getColumnCount() {
            return 3;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                ColumnMeta[] columns = wizard.getColumns();
                Map<ColumnMeta, Integer> mapping = wizard.getMapping();
                mapping.remove(columns[rowIndex]);
                ColumnMeta meta = (ColumnMeta) aValue;
                columns[rowIndex] = meta;
                mapping.put(meta, rowIndex);
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i] != null && i != mapping.get(columns[i])) {
                        columns[i] = null;
                        this.fireTableCellUpdated(i, columnIndex);
                    }
                }
            } else super.setValueAt(aValue, rowIndex, columnIndex);
        }
    }

    private javax.swing.JLabel exampleNavigationLabel;

    private javax.swing.JButton exampleNextButton;

    private javax.swing.JButton examplePrevButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;
}