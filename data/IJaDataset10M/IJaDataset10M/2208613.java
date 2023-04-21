package shogi;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author novaterata
 */
public class ShogiSquare extends JPanel {

    private Color squareColor;

    private int index;

    public ShogiSquare(Color squareColor, int index, LayoutManager layout) {
        super(layout);
        this.squareColor = squareColor;
        this.index = index;
    }

    public ShogiSquare(Color squareColor, int index) {
        this.squareColor = squareColor;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Color getSquareColor() {
        return squareColor;
    }

    public void setSquareColor(Color squareColor) {
        this.squareColor = squareColor;
    }
}
