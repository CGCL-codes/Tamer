package ch.usi.nxt.motor;

import ch.usi.nxt.LCDManager;
import ch.usi.nxt.setting.Configuration;
import lejos.navigation.TachoPilot;
import lejos.nxt.Sound;

/**
 * This class manages the motors to get the instances
 * on which call the motion methods.
 *
 * @author Krzysztof Zawadzki <zawadzkk@usi.ch>
 * @version Aug 9, 2009
 *
 */
public class MotorsManager implements Configuration {

    public static TachoPilot PILOT;

    public static void initiate() {
        if (TWO_MOTOR_ENGINE) {
            PILOT = new TachoPilot(LEFT_WHEEL_DIAMETER, RIGHT_WHEEL_DIAMETER, PILOT_TRACK_WIDTH, PILOT_LEFT_MOTOR, PILOT_RIGHT_MOTOR, PILOT_REVERSE);
            PILOT.reset();
            PILOT.regulateSpeed(true);
            PILOT.getRight().setPower(100);
            PILOT.getLeft().setPower(100);
            LCDManager.writeCompleteWords("~ Motors Active", false);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Sound.buzz();
                System.exit(0);
            }
        }
    }
}
