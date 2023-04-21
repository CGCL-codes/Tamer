package net.sf.swinggoodies.components.tables;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SGTable extends JTable {

    private static final long serialVersionUID = 1L;

    DefaultTableCellRenderer cellRender = null;

    public SGTable() {
        super();
    }

    /**
	 * Construtor padrão
	 * @param hql
	 * @param columns
	 */
    public SGTable(String hql, String[] columns) {
        super();
        setFocusable(false);
        setModel(new SGTableModel(hql, columns));
        AutofitTableColumns.autoResizeTable(this, true);
        setCellRenderer();
    }

    /**
	 * Zebra o grid
	 * @param cell
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
    public Component RenderizaCores(Component cell, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!isSelected) {
            if (row % 2 == 0) {
                cell.setBackground(Color.white);
            } else {
                cell.setBackground(new Color(238, 246, 255));
            }
        } else {
            cell.setBackground(new Color(164, 200, 238));
        }
        return cell;
    }

    private void setCellRenderer() {
        cellRender = new DefaultTableCellRenderer() {

            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return RenderizaCores(super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column), table, value, isSelected, hasFocus, row, column);
            }
        };
        try {
            setDefaultRenderer(Class.forName("java.lang.Object"), cellRender);
        } catch (Exception Ex) {
        }
    }
}
