package model.message;

import model.Direction;
import model.MazeForce;
import model.Player;
import model.Robot;
import model.exception.RuleViolation;
import model.field.Field;

/**
 * @author barkholt
 * 
 */
public class Move extends Message {

    @NetworkField
    public Robot robot;

    @NetworkField
    public Direction direction;

    public Move(Player player, Robot robot, Direction direction) {
        super(player);
        this.robot = robot;
        this.direction = direction;
    }

    public Move() {
    }

    @Override
    public void act(MazeForce mazeforce) {
        robot.move(direction);
        mazeforce.fireMovement(robot, direction);
    }

    @Override
    public String getName() {
        return "Move";
    }

    @Override
    public void validate(MazeForce mazeforce) throws RuleViolation {
        assertActivePlayer(mazeforce);
        Field targetField = mazeforce.getLevel().getAdjacentField(robot.getPosition(), direction);
        if (targetField == null || !targetField.allowsMovement()) {
            throw new RuleViolation(getPlayer().getName() + " attempting to move to an illegal field. Current robot position: " + robot.getPosition() + "), movement direction:" + direction);
        }
        for (Robot robot : mazeforce.getRobots()) {
            if (robot.getPosition().equals(targetField.getPosition())) {
                throw new RuleViolation("Illegal move detected. Attempt to move on top of another robot. Player: " + getPlayer().getName() + ", robot: " + robot + ", direction: " + "direction");
            }
        }
    }

    @Override
    public void callback(MessageCallback callback) {
        callback.move(this);
    }
}
