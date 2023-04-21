package il.co.gadiworks.jumper;

import il.co.gadiworks.games.framework.GameObject;

public class Coin extends GameObject {

    public static final float COIN_WIDTH = 0.4f;

    public static final float COIN_HEIGHT = 0.8f;

    public static final int COIN_SCORE = 10;

    float stateTime;

    public Coin(float x, float y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT);
        this.stateTime = 0;
    }

    public void update(float deltaTime) {
        this.stateTime += deltaTime;
    }
}
