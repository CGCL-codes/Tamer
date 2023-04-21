package game;

import java.awt.*;

public class DefaultCanvas extends Canvas {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DrawingGameBoard drawingGameBoard;

    private Graphics _gc;

    public void setDrawingGameBoard(DrawingGameBoard drawingGameBoard) {
        this.drawingGameBoard = drawingGameBoard;
        _gc = getGraphics();
    }

    public void paint(Graphics g) {
        try {
            drawingGameBoard.paint(g);
        } catch (NullPointerException e) {
        }
    }

    public void repaint() {
        paint(_gc);
    }
}
