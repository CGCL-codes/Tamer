package org.pricejd.rcx;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorConstants;
import josx.platform.rcx.SensorListener;

/**
 * @author WJPJ
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test implements SensorConstants {

    static int lightCount;

    static boolean countBlack;

    static boolean countWhite;

    static boolean moving;

    public static void main(String args[]) throws InterruptedException {
        lightCount = 0;
        Sensor.S1.setTypeAndMode(SENSOR_TYPE_LIGHT, SENSOR_MODE_PCT);
        Sensor.S1.activate();
        countBlack = Sensor.S1.readValue() > 50;
        countWhite = Sensor.S1.readValue() < 40;
        Sensor.S1.addSensorListener(new SensorListener() {

            public void stateChanged(Sensor arg0, int arg1, int arg2) {
                if (arg2 < 45 && countBlack) {
                    lightCount++;
                    countBlack = false;
                    countWhite = true;
                }
                if (arg2 > 45 && countWhite) {
                    lightCount++;
                    countBlack = true;
                    countWhite = false;
                }
            }
        });
        Sensor.S2.addSensorListener(new SensorListener() {

            public void stateChanged(Sensor arg0, int arg1, int arg2) {
                Motor.A.stop();
                Motor.C.stop();
                moving = false;
            }
        });
        moving = true;
        while (moving) {
        }
        Sensor.S1.passivate();
    }
}
