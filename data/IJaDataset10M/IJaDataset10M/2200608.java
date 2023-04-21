package org.gunncs.actoriface;

import com.iaroc.irobot.*;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.util.Queue;
import java.io.IOException;
import java.util.Vector;
import org.gunncs.actoriface.*;
import com.sun.spot.util.Utils;

/**
 *
 * @author Anandg
 */
public class Robot {

    public static final int LEFT = 1;

    public static final int RIGHT = -1;

    public static final int TURN_THRESHOLD = 5;

    public static final int CENTER_BLOCKED = 18;

    public static final int BACKUP_DISTANCE = 2;

    public static final int ADJUST = 2;

    EDemoBoard spotboard;

    IRobotCreate create;

    Vector sensors;

    boolean moving;

    int offset;

    int spindirection;

    int velocity = 175;

    /** Creates a new instance of Robot */
    public Robot(IRobotCreate c, EDemoBoard e) {
        create = c;
        spotboard = e;
        sensors = new Vector();
        moving = false;
    }

    public void addSensor(Sensor s) {
        sensors.addElement(s);
    }

    public synchronized Sensor[] getSensors() {
        Sensor[] temp = new Sensor[sensors.size()];
        for (int i = 0; i < sensors.size(); i++) {
            temp[i] = (Sensor) sensors.elementAt(i);
        }
        return temp;
    }

    public void goBackward() {
        moving = true;
        if (create != null) {
            create.goBackward();
        }
        spindirection = 0;
    }

    public void goForward() {
        moving = true;
        if (create != null) {
            create.goForward();
        }
        spindirection = 0;
    }

    public void turnLeft() {
        turnLeft(true);
    }

    public void turnLeft(boolean point) {
        moving = true;
        if (create != null) {
            if (!point) create.driveDirect(velocity, 0); else create.spinLeft();
        }
        spindirection = LEFT;
    }

    public void turnLeft(int angle) {
        turnLeft(angle, false);
    }

    public void turnRight(int angle) {
        turnRight(angle, false);
    }

    public void turnLeft(int angle, boolean useCompass) {
        moving = true;
        spindirection = LEFT;
        if (useCompass) {
            double goalHeading = ((Sensor) (sensors.elementAt(7))).getState()[0] - angle;
            if (goalHeading < 0) {
                goalHeading += 360;
            }
            create.driveDirect(-velocity, velocity);
            while (Math.abs(((Sensor) (sensors.elementAt(7))).getState()[0] - goalHeading) > TURN_THRESHOLD) {
                System.out.println("Turning> " + ((Sensor) (sensors.elementAt(7))).getState()[0] + " -> " + goalHeading);
            }
            create.stop();
        } else {
            turnLeft();
            waitAngle(angle);
        }
    }

    public void turnRight(int angle, boolean useCompass) {
        if (useCompass) {
            moving = true;
            spindirection = RIGHT;
            double goalHeading = ((Sensor) (sensors.elementAt(7))).getState()[0] + angle;
            if (goalHeading > 360) {
                goalHeading -= 360;
            }
            create.driveDirect(velocity, -velocity);
            while (Math.abs(((Sensor) (sensors.elementAt(7))).getState()[0] - goalHeading) > TURN_THRESHOLD) {
                System.out.println("Turning> " + ((Sensor) (sensors.elementAt(7))).getState()[0] + " -> " + goalHeading);
            }
            create.stop();
        } else {
            turnRight();
            waitAngle(angle);
        }
    }

    public void turnRight() {
        turnRight(true);
    }

    public void turnRight(boolean point) {
        moving = true;
        if (create != null) {
            if (point) create.spinRight(); else create.driveDirect(0, velocity);
        }
        spindirection = RIGHT;
    }

    public void waitAngle(int degrees) {
        waitAngle(degrees, false);
    }

    public void waitAngle(int degrees, boolean force) {
        if (create != null) {
            if (force) create.waitAngle(degrees); else {
                int start = create.getAccumulatedAngle();
                while (Math.abs(create.getAccumulatedAngle() - start) < degrees) {
                    create.sensors(IRobotCreateConstants.SENSORS_GROUP_ID6, null);
                    Utils.sleep(2);
                    try {
                        getSensors()[0].update(Thread.currentThread());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (getSensors()[0].getState()[BumpSensor.LEFT] == BumpSensor.PRESSED && getSensors()[0].getState()[BumpSensor.RIGHT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        break;
                    } else if (getSensors()[0].getState()[BumpSensor.LEFT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        int st = create.getAccumulatedAngle();
                        turnRight();
                        waitAngle(ADJUST, true);
                        int fi = create.getAccumulatedAngle();
                        start += (fi - st);
                    } else if (getSensors()[0].getState()[BumpSensor.RIGHT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        int st = create.getAccumulatedAngle();
                        turnLeft();
                        waitAngle(ADJUST, true);
                        int fi = create.getAccumulatedAngle();
                        start += (fi - st);
                    }
                    if (getSensors()[1].getState()[0] < CENTER_BLOCKED) {
                        break;
                    }
                }
            }
        }
    }

    public void waitDistance(int mms) {
        waitDistance(mms, false);
    }

    public void waitDistance(int mms, boolean force) {
        if (create != null) {
            if (force) create.waitDistance(mms); else {
                int start = create.getAccumulatedDistance();
                while (Math.abs(create.getAccumulatedDistance() - start) < mms) {
                    Utils.sleep(50);
                    create.sensors(IRobotCreateConstants.SENSORS_GROUP_ID6, null);
                    try {
                        getSensors()[0].update(Thread.currentThread());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (getSensors()[0].getState()[BumpSensor.LEFT] == BumpSensor.PRESSED && getSensors()[0].getState()[BumpSensor.RIGHT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        break;
                    } else if (getSensors()[0].getState()[BumpSensor.LEFT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        int st = create.getAccumulatedAngle();
                        turnRight(ADJUST);
                        int fi = create.getAccumulatedAngle();
                        start += (fi - st);
                    } else if (getSensors()[0].getState()[BumpSensor.RIGHT] == BumpSensor.PRESSED) {
                        goBackward();
                        waitDistance(-BACKUP_DISTANCE, true);
                        int st = create.getAccumulatedAngle();
                        turnLeft(ADJUST);
                        int fi = create.getAccumulatedAngle();
                        start += (fi - st);
                    }
                    if (getSensors()[1].getState()[0] < CENTER_BLOCKED) {
                        break;
                    }
                }
            }
        }
    }

    public void stop() {
        moving = true;
        if (create != null) {
            create.stop();
        }
        spindirection = 0;
    }

    public void setOffset(double off) {
        offset = (int) off;
    }

    public void setVelocity(int velocity) {
        if (create != null) {
            try {
                create.setVelocity(velocity);
            } catch (Exception e) {
                return;
            }
        }
        this.velocity = velocity;
    }

    public boolean isMoving() {
        return moving;
    }

    public void resetMotion() {
        moving = false;
        spindirection = 0;
    }

    public synchronized EDemoBoard getSpotBoard() {
        return spotboard;
    }

    public synchronized IRobotCreate getCreate() {
        return create;
    }

    public void driveDirect(int left, int right) {
        moving = true;
        if (create != null) create.driveDirect(left, right);
    }

    public static int round(double d) {
        if (d % 1 < .5) {
            return (int) d;
        } else {
            return (d < 0 ? (int) (d - 1) : (int) (d + 1));
        }
    }
}
