package org.tolven.teditor.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import org.tolven.logging.TolvenLogger;
import org.tolven.menuStructure.Alignment;
import org.tolven.menuStructure.Column;
import org.tolven.menuStructure.ColumnType;
import org.tolven.teditor.controller.ApplicationEditor;
import org.tolven.teditor.model.ColumnTableModel;

@SuppressWarnings("serial")
public class ColumnTable extends JPanel {

    private JTable table;

    private ColumnTableModel model;

    private JScrollPane scrollTable;

    private JButton up;

    private JButton down;

    private JButton insert;

    private JButton delete;

    private JButton edit;

    private static final String BUTTON_UP = "UP";

    private static final String BUTTON_DOWN = "Down";

    private static final String BUTTON_INSERT = "Insert";

    private static final String BUTTON_DELETE = "Delete";

    private static final String BUTTON_EDIT = "Edit";

    public ColumnTable() {
        super();
        model = new ColumnTableModel();
        initialize();
    }

    public ColumnTable(ColumnTableModel ltm) {
        super();
        this.model = ltm;
        initialize();
    }

    protected void setupColumns() {
        setupTypeColumn();
        setupAlignColumn();
        setupFromColumn();
    }

    protected void setupTypeColumn() {
        TableColumn col = table.getColumnModel().getColumn(ColumnTableModel.TYPE_COL);
        JComboBox combo = new JComboBox();
        for (ColumnType type : ColumnType.values()) {
            combo.addItem(type.value());
        }
        col.setCellEditor(new DefaultCellEditor(combo));
    }

    protected void setupAlignColumn() {
        TableColumn col = table.getColumnModel().getColumn(ColumnTableModel.ALIGN_COL);
        JComboBox combo = new JComboBox();
        for (Alignment align : Alignment.values()) {
            combo.addItem(align.value());
        }
        col.setCellEditor(new DefaultCellEditor(combo));
    }

    protected void setupFromColumn() {
        TableColumn col = table.getColumnModel().getColumn(ColumnTableModel.FROM_COL);
        col.setCellEditor(new FormCellButtonEditor());
        col.setCellRenderer(new ColumnTableButtonRenderer());
    }

    private void initialize() {
        setBorder(new TitledBorder(null, "Columns", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        scrollTable = new JScrollPane();
        table = new JTable(model);
        table.setBounds(10, 10, 410, 158);
        table.getSelectionModel().addListSelectionListener(new SelectionListener());
        setupColumns();
        scrollTable.setViewportView(table);
        TableModifiers buttonListeners = new TableModifiers();
        up = new JButton(BUTTON_UP);
        up.addActionListener(buttonListeners);
        up.setBounds(10, 185, 41, 26);
        up.setEnabled(false);
        down = new JButton(BUTTON_DOWN);
        down.addActionListener(buttonListeners);
        down.setBounds(70, 185, 41, 26);
        down.setEnabled(false);
        edit = new JButton(BUTTON_EDIT);
        edit.setBounds(70, 185, 41, 26);
        edit.addActionListener(buttonListeners);
        edit.setEnabled(false);
        insert = new JButton(BUTTON_INSERT);
        insert.addActionListener(buttonListeners);
        delete = new JButton(BUTTON_DELETE);
        delete.addActionListener(buttonListeners);
        delete.setEnabled(false);
        GroupLayout layout = new GroupLayout((JComponent) this);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE).addGap(38, 38, 38).addGroup(layout.createSequentialGroup().addComponent(up).addGap(27, 27, 27).addComponent(down).addGap(27, 27, 27).addComponent(insert).addGap(27, 27, 27).addComponent(delete).addGap(27, 27, 27).addComponent(edit)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scrollTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(up).addComponent(down).addComponent(insert).addComponent(delete).addComponent(edit)));
        setLayout(layout);
    }

    public java.util.List<Column> getColumns() {
        return model.getDatamodel();
    }

    public void setColumns(java.util.List<Column> columns) {
        setTableModel(new ColumnTableModel(columns));
    }

    public ColumnTableModel getTableModel() {
        return model;
    }

    public void setTableModel(ColumnTableModel columnsModel) {
        this.model = columnsModel;
        table.setModel(this.model);
        setupColumns();
        setTableHeight();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new ColumnTable();
    }

    private void setTableHeight() {
        Dimension prefSize = scrollTable.getSize();
        prefSize.height = table.getRowHeight() * (model.getRowCount() + 2);
        scrollTable.setPreferredSize(prefSize);
        updateUI();
    }

    public void fireColumnUpdated() {
        model.fireTableDataChanged();
    }

    class TableModifiers implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            final int row = table.getSelectedRow();
            final ActionEvent ae = e;
            Thread action = new Thread() {

                public void run() {
                    if (BUTTON_UP.equals(ae.getActionCommand()) && row > 0) {
                        model.moveUp(row);
                        table.setRowSelectionInterval(row - 1, row - 1);
                    } else if (BUTTON_DOWN.equals(ae.getActionCommand()) && row > -1 && row < table.getRowCount() - 1) {
                        model.moveDown(row);
                        table.setRowSelectionInterval(row + 1, row + 1);
                    } else if (BUTTON_INSERT.equals(ae.getActionCommand())) {
                        int r = row;
                        model.addRow(row);
                        if (row < 0 || row > model.getRowCount()) r = model.getRowCount() - 1;
                        model.fireTableRowsInserted(0, model.getRowCount() - 1);
                        table.setRowSelectionInterval(r, r);
                        setTableHeight();
                    } else if (BUTTON_DELETE.equals(ae.getActionCommand()) && row > -1 && row < table.getRowCount()) {
                        model.removeRow(row);
                        int colsize = (model.getRowCount() > 0) ? row : 0;
                        model.fireTableRowsDeleted(0, colsize);
                        setTableHeight();
                    } else if (BUTTON_EDIT.equals(ae.getActionCommand()) && row > -1 && row < table.getRowCount()) {
                        ColumnUpdater cu = new ColumnUpdater(null, ColumnTable.this, "updater", model.getDatamodel().get(row));
                        cu.pack();
                    } else {
                        TolvenLogger.info(ae.getActionCommand(), ColumnTable.class);
                    }
                }
            };
            action.start();
        }
    }

    class SelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            final int row = table.getSelectedRow();
            Thread selThread = new Thread() {

                public void run() {
                    if (row <= 0) {
                        up.setEnabled(false);
                    } else {
                        up.setEnabled(true);
                    }
                    if (row == table.getRowCount() - 1 || row < 0) {
                        down.setEnabled(false);
                    } else {
                        down.setEnabled(true);
                    }
                    if (row < 0) {
                        delete.setEnabled(false);
                        edit.setEnabled(false);
                    } else {
                        delete.setEnabled(true);
                        edit.setEnabled(true);
                    }
                }
            };
            selThread.start();
        }
    }
}
