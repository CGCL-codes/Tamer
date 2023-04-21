package org.columba.calendar.ui.list;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * 
 * 
 * @author fdietz
 */
public class CheckableList extends JTable {

    public CheckableList() {
        super();
        setTableHeader(null);
        setShowGrid(false);
        CheckableItemListTableModel model = new CheckableItemListTableModel();
        super.setModel(model);
        setRowHeight((int) new JCheckBox("test").getPreferredSize().getHeight() + 3);
    }

    public void setOptionsCellRenderer(TableCellRenderer renderer) {
        setDefaultRenderer(String.class, renderer);
    }

    private void initColumns() {
        DefaultTableColumnModel model = new DefaultTableColumnModel();
        TableColumn tc = new TableColumn(0);
        tc.setIdentifier("Boolean");
        tc.setMaxWidth(21);
        tc.setCellEditor(new CheckableListEditor());
        tc.setCellRenderer(new DefaultBooleanRenderer());
        model.addColumn(tc);
        tc = new TableColumn(1);
        tc.setIdentifier("String");
        tc.setCellRenderer(new DefaultStringRenderer());
        model.addColumn(tc);
        setColumnModel(model);
        setIntercellSpacing(new Dimension(2, 2));
    }

    /**
	 * @see javax.swing.JTable#setModel(javax.swing.table.TableModel)
	 * @overwrite
	 */
    public void setModel(TableModel model) {
        super.setModel(model);
        initColumns();
    }
}
