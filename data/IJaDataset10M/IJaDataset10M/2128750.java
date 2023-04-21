package ba_leipzig_lending_and_service_control_system.view.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

/**
 * Control-Class to create a JTable object that can span different rows and columns
 *
 * @author Chris Hagen
 */
public class JSpanTable extends JTable {

    private CMap map;

    /**
     * Creates a new instance of JSpanTable
     *
     * @param map the CMap-Modell of the table
     */
    public JSpanTable(CMap map) {
        super();
        this.map = map;
        setUI(new CTUI());
    }

    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        if (map == null) return super.getCellRect(row, column, includeSpacing);
        int sk = map.visibleCell(row, column);
        Rectangle r1 = super.getCellRect(sk, column, includeSpacing);
        if (map.span(sk, column) != 1) {
            for (int i = 1; i < map.span(sk, column); i++) r1.height += getRowHeight(sk + i);
        }
        return r1;
    }

    public int rowAtPoint(Point p) {
        int y = super.rowAtPoint(p);
        if (y < 0) return y;
        int x = super.columnAtPoint(p);
        return map.visibleCell(y, x);
    }

    /**
     * Inner support-class to create the UI
     */
    private class CTUI extends BasicTableUI {

        public void paint(Graphics g, JComponent c) {
            Rectangle r = g.getClipBounds();
            int firstCol = table.columnAtPoint(new Point(r.x, 0));
            int lastCol = table.columnAtPoint(new Point(r.x + r.width, 0));
            if (lastCol < 0) lastCol = table.getColumnCount() - 1;
            for (int i = firstCol; i <= lastCol; i++) paintCol(i, g);
        }

        /**
         * Paints one column
         *
         * @param col column number
         * @param g graphics context
         */
        private void paintCol(int col, Graphics g) {
            Rectangle r = g.getClipBounds();
            for (int i = 0; i < table.getRowCount(); i++) {
                Rectangle r1 = table.getCellRect(i, col, true);
                if (r1.intersects(r)) {
                    int sk = map.visibleCell(i, col);
                    paintCell(sk, col, g, r1);
                    i += ((JSpanTable) table).map.span(sk, col) - 1;
                }
            }
        }

        /**
         * Paints one cell
         *
         * @param row row number of the component to be painted
         * @param column coulmn number of the component to be painted
         * @param g graphics context
         * @param area rectangle with the cell position and size
         */
        private void paintCell(int row, int column, Graphics g, Rectangle area) {
            int verticalMargin = table.getRowMargin();
            int horizontalMargin = table.getColumnModel().getColumnMargin();
            Color c = g.getColor();
            g.setColor(table.getGridColor());
            g.drawRect(area.x, area.y, area.width - 1, area.height - 1);
            g.setColor(c);
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
        }
    }

    /**
     * Inner interface to provide the functions for getting the span model
     */
    public interface CMap {

        /**
         * Gets the number of spanned columns
         *
         * @param row logical cell row
         * @param column logical cell column
         * @return number of columns spanned a cell
         */
        int span(int row, int column);

        /**
         * Gets the number of the cell spanning the neighbour cells
         *
         * @param row logical cell row
         * @param column logical cell column
         * @return the index of a visible cell covering a logical cell
         */
        int visibleCell(int row, int column);
    }
}
