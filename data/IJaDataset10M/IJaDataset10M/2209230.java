package org.jogre.dots.client;

import java.awt.Color;
import java.awt.Graphics;
import org.jogre.client.awt.AbstractBoardComponent;

/**
 * @author Garrett Lehman
 * @version Alpha 0.2.3
 *
 * Dots board component.
 */
public class DotsBoardComponent extends AbstractBoardComponent {

    private DotsModel model = null;

    private int currentSeat = -1;

    /**
	 * Default constructor
	 *
	 * @param model
	 */
    public DotsBoardComponent(DotsModel model) {
        super(model.getRows(), model.getCols(), DotsModel.CELL_SIZE, DotsModel.CELL_SPACING, DotsModel.BORDER_WIDTH, 0, false, false, false);
        this.setColours(Color.white, Color.white, Color.white, Color.blue);
        this.model = model;
    }

    /**
	 * Set current seat at start of game
	 *
	 * @param currentSeat
	 */
    public void setCurrentSeat(int currentSeat) {
        this.currentSeat = currentSeat;
    }

    /**
	 * Update the graphics.
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMouseLine(g);
        drawCells(g);
        drawLastMoveLine(g);
        drawDots(g);
    }

    /**
	 * Override draw border.  Removes an additional border line.
	 *
	 * @see org.jogre.client.awt.AbstractBoardComponent#drawBorder(java.awt.Graphics)
	 */
    protected void drawBorder(Graphics g) {
        g.setColor(this.spacingColour);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        int size = getWidth();
        g.drawRect(0, 0, size - 1, size - 1);
    }

    /**
	 * Draw dots
	 *
	 * @param g
	 */
    public void drawDots(Graphics g) {
        int cols = this.model.getCols() + 1;
        int rows = this.model.getRows() + 1;
        int spacing = DotsModel.CELL_SIZE + DotsModel.CELL_SPACING;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                int x = (c * spacing) + DotsModel.BORDER_WIDTH;
                int y = (r * spacing) + DotsModel.BORDER_WIDTH;
                g.setColor(Color.black);
                g.fillRect(x + 1, y, DotsModel.CELL_SPACING - 2, DotsModel.CELL_SPACING);
                g.fillRect(x, y + 1, DotsModel.CELL_SPACING, DotsModel.CELL_SPACING - 2);
            }
        }
    }

    /**
	 * Draw mouse line
	 *
	 * @param g
	 */
    public void drawMouseLine(Graphics g) {
        if (currentSeat < 0) return;
        int col = this.model.getMouseCol();
        int row = this.model.getMouseRow();
        int location = this.model.getMouseLocation();
        drawLine(g, col, row, location, DotsModel.seatColor[this.currentSeat]);
    }

    /**
	 * Draw mouse line
	 *
	 * @param g
	 */
    public void drawLastMoveLine(Graphics g) {
        if (currentSeat < 0) return;
        int col = this.model.getLastMoveColumn();
        int row = this.model.getLastMoveRow();
        int location = this.model.getLastMoveLocation();
        drawLine(g, col, row, location, DotsModel.lastMoveColor);
    }

    public void drawLine(Graphics g, int col, int row, int location, Color color) {
        int spacing = DotsModel.CELL_SIZE + DotsModel.CELL_SPACING;
        if (col < 0 || row < 0 || location < 0) return;
        int x = (col * spacing) + DotsModel.BORDER_WIDTH;
        int y = (row * spacing) + DotsModel.BORDER_WIDTH;
        int width = spacing + DotsModel.CELL_SPACING;
        int height = DotsModel.CELL_SPACING;
        if (location == DotsCell.LOCATION_LEFT) {
            width = DotsModel.CELL_SPACING;
            height = spacing + DotsModel.CELL_SPACING;
        } else if (location == DotsCell.LOCATION_BOTTOM) {
            y += spacing;
        } else if (location == DotsCell.LOCATION_RIGHT) {
            x += spacing;
            width = DotsModel.CELL_SPACING;
            height = spacing + DotsModel.CELL_SPACING;
        }
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
	 * Draw cells
	 *
	 * @param g
	 */
    public void drawCells(Graphics g) {
        int cols = this.model.getCols();
        int rows = this.model.getRows();
        DotsCell[][] data = this.model.getData();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                drawCell(g, c, r, data[c][r]);
            }
        }
    }

    /**
	 * Draw cell based on column, row and cell data object
	 *
	 * @param g
	 * @param col
	 * @param row
	 * @param cell
	 */
    public void drawCell(Graphics g, int col, int row, DotsCell cell) {
        int spacing = DotsModel.CELL_SIZE + DotsModel.CELL_SPACING;
        Color lineColor = new Color(100, 100, 100);
        if (cell.isFilled(DotsCell.LOCATION_TOP)) {
            int x = (col * spacing) + DotsModel.BORDER_WIDTH;
            int y = (row * spacing) + DotsModel.BORDER_WIDTH;
            int width = spacing + DotsModel.CELL_SPACING;
            int height = DotsModel.CELL_SPACING;
            g.setColor(lineColor);
            g.fillRect(x, y, width, height);
        }
        if (cell.isFilled(DotsCell.LOCATION_LEFT)) {
            int x = (col * spacing) + DotsModel.BORDER_WIDTH;
            int y = (row * spacing) + DotsModel.BORDER_WIDTH;
            int width = DotsModel.CELL_SPACING;
            int height = spacing + DotsModel.CELL_SPACING;
            g.setColor(lineColor);
            g.fillRect(x, y, width, height);
        }
        if (cell.isFilled(DotsCell.LOCATION_BOTTOM)) {
            int x = (col * spacing) + DotsModel.BORDER_WIDTH;
            int y = (row * spacing) + DotsModel.BORDER_WIDTH + spacing;
            int width = spacing + DotsModel.CELL_SPACING;
            int height = DotsModel.CELL_SPACING;
            g.setColor(lineColor);
            g.fillRect(x, y, width, height);
        }
        if (cell.isFilled(DotsCell.LOCATION_RIGHT)) {
            int x = (col * spacing) + DotsModel.BORDER_WIDTH + spacing;
            int y = (row * spacing) + DotsModel.BORDER_WIDTH;
            int width = DotsModel.CELL_SPACING;
            int height = spacing + DotsModel.CELL_SPACING;
            g.setColor(Color.black);
            g.fillRect(x, y, width, height);
        }
        if (cell.isOwned()) {
            g.setColor(DotsModel.seatColor[cell.getOwnedBy()]);
            int x = (col * spacing) + DotsModel.BORDER_WIDTH + DotsModel.CELL_SPACING;
            int y = (row * spacing) + DotsModel.BORDER_WIDTH + DotsModel.CELL_SPACING;
            int width = DotsModel.CELL_SIZE;
            int height = DotsModel.CELL_SIZE;
            g.setColor(this.model.seatColor[cell.getOwnedBy()]);
            g.fillRect(x, y, width, height);
        }
    }
}
