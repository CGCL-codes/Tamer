package org.jbubblebreaker.gamemodes;

import org.jbubblebreaker.GameSize;

/**
 * Ongoing Speed Rules
 * @author Sven Strickroth
 */
public class GameOngoingSpeed extends GameOngoing {

    /**
	 * Stores the name of this Game-Mode
	 */
    public static String name = "OngoingSpeed";

    /**
	 * Stores the possible matrix-sizes for this mode
	 */
    public static GameSize allowedSize = new GameSize(0, 0, 0, 0);

    /**
	 * Stores the timestamp for time differenz calculations
	 */
    private long time;

    /**
	 * Creates a game with rules for an ongoing game
	 * @param rows of the matrix
	 * @param cols of the matrix
	 * @param bubbleType bubbleType index
	 */
    public GameOngoingSpeed(int rows, int cols, int bubbleType) {
        super(rows, cols, bubbleType);
    }

    @Override
    public String getMode() {
        return name;
    }

    ;

    @Override
    protected void removeMarkedBubbles(int row, int col) {
        super.removeMarkedBubbles(row, col);
        time = System.currentTimeMillis();
    }

    @Override
    protected void fillPlayground() {
        super.fillPlayground();
        time = System.currentTimeMillis();
    }

    @Override
    protected Integer getCalculatedPoints() {
        int calculatedTime = 10 - (int) ((System.currentTimeMillis() - time) / 100);
        if (calculatedTime < 0) {
            calculatedTime = 0;
        }
        return (marked * calculatedTime);
    }
}
