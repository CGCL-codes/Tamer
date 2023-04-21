package org.digitall.lib.components.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class BasicHeaderRenderer extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {

    private Rectangle paintingRect = null;

    private Rectangle lastPaintingRect = null;

    private JTableHeader header = null;

    private boolean isOnCol = false;

    private boolean highlightClickedCol = false;

    private int paintingCol = -1;

    private int clickedCol = -1;

    private int currentCol = -1;

    /**
     * Buffer gradient paint.
     */
    GradientPaint gp = null, hoverGradient, columnGradient;

    /**
     * The current sorting state of the columns
     */
    public BasicHeaderRenderer() {
        super();
        setFont(getFont().deriveFont(Font.BOLD));
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 230)));
        setHighlightClickedColumn(true);
        setToolTipText("Click para ordenar");
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    }

    public void setColumnGradient(GradientPaint gp) {
        this.columnGradient = gp;
    }

    public void setHoverGradient(GradientPaint gp) {
        this.hoverGradient = gp;
    }

    public GradientPaint getColumnGradient() {
        return columnGradient;
    }

    public GradientPaint getHoverGradient() {
        return hoverGradient;
    }

    public void setHighlightClickedColumn(boolean b) {
        highlightClickedCol = b;
    }

    public void paintComponent(Graphics g) {
        Rectangle rect = paintingRect;
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(gp);
        g2.fillRect(0, 0, rect.width, rect.height);
        if (currentCol == clickedCol) {
        }
        super.paintComponent(g);
    }

    public void attachListener() {
        header.addMouseListener(this);
        header.addMouseMotionListener(this);
    }

    public void mouseEntered(MouseEvent e) {
        isOnCol = true;
    }

    public void mouseExited(MouseEvent e) {
        isOnCol = false;
        paintingCol = -1;
        header.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        clickedCol = header.columnAtPoint(e.getPoint());
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        paintingCol = header.columnAtPoint(e.getPoint());
        paintingRect = header.getHeaderRect(paintingCol);
        header.repaint(paintingRect.x, paintingRect.y, paintingRect.width, paintingRect.height);
        if (lastPaintingRect != null) {
            header.repaint(lastPaintingRect.x, lastPaintingRect.y, lastPaintingRect.width, lastPaintingRect.height);
        }
        lastPaintingRect = paintingRect;
    }

    public void mouseDragged(MouseEvent e) {
        paintingCol = header.columnAtPoint(e.getPoint());
        paintingRect = header.getHeaderRect(paintingCol);
        header.repaint(paintingRect.x, paintingRect.y, paintingRect.width, paintingRect.height);
        if (lastPaintingRect != null) {
            header.repaint(lastPaintingRect.x, lastPaintingRect.y, lastPaintingRect.width, lastPaintingRect.height);
        }
        lastPaintingRect = paintingRect;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        currentCol = col;
        if (header == null) {
            header = table.getTableHeader();
            attachListener();
        }
        Rectangle rect = table.getTableHeader().getHeaderRect(col);
        if ((isOnCol && paintingCol == col) || (clickedCol == col && highlightClickedCol)) {
            gp = new GradientPaint(rect.x, rect.y + rect.height, BasicConfig.TABLE_HEADER_SELECTED_GRADIENT_START_COLOR, rect.x, rect.y, BasicConfig.TABLE_HEADER_SELECTED_GRADIENT_END_COLOR);
            setForeground(BasicConfig.TABLE_HEADER_SELECTED_FOREGROUND_COLOR);
        } else {
            gp = new GradientPaint(rect.x, rect.y + rect.height, BasicConfig.TABLE_HEADER_NOT_SELECTED_GRADIENT_START_COLOR, rect.x, rect.y, BasicConfig.TABLE_HEADER_NOT_SELECTED_GRADIENT_END_COLOR);
            setForeground(BasicConfig.TABLE_HEADER_NOT_SELECTED_FOREGROUND_COLOR);
        }
        paintingRect = rect;
        setText(value == null ? "" : value.toString());
        return this;
    }
}
