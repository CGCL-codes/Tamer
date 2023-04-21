package org.isakiev.xl.view.sheet;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;

/**
 * Lightweight UI component for sheet rendering.
 * 
 * @author Ruslan Isakiev
 */
public class Sheet extends JComponent implements SheetModelListener, SheetInteractionListener, SheetAppearanceListener {

    private static final long serialVersionUID = 1L;

    private SheetModel model;

    private SheetInteractionModel interactionModel;

    private SheetAppearance appearance;

    private FontMetrics fontMetrics;

    private int rowHeaderWidth;

    private int columnHeaderHeight;

    private int visibleRowsNumber;

    private int visibleColumnsNumber;

    private Coordinates coords;

    public Sheet(SheetModel model, SheetInteractionModel interactionModel, SheetAppearance appearance) {
        this.model = model;
        this.interactionModel = interactionModel;
        this.appearance = appearance;
        model.addSheetModelListener(this);
        interactionModel.addSheetInteractionListener(this);
        appearance.addSheetAppearanceListener(this);
        processAppearanceChanged();
        setFocusable(true);
        repaint();
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                processKeyPressed(e);
            }
        });
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                processMousePressed(e);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                processMouseWheel(e);
            }
        });
    }

    public void setModel(SheetModel newModel) {
        model.removeSheetModelListener(this);
        model = newModel;
        model.addSheetModelListener(this);
        repaint();
    }

    public void setInteractionModel(SheetInteractionModel newInteractionModel) {
        interactionModel.removeSheetInteractionListener(this);
        interactionModel = newInteractionModel;
        interactionModel.addSheetInteractionListener(this);
        repaint();
    }

    public void setAppearance(SheetAppearance newAppearance) {
        appearance.removeSheetAppearanceListener(this);
        appearance = newAppearance;
        appearance.addSheetAppearanceListener(this);
        repaint();
    }

    public SheetInteractionModel getInteractionModel() {
        return interactionModel;
    }

    public SheetAppearance getAppearance() {
        return appearance;
    }

    private void processAppearanceChanged() {
        rowHeaderWidth = appearance.getColumnWidth();
        columnHeaderHeight = appearance.getRowHeight();
        fontMetrics = getFontMetrics(appearance.getFont());
    }

    private class Coordinates {

        int topLeftX;

        int topLeftY;

        int bottomRightX;

        int bottomRightY;

        int contentX;

        int contentY;

        int contentWidth;

        int contentHeight;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        coords = new Coordinates();
        Insets insets = getInsets();
        coords.topLeftX = insets.left;
        coords.topLeftY = insets.top;
        coords.bottomRightX = getWidth() - insets.right - 1;
        coords.bottomRightY = getHeight() - insets.bottom - 1;
        coords.contentX = coords.topLeftX + rowHeaderWidth;
        coords.contentY = coords.topLeftY + columnHeaderHeight;
        coords.contentWidth = coords.bottomRightX - coords.contentX + 1;
        coords.contentHeight = coords.bottomRightY - coords.contentY + 1;
        this.visibleRowsNumber = coords.contentHeight / appearance.getRowHeight() + 1;
        this.visibleColumnsNumber = coords.contentWidth / appearance.getColumnWidth() + 1;
        interactionModel.setViewportSize(visibleRowsNumber, visibleColumnsNumber);
        drawBackground(g, coords);
        drawLines(g, coords);
        drawHeader(g, coords);
        drawContent(g, coords);
    }

    private void drawBackground(Graphics g, Coordinates c) {
        g.setColor(appearance.getHeaderBackgroundColor());
        g.fillRect(c.topLeftX, c.topLeftY, c.bottomRightX - c.topLeftX + 1, columnHeaderHeight);
        g.fillRect(c.topLeftX, c.topLeftY, rowHeaderWidth, c.bottomRightY - c.topLeftY + 1);
        g.setColor(appearance.getCellBackgoundColor());
        g.fillRect(c.topLeftX + rowHeaderWidth, c.topLeftY + columnHeaderHeight, c.contentWidth, c.contentHeight);
    }

    private void drawLines(Graphics g, Coordinates c) {
        g.setColor(appearance.getLineColor());
        for (int i = 0; i < visibleRowsNumber; i++) {
            int y = c.contentY + i * appearance.getRowHeight();
            if (y < c.bottomRightY) {
                g.drawLine(c.contentX, y, c.bottomRightX, y);
            }
        }
        for (int i = 0; i < visibleColumnsNumber; i++) {
            int x = c.contentX + i * appearance.getColumnWidth();
            if (x < c.bottomRightX) {
                g.drawLine(x, c.contentY, x, c.bottomRightY);
            }
        }
        g.setColor(appearance.getHeaderLineColor());
        g.drawRect(c.topLeftX, c.topLeftY, c.bottomRightX - c.topLeftX + 1, c.bottomRightY - c.topLeftY + 1);
        g.drawRect(c.topLeftX, c.topLeftY, c.bottomRightX - c.topLeftX + 1, columnHeaderHeight);
        g.drawRect(c.topLeftX, c.topLeftY, rowHeaderWidth, c.bottomRightY - c.topLeftY + 1);
        for (int i = 0; i < visibleRowsNumber; i++) {
            int y = c.contentY + i * appearance.getRowHeight();
            if (y < c.bottomRightY) {
                g.drawLine(c.topLeftX, y, c.topLeftX + rowHeaderWidth, y);
            }
        }
        for (int i = 0; i < visibleColumnsNumber; i++) {
            int x = c.contentX + i * appearance.getColumnWidth();
            if (x < c.bottomRightX) {
                g.drawLine(x, c.topLeftY, x, c.topLeftY + columnHeaderHeight);
            }
        }
    }

    private void drawHeader(Graphics g, Coordinates c) {
        if (!interactionModel.isSelectionEmpty()) {
            if (0 <= interactionModel.getRelativeSelectedRow() && interactionModel.getRelativeSelectedRow() < visibleRowsNumber) {
                int selectedY = c.contentY + interactionModel.getRelativeSelectedRow() * appearance.getRowHeight();
                g.setColor(appearance.getSelectedHeaderBackgroundColor());
                g.fillRect(c.topLeftX, selectedY, rowHeaderWidth, appearance.getRowHeight());
                g.setColor(appearance.getSelectedHeaderLineColor());
                g.drawRect(c.topLeftX, selectedY, rowHeaderWidth, appearance.getRowHeight());
            }
            if (0 <= interactionModel.getRelativeSelectedColumn() && interactionModel.getRelativeSelectedColumn() < visibleColumnsNumber) {
                int selectedX = c.contentX + interactionModel.getRelativeSelectedColumn() * appearance.getColumnWidth();
                g.setColor(appearance.getSelectedHeaderBackgroundColor());
                g.fillRect(selectedX, c.topLeftY, appearance.getColumnWidth(), columnHeaderHeight);
                g.setColor(appearance.getSelectedHeaderLineColor());
                g.drawRect(selectedX, c.topLeftY, appearance.getColumnWidth(), columnHeaderHeight);
            }
        }
        g.setFont(appearance.getFont());
        g.setColor(appearance.getHeaderFontColor());
        int startingRow = interactionModel.getViewportPositionRow();
        int startingColumn = interactionModel.getViewportPositionColumn();
        for (int i = 0; i < visibleRowsNumber; i++) {
            int y = c.contentY + (i + 1) * appearance.getRowHeight();
            String rowName = model.getRowName(startingRow + i);
            drawString(g, rowName, c.topLeftX, y, rowHeaderWidth, true);
        }
        for (int i = 0; i < visibleColumnsNumber; i++) {
            int x = c.contentX + i * appearance.getColumnWidth();
            String columnName = model.getColumnName(startingColumn + i);
            drawString(g, columnName, x, c.contentY, appearance.getColumnWidth(), true);
        }
    }

    private void drawContent(Graphics g, Coordinates c) {
        if (0 <= interactionModel.getRelativeSelectedRow() && interactionModel.getRelativeSelectedRow() < visibleRowsNumber && 0 <= interactionModel.getRelativeSelectedColumn() && interactionModel.getRelativeSelectedColumn() < visibleColumnsNumber) {
            drawCell(g, c, interactionModel.getSelectedRow(), interactionModel.getSelectedColumn(), interactionModel.getRelativeSelectedRow(), interactionModel.getRelativeSelectedColumn(), null);
        }
        int startingRow = interactionModel.getViewportPositionRow();
        int startingColumn = interactionModel.getViewportPositionColumn();
        for (int relativeRow = 0; relativeRow < visibleRowsNumber; relativeRow++) {
            for (int relativeColumn = 0; relativeColumn < visibleColumnsNumber; relativeColumn++) {
                int row = startingRow + relativeRow;
                int column = startingColumn + relativeColumn;
                SheetCellValue value = model.getValueAt(row, column);
                if (value != null) {
                    drawCell(g, c, row, column, relativeRow, relativeColumn, value);
                }
            }
        }
    }

    private void drawCell(Graphics g, Coordinates c, int row, int column, int relativeRow, int relativeColumn, SheetCellValue value) {
        int x = c.contentX + relativeColumn * appearance.getColumnWidth();
        int y = c.contentY + relativeRow * appearance.getRowHeight();
        if (value != null) {
            if (!value.isValid()) {
                g.setColor(appearance.getInvalidCellBackgroundColor());
            } else if (value.isErroneous()) {
                g.setColor(appearance.getErroneousCellBackgroundColor());
            } else {
                g.setColor(appearance.getCellBackgoundColor());
            }
            g.fillRect(x + 1, y + 1, appearance.getColumnWidth() - 1, appearance.getRowHeight() - 1);
            g.setFont(appearance.getFont());
            g.setColor(appearance.getCellFontColor());
            drawString(g, value.getText(), x, y + appearance.getRowHeight(), appearance.getColumnWidth(), false);
        } else {
            g.setColor(appearance.getCellBackgoundColor());
            g.fillRect(x + 1, y + 1, appearance.getColumnWidth() - 1, appearance.getRowHeight() - 1);
        }
        if (row == interactionModel.getSelectedRow() && column == interactionModel.getSelectedColumn()) {
            g.setColor(appearance.getSelectedCellBorderColor());
            for (int i = 0; i < appearance.getSelectedCellBorderWidth(); i++) {
                g.drawRect(x + i, y + i, appearance.getColumnWidth() - i * 2, appearance.getRowHeight() - i * 2);
            }
        }
    }

    private void drawString(Graphics g, String s, int cellX, int cellBottomY, int cellWidth, boolean centerHorizontally) {
        String targetString = fitString(s, cellWidth - appearance.getLeftTextOffset());
        int y = cellBottomY - appearance.getBottomTextOffset();
        int x = cellX;
        if (centerHorizontally) {
            int stringWidth = fontMetrics.stringWidth(targetString);
            x += (cellWidth - stringWidth) / 2;
        } else {
            x += appearance.getLeftTextOffset();
        }
        g.drawString(targetString, x, y);
    }

    private String fitString(String s, int width) {
        int stringWidth = fontMetrics.stringWidth(s);
        if (stringWidth <= width) {
            return s;
        }
        final String dots = "..";
        width -= fontMetrics.stringWidth(dots);
        char[] chars = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        int count = 0;
        int totalWidth = fontMetrics.charWidth(chars[0]);
        while (totalWidth < width) {
            count++;
            totalWidth += fontMetrics.charWidth(chars[count]);
        }
        return s.substring(0, count) + dots;
    }

    private void processKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            interactionModel.shiftSelection(0, -1, false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            interactionModel.shiftSelection(0, 1, false);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            interactionModel.shiftSelection(-1, 0, false);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            interactionModel.shiftSelection(1, 0, false);
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            if (scrollHorizontally(e)) {
                interactionModel.shiftSelection(0, -visibleColumnsNumber, true);
            } else {
                interactionModel.shiftSelection(-visibleRowsNumber, 0, true);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            if (scrollHorizontally(e)) {
                interactionModel.shiftSelection(0, visibleColumnsNumber, true);
            } else {
                interactionModel.shiftSelection(visibleRowsNumber, 0, true);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_HOME) {
            interactionModel.setViewportPosition(0, 0);
            interactionModel.setSelection(0, 0);
        }
    }

    private boolean scrollHorizontally(InputEvent e) {
        return e.isShiftDown();
    }

    private void processMousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (coords.contentX < x && x < coords.bottomRightX && coords.contentY < y && y < coords.bottomRightY) {
            int selectedRow = (y - coords.contentY) / appearance.getRowHeight();
            int selectedColumn = (x - coords.contentX) / appearance.getColumnWidth();
            interactionModel.setRelativeSelection(selectedRow, selectedColumn);
        }
        requestFocus();
    }

    private void processMouseWheel(MouseWheelEvent e) {
        boolean horizontal = scrollHorizontally(e);
        interactionModel.shiftSelection(horizontal ? 0 : e.getWheelRotation(), horizontal ? e.getWheelRotation() : 0, true);
    }

    @Override
    public void valueChanged(int row, int column) {
        int relativeRow = row - interactionModel.getViewportPositionRow();
        int relativeColumn = column - interactionModel.getViewportPositionColumn();
        if (0 <= relativeRow && relativeRow < interactionModel.getVerticalViewportSize() && 0 <= relativeColumn && relativeColumn < interactionModel.getHorizontalViewportSize()) {
            SheetCellValue value = model.getValueAt(row, column);
            drawCell(getGraphics(), coords, row, column, relativeRow, relativeColumn, value);
        }
    }

    @Override
    public void modelChanged() {
        repaint();
    }

    @Override
    public void appearanceChanged() {
        processAppearanceChanged();
        repaint();
    }

    @Override
    public void selectionChanged() {
        repaint();
    }

    @Override
    public void viewportPositionChanged() {
        repaint();
    }

    @Override
    public void viewportSizeChanged() {
    }
}
