package org.fableStudios.tetris.renderables;

import java.awt.*;
import org.fableStudios.tetris.*;

public class GhostOutline implements Renderable {

    private float alpha = 1.0f;

    private int x = 0;

    private int y = 0;

    private float alphaStep = 0.05f;

    private boolean isStarted = false;

    private GhostBorder[][] grid;

    private int xOffset = 0;

    private int yOffset = 0;

    @Override
    public void render(Graphics g) {
        if (!isStarted || alpha <= 0) {
            isStarted = false;
            return;
        }
        g.setColor(new Color(0xFF, 0xFF, 0xFF, (int) (alpha * 255)));
        int i, j, x, y, squareHeight = TetrisPanel.squareHeight(), squareWidth = TetrisPanel.squareWidth();
        for (i = 0; i < grid.length; i++) for (j = 0; j < grid[i].length; j++) if (grid[i][j].hasBorder()) {
            x = TetrisPanel.getX(j + this.x + this.xOffset);
            y = TetrisPanel.getY(Main.panel.getBoardHeight() - this.y - i - this.yOffset - 1);
            if (grid[i][j].left) g.drawLine(x, y + TetrisPanel.squareHeight() - 1, x, y);
            if (grid[i][j].top) g.drawLine(x, y, x + TetrisPanel.squareWidth() - 1, y);
            if (grid[i][j].bottom) g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y + squareHeight - 1);
            if (grid[i][j].right) g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth - 1, y + 1);
        }
    }

    @Override
    public void update() {
        if (alpha > 0) alpha -= alphaStep; else {
            alpha = 0;
            isStarted = false;
        }
    }

    public void start(int x, int y, BaseTetrisPiece piece) {
        this.x = x;
        this.y = y;
        this.alpha = 1.0f;
        this.isStarted = true;
        int height = piece.maxY() - piece.minY() + 1;
        int width = piece.maxX() - piece.minX() + 1;
        this.xOffset = piece.minX();
        this.yOffset = piece.minY();
        grid = new GhostBorder[height][width];
        boolean[][] pieceGrid = new boolean[height][width];
        int i, j;
        for (i = 0; i < height; i++) for (j = 0; j < width; j++) {
            grid[i][j] = new GhostBorder();
            pieceGrid[i][j] = false;
        }
        for (i = 0; i < piece.maxCoords(); i++) {
            pieceGrid[piece.y(i) - yOffset][piece.x(i) - xOffset] = true;
        }
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                if (pieceGrid[i][j]) {
                    if (i > 0 && pieceGrid[i - 1][j]) grid[i][j].bottom = false; else grid[i][j].bottom = true;
                    if (i < height - 1 && pieceGrid[i + 1][j]) grid[i][j].top = false; else grid[i][j].top = true;
                    if (j > 0 && pieceGrid[i][j - 1]) grid[i][j].left = false; else grid[i][j].left = true;
                    if (j < width - 1 && pieceGrid[i][j + 1]) grid[i][j].right = false; else grid[i][j].right = true;
                }
            }
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void clear() {
        alpha = 0.0f;
    }
}
