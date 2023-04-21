package org.gunncs.actoriface;

import com.iaroc.irobot.IRobotCreate;
import java.io.*;

/**
 *
 * @author kroo
 */
public class BumpSensor extends Sensor {

    public static final double PRESSED = 1.0;

    public static final double UNPRESSED = 0.0;

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    IRobotCreate create;

    public BumpSensor(IRobotCreate c) {
        create = c;
        double[] d = new double[3];
        d[0] = 0.0;
        d[1] = 0.0;
        d[2] = Simulator.time();
        setState(d);
    }

    public String getName() {
        return "BumpSensor";
    }

    public int getID() {
        return 2;
    }

    public synchronized void update(Thread daemon) throws InterruptedException, IOException {
        if (create == null) {
            return;
        }
        double[] d = new double[3];
        if (create.isBumpLeft()) {
            d[0] = PRESSED;
        } else {
            d[0] = UNPRESSED;
        }
        if (create.isBumpRight()) {
            d[1] = PRESSED;
        } else {
            d[1] = UNPRESSED;
        }
        d[2] = Simulator.time();
        setState(d);
    }
}
