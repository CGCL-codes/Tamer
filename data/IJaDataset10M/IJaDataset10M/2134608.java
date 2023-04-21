package gameframework.base;

import java.awt.Point;

/**
 * This is the most basic implementation of {@link SpeedVector} which is nothing
 * more than a plain structure with getters and setters.
 */
public class SpeedVectorDefaultImpl implements SpeedVector {

    private static final int DEFAULT_SPEED = 8;

    private Point direction;

    private int speed;

    public static SpeedVector createNullVector() {
        return new SpeedVectorDefaultImpl(new Point(0, 0), 0);
    }

    public SpeedVectorDefaultImpl(Point direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    public SpeedVectorDefaultImpl(Point direction) {
        this(direction, DEFAULT_SPEED);
    }

    public Point getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public Object clone() {
        return new SpeedVectorDefaultImpl(direction, speed);
    }
}
