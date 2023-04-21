package musite.ui.util;

import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.table.TableCellRenderer;
import javax.swing.plaf.basic.BasicTableUI;

public class CTableUI extends BasicTableUI {

    public void paint(Graphics g, JComponent c) {
        if (((CTable) table).map == null) {
            super.paint(g, c);
            return;
        }
        Rectangle r = g.getClipBounds();
        int firstRow = table.rowAtPoint(new Point(0, r.y));
        int lastRow = table.rowAtPoint(new Point(0, r.y + r.height));
        if (lastRow < 0) lastRow = table.getRowCount() - 1;
        for (int i = firstRow; i <= lastRow; i++) paintRow(i, g);
    }

    private void paintRow(int row, Graphics g) {
        Rectangle r = g.getClipBounds();
        int columns = table.getColumnCount();
        for (int i = 0; i < columns; i++) {
            Rectangle r1 = table.getCellRect(row, i, true);
            if (r1.intersects(r)) {
                int sk = ((CTable) table).map.visibleCell(row, i);
                paintCell(row, sk, g, r1);
                i += ((CTable) table).map.span(row, sk) - 1;
            }
        }
    }

    private void paintCell(int row, int column, Graphics g, Rectangle area) {
        int verticalMargin = table.getRowMargin();
        int horizontalMargin = table.getColumnModel().getColumnMargin();
        area.setBounds(area.x + horizontalMargin / 2, area.y + verticalMargin / 2, area.width - horizontalMargin, area.height - verticalMargin);
        if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
            Component component = table.getEditorComponent();
            component.setBounds(area);
            component.validate();
        } else {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component component = table.prepareRenderer(renderer, row, column);
            if (component.getParent() == null) rendererPane.add(component);
            rendererPane.paintComponent(g, component, table, area.x, area.y, area.width, area.height, true);
        }
        Color c = g.getColor();
        g.setColor(table.getGridColor());
        g.drawRect(area.x, area.y, area.width, area.height);
        g.setColor(c);
    }
}
