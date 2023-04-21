package transport.action;

import transport.IAction;

/**
 * Represents the move action
 * 
 * @author rem
 */
public class KickAction implements IAction {

    private double power, direction;

    public KickAction(double power, double direction) {
        this.power = power;
        this.direction = direction;
    }

    public String toMessageString() {
        return "(kick " + power + " " + direction + ")\0";
    }
}
