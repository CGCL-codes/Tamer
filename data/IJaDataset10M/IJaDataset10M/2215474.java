package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.Recoverable;

/**
 *
 * @author MattKahn
 */
public abstract class SwerveSubsystem extends Subsystem implements Recoverable {

    Solenoid swerveRetract = null;

    Solenoid swerveDeploy = null;

    protected boolean m_kSwerveStateDeployed = false;

    public SwerveSubsystem() {
        System.out.println("Constructing SwerveSubsystem");
    }

    public void initDefaultCommand() {
        setDefaultCommand(null);
    }

    public abstract void raiseSwerve();

    public abstract void lowerSwerve();

    public boolean isDeployed() {
        return m_kSwerveStateDeployed;
    }

    public abstract void setPosition(double position);

    public abstract double getPosition();

    public abstract double getCommandedPosition();

    public abstract void recover(boolean override);
}
