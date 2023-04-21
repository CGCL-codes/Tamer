package laf.socf.simulator.sensors;

import javax.swing.Timer;
import laf.socf.simulator.gui.Robot;
import org.freedesktop.dbus.DBusInterface;

/**
 *
 * @author laf
 */
public class NavigationRemote implements DBusInterface {

    private Robot robot;

    private Timer timer;

    public NavigationRemote() {
    }

    public NavigationRemote(Robot robot, Timer timer) {
        this.robot = robot;
        this.timer = timer;
    }

    public boolean isRemote() {
        return false;
    }

    public void moveUp() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void moveDown() {
        robot.setCurrentDir(Robot.DIR.values()[(robot.getCurrentDir().ordinal() + 2) % 4]);
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void moveLeft() {
        boolean wall = robot.getFrontObstacleDistance() < 5;
        Robot.DIR xdir = robot.getCurrentDir();
        robot.setCurrentDir(Robot.DIR.values()[(robot.getCurrentDir().ordinal() + 1) % 4]);
        if (!timer.isRunning()) {
            timer.start();
        }
        if (wall) {
            if (xdir == Robot.DIR.LEFT) {
                robot.setLocation(robot.getX() + (robot.getHeight() - robot.getWidth()), robot.getY());
            } else if (xdir == Robot.DIR.UP) {
                robot.setLocation(robot.getX(), robot.getY() + (robot.getWidth() - robot.getHeight()));
            }
        } else {
            if (xdir == Robot.DIR.DOWN) {
                robot.setLocation(robot.getX(), robot.getY() + (robot.getWidth() - robot.getHeight()));
            } else if (xdir == Robot.DIR.RIGHT) {
                robot.setLocation(robot.getX() + (robot.getHeight() - robot.getWidth()), robot.getY());
            }
        }
        xdir = robot.getCurrentDir();
        if (!wall) {
            if (xdir == Robot.DIR.LEFT) {
                robot.setLocation(robot.getX() + 2, robot.getY());
            } else if (xdir == Robot.DIR.RIGHT) {
                robot.setLocation(robot.getX() - 2, robot.getY());
            } else if (xdir == Robot.DIR.UP) {
                robot.setLocation(robot.getX(), robot.getY() + 2);
            } else if (xdir == Robot.DIR.DOWN) {
                robot.setLocation(robot.getX(), robot.getY() - 2);
            }
        }
    }

    public void moveRight() {
        boolean wall = robot.getFrontObstacleDistance() < 5;
        Robot.DIR xdir = robot.getCurrentDir();
        int newOrd = (robot.getCurrentDir().ordinal() - 1) % 4;
        if (newOrd < 0) {
            newOrd = 3;
        }
        robot.setCurrentDir(Robot.DIR.values()[newOrd]);
        if (!timer.isRunning()) {
            timer.start();
        }
        if (wall) {
            if (xdir == Robot.DIR.LEFT) {
                robot.setLocation(robot.getX() - (robot.getHeight() - robot.getWidth()), robot.getY());
            } else if (xdir == Robot.DIR.UP) {
                robot.setLocation(robot.getX(), robot.getY() + (robot.getWidth() - robot.getHeight()));
            }
        } else {
            if (xdir == Robot.DIR.DOWN) {
                robot.setLocation(robot.getX(), robot.getY() + (robot.getWidth() - robot.getHeight()));
            } else if (xdir == Robot.DIR.RIGHT) {
                robot.setLocation(robot.getX() + (robot.getHeight() - robot.getWidth()), robot.getY());
            }
        }
        xdir = robot.getCurrentDir();
        if (!wall) {
            if (xdir == Robot.DIR.LEFT) {
                robot.setLocation(robot.getX() + 2, robot.getY());
            } else if (xdir == Robot.DIR.RIGHT) {
                robot.setLocation(robot.getX() - 2, robot.getY());
            } else if (xdir == Robot.DIR.UP) {
                robot.setLocation(robot.getX(), robot.getY() + 2);
            } else if (xdir == Robot.DIR.DOWN) {
                robot.setLocation(robot.getX(), robot.getY() - 2);
            }
        }
    }

    public void setSpeed(int speed) {
    }

    public void stop() {
        timer.stop();
    }
}
