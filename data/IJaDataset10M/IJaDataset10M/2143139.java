package com.apeworks;

import java.util.Random;

/**
 * Implements the Tetris gameboard.
 * 
 * Supports clearing the field, setting and reading single fields. Removelines
 * supports deleting full lines and moving the rest down.
 * 
 * @author chris
 * 
 */
public class Board {

    int width;

    int height;

    int field[][];

    /**
	 * 
	 */
    public Board(int width, int height) {
        setWidth(width);
        setHeight(height);
        field = new int[height][width];
        clearField();
    }

    public boolean lineFull(int y) {
        for (int x = 0; x < width; x++) {
            if (field[y][x] == 0) return false;
        }
        return true;
    }

    public void copyLine(int srcY, int destY) {
        for (int x = 0; x < width; x++) {
            field[destY][x] = field[srcY][x];
        }
    }

    public void removeLine(int y) {
        for (int cY = y; cY > 0; cY--) {
            copyLine(cY - 1, cY);
        }
        for (int x = 0; x < width; x++) field[0][x] = 0;
    }

    /**
	 * Deletes full lines and moves the rest down Returns the number of rows
	 * removed.
	 * @param currentBlock the current block
	 * 
	 * @return
	 */
    public int removeFullLines(Block currentBlock) {
        int lines = 0;
        for (int y = currentBlock.getLowestAffected(); y > 0; y--) {
            if (lineFull(y)) {
                removeLine(height - 1);
                lines++;
                y++;
            }
        }
        return lines;
    }

    /**
	 * @return
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * @return
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * @param i
	 */
    public void setHeight(int i) {
        height = i;
    }

    /**
	 * @param i
	 */
    public void setWidth(int i) {
        width = i;
    }

    public void clearField() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[y][x] = 0;
            }
        }
    }

    public void setField(int x, int y, int color) {
        if (x >= 0 && x < width && y >= 0 && y < height) field[y][x] = color;
    }

    public int getField(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return field[y][x];
        } else {
            return 1;
        }
    }

    public void addRandomStones(int lines, float density) {
        for (int y = (int) (height * .6) - (int) (lines / 2); y < (height * .6) + (lines / 2); y++) {
            for (int x = 0; x < width - 1; x++) {
                if (Math.random() < density) field[y][x] = 1;
            }
        }
    }
}
