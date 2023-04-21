package northernforce.main;

import northernforce.main.*;
import edu.wpi.first.wpilibj.*;
import java.lang.Math;

public class footballDrive {

    public static double wheelBase = 25.0;

    public static double wheelTrack = 22.25;

    public static double wheelRatio = wheelBase / wheelTrack;

    public static double bigWheelDiameter = 12.5;

    public static double mecanumWheelDiameter = 5.5;

    public static int encoderPulsesPerRevolution = 90;

    public static double bigWheelDistancePerPulse = bigWheelDiameter * Math.PI / encoderPulsesPerRevolution;

    public static double mecanumWheelDistancePerPulse = mecanumWheelDiameter * Math.PI / encoderPulsesPerRevolution;

    public boolean m_useEncoders = false;

    Jaguar m_frontLeftPWM = new Jaguar(1);

    Jaguar m_rearLeftPWM = new Jaguar(2);

    Jaguar m_frontRightPWM = new Jaguar(3);

    Jaguar m_rearRightPWM = new Jaguar(4);

    Encoder m_frontLeftEncoder = new Encoder(4, 1, 2, (int) bigWheelDistancePerPulse, false);

    Encoder m_rearLeftEncoder = new Encoder(4, 3, 4, (int) mecanumWheelDistancePerPulse, false);

    Encoder m_frontRightEncoder = new Encoder(4, 5, 6, (int) bigWheelDistancePerPulse, false);

    Encoder m_rearRightEncoder = new Encoder(4, 7, 8, (int) mecanumWheelDistancePerPulse, false);

    final int rightX = 2;

    final int rightY = 3;

    final int leftX = 4;

    public void wheelControllers() {
    }

    public void footballDrive() {
    }

    public void controllerDrive(GenericHID controller) {
        double x = controller.getRawAxis(rightX);
        double y = controller.getRawAxis(rightY);
        double m = Math.sqrt((y * y) + (x * x));
        double v = controller.getRawAxis(leftX);
        double t = northernforce.main.ConorMath.atan2(y, x);
        arcadeDrive(m, v, t, false);
    }

    public void arcadeDrive(double moveValue, double rotateValue, double theta, boolean squaredInputs) {
        double frontleft;
        double frontright;
        double backleft;
        double backright;
        if (squaredInputs) {
            if (moveValue >= 0.0) {
                moveValue = (moveValue * moveValue);
            } else {
                moveValue = -(moveValue * moveValue);
            }
            if (rotateValue >= 0.0) {
                rotateValue = (rotateValue * rotateValue);
            } else {
                rotateValue = -(rotateValue * rotateValue);
            }
        }
        if ((rotateValue > -0.1) && (rotateValue < 0.1)) {
            frontleft = (moveValue * Math.sin(theta - (Math.PI / 4)));
            frontright = (moveValue * Math.sin(theta + (Math.PI / 4)));
            backleft = (moveValue * Math.sin(theta - (Math.PI / 4)));
            backright = (moveValue * Math.sin(theta - (Math.PI / 4)));
        } else {
            frontleft = rotateValue;
            frontright = -rotateValue;
            backleft = rotateValue;
            backright = -rotateValue;
        }
        powerMotors(frontleft, backleft, frontright, backright);
    }

    public void enableEncoders() {
        if (m_useEncoders == true) {
            m_frontLeftEncoder.start();
            m_rearLeftEncoder.start();
            m_frontRightEncoder.start();
            m_rearRightEncoder.start();
        } else {
            m_frontLeftEncoder.stop();
            m_rearLeftEncoder.stop();
            m_frontRightEncoder.stop();
            m_rearRightEncoder.stop();
        }
    }

    public void powerMotors(double frontLeft, double rearLeft, double frontRight, double rearRight) {
        rampMotor(m_frontRightPWM, -frontRight);
        rampMotor(m_rearLeftPWM, rearLeft);
        rampMotor(m_frontLeftPWM, -frontLeft);
        rampMotor(m_rearRightPWM, rearRight);
    }

    public void rampMotor(SpeedController controller, double newValue) {
        double ramp = 0.2;
        double curValue = controller.get();
        newValue = curValue + (newValue - curValue) * ramp;
        controller.set(newValue);
    }
}
