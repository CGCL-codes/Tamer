package net.sourceforge.crhtetris.panels;

import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;
import net.sourceforge.crhtetris.frames.TetrisFrame;
import net.sourceforge.crhtetris.i18n.Text;
import net.sourceforge.crhtetris.misc.TetrisColor;
import net.sourceforge.crhtetris.stones.Stone;

/**
 * This is the panel that contains the data of the game and is able to display it.
 * 
 * @author croesch
 */
public class TetrisInfoPanel implements Serializable {

    /** generated serial version UID */
    private static final long serialVersionUID = -8453884145216403955L;

    /** current score to visualise in this panel */
    private int currentScore = 0;

    /** current number of removed rows to visualise in this panel */
    private int removedRows = 0;

    /** current level to visualise in this panel */
    private int currentLevel = 1;

    /** the width of this panel */
    private final int width;

    /**
   * Constructs the info panel to display the level, rows and the score
   * 
   * @author croesch
   * @since Date: 19.01.2011
   * @param widthOfPanel the width that is available for displaying information
   */
    public TetrisInfoPanel(final int widthOfPanel) {
        this.width = widthOfPanel;
    }

    /**
   * @return the number of removed rows
   */
    public final int getRows() {
        return this.removedRows;
    }

    /**
   * Increments the number of removed rows.
   * 
   * @param rows the number of rows the counter should be incremented
   * @throws IllegalArgumentException if the number of rows is negative or higher than four
   */
    public final void addRows(final int rows) throws IllegalArgumentException {
        if (rows < 0 || rows > 4) {
            throw new IllegalArgumentException(Text.MSG_ILLEGAL_ROWS.text(Integer.valueOf(rows)));
        }
        this.removedRows += rows;
    }

    /**
   * Increments the current score.
   * 
   * @param points number of points to add to the score
   * @throws IllegalArgumentException if the number of points is negative
   */
    public final void addScore(final int points) throws IllegalArgumentException {
        if (points < 0) {
            throw new IllegalArgumentException(Text.MSG_ILLEGAL_POINTS.text(Integer.valueOf(points)));
        }
        this.currentScore += points;
    }

    /**
   * @return the current points stored in this panel
   */
    public final int getScore() {
        return this.currentScore;
    }

    /**
   * sets the current level to the given level.
   * 
   * @param level the new level
   */
    public final void setLevel(final int level) {
        if (level <= 0) {
            throw new IllegalArgumentException(Text.MSG_ILLEGAL_LEVEL.text(Integer.valueOf(level)));
        }
        this.currentLevel = level;
    }

    /**
   * @return the current level
   */
    public final int getLevel() {
        return this.currentLevel;
    }

    /**
   * resets the data in this panel, score to 0, rows too and level to 1.
   */
    public final void reset() {
        this.currentScore = 0;
        this.removedRows = 0;
        setLevel(1);
    }

    /** the number of positions in y-axis to calculate the different positions */
    private static final int NUMBER_OF_Y_POSITIONS = 16;

    /** the position of the description for the score value */
    private static final int SCORE_DESC_POS = 4;

    /** the position of the score value */
    private static final int SCORE_INFO_POS = SCORE_DESC_POS + 1;

    /** the position of the description for the removed rows value */
    private static final int RROWS_DESC_POS = 7;

    /** the position of the removed rows value */
    private static final int RROWS_INFO_POS = RROWS_DESC_POS + 1;

    /** the position of the description for the level value */
    private static final int LEVEL_DESC_POS = 10;

    /** the position of the level value */
    private static final int LEVEL_INFO_POS = LEVEL_DESC_POS + 1;

    /**
   * Paints the information to the given Graphics. The height of the tetris field is needed to calculate the position in
   * y-axis.
   * 
   * @param g the Graphics to paint the information on it.
   */
    public final void paintInfo(final Graphics g) {
        g.setColor(TetrisColor.getForeground());
        paintInfo(g, SCORE_DESC_POS, SCORE_INFO_POS, Text.INFO_SCORE, getScore(), this.width);
        paintInfo(g, RROWS_DESC_POS, RROWS_INFO_POS, Text.INFO_ROWS, getRows(), this.width);
        paintInfo(g, LEVEL_DESC_POS, LEVEL_INFO_POS, Text.INFO_LEVEL, getLevel(), this.width);
    }

    /**
   * Paints the given information to the given position on the panel
   * 
   * @author croesch
   * @since Date: 20.02.2011
   * @param g the {@link Graphics} to paint to
   * @param descPos the vertical position of the description
   * @param infoPos the vertical position of the information
   * @param desc the description
   * @param info the information
   * @param width the with of the panel, to calculate the horizontal position
   */
    private static void paintInfo(final Graphics g, final int descPos, final int infoPos, final Text desc, final int info, final int width) {
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.drawString(desc.text(), Stone.SIZE, TetrisFrame.FIELD_HEIGHT / NUMBER_OF_Y_POSITIONS * descPos);
        g.setFont(g.getFont().deriveFont(Font.PLAIN));
        final String value = String.valueOf(info);
        g.drawString(value, calculateXPos(g, value, width), TetrisFrame.FIELD_HEIGHT / NUMBER_OF_Y_POSITIONS * infoPos);
    }

    /**
   * Calculates the starting position of the given string to centre this string.
   * 
   * @author croesch
   * @since Date: 20.02.2011
   * @param g the {@link Graphics} that provide the font metrics
   * @param s the string to paint
   * @param w the width of the place where the string should be printed
   * @return the starting position to be able to centre the string
   */
    private static int calculateXPos(final Graphics g, final String s, final int w) {
        return (w - g.getFontMetrics().stringWidth(s)) / 2;
    }
}
