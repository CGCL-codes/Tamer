package golgothrobots;

import java.awt.Color;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 * MyFirstRobot - a robot by (your name here)
 */
public class Robot2 extends Robot {

    /**
	 * run: MyFirstRobot's default behavior
	 */
    public void run() {
        setColors(Color.yellow, Color.green, Color.red, Color.blue, Color.white);
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    /**
	 * onScannedRobot: What to do when you see another robot
	 */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(100);
    }

    /**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(90 - e.getBearing());
    }
}
