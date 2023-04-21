package util.colors;

import java.awt.*;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ColorModel2 extends AbstractTableModel {

    private final List<ColorNode2> colors;

    public ColorModel2(List<ColorNode2> colors) {
        this.colors = colors;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public int getRowCount() {
        return colors.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        ColorNode2 node = colors.get(row);
        switch(col) {
            case 0:
                return node.name;
            case 1:
                return node.color;
            case 2:
                return node.background;
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        switch(col) {
            case 0:
                return "Name";
            case 1:
                return "Color";
            case 2:
                return "Background";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        switch(col) {
            default:
                return;
            case 1:
                colors.get(row).color = (Color) value;
                break;
            case 2:
                colors.get(row).background = (Color) value;
                break;
        }
        fireTableCellUpdated(row, col);
    }
}
