package com.karpouzas.reversiui.utils;

/**
 * represents window bounds
 * @author George Karpouzas
 */
public class WindowBounds extends Bounds {

    private int _squareSize;

    /**
     * constructor specifying x, y, width and height of window
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public WindowBounds(int x, int y, int width, int height, int squareSize) {
        super(x, y, width, height);
        _squareSize = squareSize < 0 ? 10 : squareSize;
    }

    /**
     * get square size (board)
     * @return integer
     */
    public int getSquareSize() {
        return _squareSize;
    }

    /**
     * set square size (board)
     * @param value
     */
    public void setSquareSize(int value) {
        _squareSize = value < 0 ? 10 : value;
    }
}
