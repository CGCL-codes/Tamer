package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Driver
 */
public class PickupBallsUpper extends CommandBase {

    public PickupBallsUpper() {
        requires(pickup);
    }

    protected void initialize() {
    }

    protected void execute() {
        pickup.pickupUpper();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
