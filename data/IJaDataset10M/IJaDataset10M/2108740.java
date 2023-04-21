package il.co.gadiworks.jumper;

import il.co.gadiworks.games.framework.GameObject;

public class Castle extends GameObject {

    public static final float CASTLE_WIDTH = 1.7f;

    public static final float CASTLE_HEIGHT = 1.7f;

    float stateTime;

    public Castle(float x, float y) {
        super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
    }
}
