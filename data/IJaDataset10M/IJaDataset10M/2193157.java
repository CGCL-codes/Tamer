package com.agentfactory.vacworld.vacGui;

import com.agentfactory.vacworld.taskArchitecture.AbstractTask;
import com.agentfactory.vacworld.taskArchitecture.Task;
import com.agentfactory.vacworld.vacGui.Turnable.Rotate;

public class TurnTask extends AbstractTask implements Task {

    private Turnable turnable;

    private Direction initialDirection = null;

    private Direction finalDirection;

    private Rotate rotation = Rotate.NONE;

    public static final int resolution = 64;

    public static final double stepSize = (2.0 * Math.PI) / resolution;

    /**
	 * When this task is invoked, if the Turnable object's direction doesn't match
	 * the originally-provided initialDirection the turn task will fail. 
	 * @param turnable - object to turn
	 * @param initialDirection - if not null, will be compared at runtime
	 * @param finalDirection - stop turning when pointing in this direction
	 */
    public TurnTask(Turnable turnable, Direction initialDirection, Direction finalDirection) {
        super();
        if (turnable == null || finalDirection == null) {
            throw new IllegalArgumentException("Must specify a turnable object and a final direction");
        }
        this.turnable = turnable;
        this.initialDirection = initialDirection;
        this.finalDirection = finalDirection;
        calculateTurn();
    }

    public void run() throws WrongDirectionException {
        super.run();
        if (initialDirection != null) {
            if (!initialDirection.equals(turnable.getDirection())) {
                throw new WrongDirectionException(initialDirection, turnable.getDirection());
            }
        } else {
            initialDirection = turnable.getDirection();
            calculateTurn();
        }
        Direction currentDirection = new Direction(turnable.getDirection().getValue());
        turnable.setRotation(rotation);
        turnable.firePropertyChange(Turnable.TURN_START, initialDirection, finalDirection);
        while (!currentDirection.equals(finalDirection)) {
            this.step();
            if (rotation == Rotate.CLOCKWISE) currentDirection.setValue(currentDirection.getValue() + TurnTask.stepSize); else currentDirection.setValue(currentDirection.getValue() - TurnTask.stepSize);
            turnable.setDirection(currentDirection);
            turnable.firePropertyChange(Turnable.TURN_STEP, null, null);
        }
        turnable.setDirection(finalDirection);
        turnable.firePropertyChange(Turnable.TURN_STEP, null, null);
        turnable.setRotation(Rotate.NONE);
        turnable.firePropertyChange(Turnable.TURN_STOP, initialDirection, finalDirection);
    }

    /**
	 * Calculate which way to turn, and how long it will take.
	 */
    private void calculateTurn() {
        setTaskName("Turn from " + initialDirection + " to " + finalDirection);
        if (initialDirection.equals(finalDirection)) {
            rotation = Rotate.NONE;
            setDuration(0);
        } else {
            double turn = finalDirection.getValue() - initialDirection.getValue();
            if (turn > Math.PI) {
                turn -= (2.0 * Math.PI);
            } else if (turn < -Math.PI) {
                turn += (2.0 * Math.PI);
            }
            if (turn < 0) rotation = Rotate.ANTICLOCKWISE; else if (turn > 0) rotation = Rotate.CLOCKWISE;
            double turnFraction = Math.abs(turn) / (2.0 * Math.PI);
            this.setDuration(Math.round(turnable.getTimeToTurn() * turnFraction));
            this.setStepCount((int) Math.round(TurnTask.resolution * turnFraction));
        }
    }

    public Direction getInitialDirection() {
        return initialDirection;
    }

    public Direction getFinalDirection() {
        return finalDirection;
    }

    public Rotate getRotation() {
        return rotation;
    }
}
