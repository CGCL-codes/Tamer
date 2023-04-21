package org.jogre.quetris.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import org.jogre.client.awt.AbstractBoardComponent;
import org.jogre.client.awt.GameImages;
import org.jogre.client.awt.JogreAwt;

/**
 * Quetris grid component which displays the various different 
 * quetris pieces on the screen.
 *
 * @author Bob Marks
 * @version Beta 0.3
 */
public class QuetrisGridComponent extends AbstractBoardComponent {

    private static final int NUM_OF_COLS = QuetrisPlayerModel.NUM_OF_COLS;

    private static final int NUM_OF_ROWS = QuetrisPlayerModel.NUM_OF_ROWS;

    private static final int CELL_SIZE = 16;

    private static final int CELL_SPACING = 0;

    private static final int BORDER_WIDTH = 10;

    private QuetrisPlayerModel quetrisPlayerModel;

    private static final Color bgColor = new Color(240, 240, 240);

    private Color color1, color2;

    /**
     * Constructor for a quetris grid component.
     */
    public QuetrisGridComponent(QuetrisPlayerModel quetrisPlayerModel, Color color1, Color color2) {
        super(NUM_OF_ROWS, NUM_OF_COLS, CELL_SIZE, CELL_SPACING, BORDER_WIDTH, 0, false, false, false);
        this.color1 = color1;
        this.color2 = color2;
        setColours(bgColor, bgColor, new Color(64, 64, 64), new Color(250, 250, 250));
        this.quetrisPlayerModel = quetrisPlayerModel;
        quetrisPlayerModel.addObserver(this);
    }

    /** 
	 * Method used for udpating the background.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        JogreAwt.drawVerticalGradiant(g, BORDER_WIDTH, BORDER_WIDTH, getWidth() - BORDER_WIDTH - 1, getHeight() - BORDER_WIDTH, color1, color2);
        for (int i = 0; i < QuetrisPlayerModel.NUM_OF_COLS; i++) {
            for (int j = 0; j < QuetrisPlayerModel.NUM_OF_ROWS; j++) {
                Point sCoords = getScreenCoords(i, j);
                int block = quetrisPlayerModel.getGridShapeNum(i, j) + 1;
                if (block > 0) {
                    g.drawImage(GameImages.getImage(block), sCoords.x, sCoords.y, null);
                }
            }
        }
    }
}
