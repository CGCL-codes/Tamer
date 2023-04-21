package org.tiberiumlabs.lailaps.gui.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import org.tiberiumlabs.lailaps.gui.laf.LailapsMetalTheme;

/**
 *
 * @author <a href="mailto:paranoid.tiberiumlabs@gmail.com">Paranoid</a>
 */
public class RowNumberComponent extends JComponent implements MouseListener {

    private final JTable table;

    private final Font font;

    private final FontMetrics fontMetrics;

    private final Dimension preferredSize = new Dimension(15, 100);

    public RowNumberComponent(JTable table) {
        this.table = table;
        this.font = LailapsMetalTheme.monospacedFont;
        this.fontMetrics = table.getFontMetrics(font);
        this.addMouseListener(this);
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int rowCount = table.getRowCount();
        if (rowCount < 1) {
            return;
        }
        Font savedFont = g.getFont();
        Color savedColor = g.getColor();
        g.setFont(font);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int rowHeight = table.getRowHeight();
        Rectangle visibleRectangle = g.getClipBounds();
        int visibleY2 = visibleRectangle.y + visibleRectangle.height;
        int firstVisibleRow = visibleRectangle.y / rowHeight;
        int lastVisibleRow = Math.min(visibleY2 / rowHeight, table.getRowCount() - 1);
        int width = getWidth();
        int lesserWidth = width - 1;
        g.setColor(table.getGridColor());
        g.drawLine(lesserWidth, visibleRectangle.y, lesserWidth, visibleY2);
        Color alternativeBackground = getDarkerColor(getBackground());
        Color tableForeground = table.getForeground();
        String s;
        int stringLength = 0;
        int stringX = 0;
        int y = rowHeight * firstVisibleRow;
        int baseline = table.getBaseline(width, rowHeight) + y;
        int lesserRowHeight = rowHeight - 1;
        int row = firstVisibleRow;
        for (int i = firstVisibleRow; i <= lastVisibleRow; i++) {
            row++;
            if (row % 2 == 0) {
                g.setColor(alternativeBackground);
                g.fillRect(0, y, lesserWidth, lesserRowHeight);
            }
            y = y + rowHeight;
            s = Integer.toString(row);
            int l = s.length();
            if (stringLength != l) {
                stringX = width - (fontMetrics.stringWidth(s) + 3);
                stringLength = l;
            }
            g.setColor(tableForeground);
            g.drawString(s, stringX, baseline);
            baseline = baseline + rowHeight;
        }
        g.setColor(savedColor);
        g.setFont(savedFont);
    }

    @Override
    public int getHeight() {
        return table.getHeight();
    }

    @Override
    public int getWidth() {
        return fontMetrics.stringWidth(String.valueOf(table.getRowCount())) + 6;
    }

    @Override
    public Dimension getPreferredSize() {
        preferredSize.height = getHeight();
        preferredSize.width = getWidth();
        return preferredSize;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        if (table.getRowCount() < 1) {
            return null;
        }
        return Integer.toString(event.getY() / table.getRowHeight() + 1);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int rowNumber = e.getY() / table.getRowHeight();
            table.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private Color getDarkerColor(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return new Color((int) (red * 0.94), (int) (green * 0.94), (int) (blue * 0.94));
    }
}
