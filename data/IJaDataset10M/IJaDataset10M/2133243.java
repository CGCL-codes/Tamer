package fun.trackcam.game.tennis;

import java.awt.Color;
import fun.trackcam.game.balls.Ball;

class TennisBall extends Ball {

    /**
	 * State color
	 */
    static Color[] COLOR_STATE = { Color.green, Color.blue };

    /**
	 * Dead state. If a ball is in the dead state, it can't move or be visible.
	 */
    static int DEAD_STATE = -1;

    /**
	 * Ball state
	 */
    static int BALL_STATE = 0;

    /**
	 * Racquet state
	 */
    static int RACQUET_STATE = 1;

    /**
	 * the constructor for a tennis ball
	 */
    public TennisBall(int w, int h, int x, int y, double xinc, double yinc) {
        super(w, h, x, y, xinc, yinc);
        this.state = BALL_STATE;
    }

    /**
	 * the constructor for a tennis racquet
	 */
    public TennisBall(int w, int h, int x, int y, int state) {
        super(w, h, x, y, 0, 0);
        this.state = state;
    }

    public void hit(TennisBall b) {
        if (!collide) {
            coll_x = b.getCenterX();
            coll_y = b.getCenterY();
            collide = true;
        }
    }
}
