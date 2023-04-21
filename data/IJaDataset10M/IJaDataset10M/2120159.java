package model.field;

import model.Direction;
import model.Position;
import model.Robot;

/**
 * @author barkholt
 * 
 */
public class Spawn extends Floor {

    private final Direction direction;

    /**
     * @param x
     * @param y
     */
    public Spawn(Position position, Direction direction) {
        super(position);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public String toString() {
        return "Spawn(" + getPosition() + ")";
    }

    @Override
    public void callback(Callback callback) {
        callback.spawn(this);
    }

    @Override
    public void steppedOn(Robot robot) {
    }
}
