package org.schwiet.LincolnLog.ui.delegates;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import org.schwiet.LincolnLog.ui.color.UnselectedHeaderColors;
import org.schwiet.LincolnLog.ui.painters.TableHeaderPainter;
import org.schwiet.LincolnLog.ui.utils.TableHeaderUtils;
import org.schwiet.LincolnLog.ui.utils.TableUtils;
import org.schwiet.spill.painter.Painter;
import org.schwiet.spill.plaf.IndentLabelUI;
import org.schwiet.spill.plaf.IndentLabelUI.IndentDirection;

/**
 *
 * @author sethschwiethale
 */
public class TransactionTableHeaderRenderer extends JLabel implements TableCellRenderer {

    private JTable tableRef;

    private int fColumnModelIndexBeingPainted = -1;

    private static final int SORT_ICON_INDENT_PIXELS = 6;

    private static final Color SORT_ICON_COLOR = new Color(0, 0, 0, 175);

    private final Painter<Component> regPainter = new TableHeaderPainter(new UnselectedHeaderColors());

    public TransactionTableHeaderRenderer(JTable table) {
        tableRef = table;
        this.setUI(new IndentLabelUI(IndentDirection.UP));
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            setText(value.toString());
            setFont(tableRef.getTableHeader().getFont());
        }
        fColumnModelIndexBeingPainted = 0 <= column && column < tableRef.getColumnCount() ? tableRef.convertColumnIndexToModel(column) : -1;
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g.create();
        regPainter.paint(graphics2d, this, getWidth(), getHeight());
        super.paintComponent(g);
        paintSortIndicatorIfNecessary(graphics2d);
        graphics2d.dispose();
    }

    private void paintSortIndicatorIfNecessary(Graphics2D graphics2d) {
        TableUtils.SortDirection sortDirection = getColumnBeingPaintedSortDirection();
        if (sortDirection != TableUtils.SortDirection.NONE) {
            paintSortIndicator(graphics2d, sortDirection);
        }
    }

    private void paintSortIndicator(Graphics2D graphics2d, TableUtils.SortDirection sortDirection) {
        Shape sortShape = sortDirection == TableUtils.SortDirection.ASCENDING ? createSortAscendingShape() : createSortDescendingShape();
        int x = getWidth() - sortShape.getBounds().width - SORT_ICON_INDENT_PIXELS;
        int y = getHeight() / 2 - sortShape.getBounds().height / 2;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.translate(x, y);
        graphics2d.setColor(SORT_ICON_COLOR);
        graphics2d.fill(sortShape);
    }

    private TableUtils.SortDirection getColumnBeingPaintedSortDirection() {
        return TableHeaderUtils.getSortDirection(tableRef.getTableHeader(), fColumnModelIndexBeingPainted);
    }

    private static GeneralPath createSortAscendingShape() {
        float width = 7;
        float height = 6;
        GeneralPath path = new GeneralPath();
        path.moveTo(width / 2.0f, 0.0f);
        path.lineTo(width, height);
        path.lineTo(0.0f, height);
        path.lineTo(width / 2.0f, 0.0f);
        return path;
    }

    private static GeneralPath createSortDescendingShape() {
        GeneralPath path = createSortAscendingShape();
        double centerX = path.getBounds2D().getWidth() / 2.0;
        double centerY = path.getBounds2D().getHeight() / 2.0;
        path.transform(AffineTransform.getRotateInstance(Math.PI, centerX, centerY));
        return path;
    }
}
