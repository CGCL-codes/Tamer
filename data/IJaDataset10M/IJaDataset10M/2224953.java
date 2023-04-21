package jmri.jmrix.rps;

import javax.vecmath.Point3d;

/**
 * Some helpful implementations and values for Calculators.
 *
 * @author	Bob Jacobsen  Copyright (C) 2006, 2008
 * @version	$Revision: 1.6 $
 */
public abstract class AbstractCalculator implements Calculator {

    Point3d sensors[];

    double[] Tr;

    double[] Xr;

    double[] Yr;

    double[] Zr;

    int nr;

    public void prep(Reading r) {
        if (log.isDebugEnabled()) {
            log.debug("Reading: " + r.toString());
            log.debug("Sensors: " + sensors.length);
            if (sensors.length >= 1 && sensors[0] != null) log.debug("Sensor[0]: " + sensors[0].x + "," + sensors[0].y + "," + sensors[0].z);
            if (sensors.length >= 2 && sensors[1] != null) log.debug("Sensor[1]: " + sensors[1].x + "," + sensors[1].y + "," + sensors[1].z);
        }
        nr = r.getNValues();
        if (nr != sensors.length - 1) log.error("Mismatch: " + nr + " readings, " + (sensors.length - 1) + " receivers");
        nr = Math.min(nr, sensors.length - 1);
        Tr = new double[nr];
        Xr = new double[nr];
        Yr = new double[nr];
        Zr = new double[nr];
        int j = 0;
        for (int i = 0; i <= nr; i++) {
            if (sensors[i] != null) {
                Tr[j] = r.getValue(i);
                Xr[j] = sensors[i].x;
                Yr[j] = sensors[i].y;
                Zr[j] = sensors[i].z;
                j++;
            }
        }
        nr = j;
        if (log.isDebugEnabled()) summarize();
    }

    void summarize() {
        System.out.println("nr is " + nr);
        for (int j = 0; j < nr; j++) {
            System.out.println(" t: " + Tr[j] + " to " + Xr[j] + "," + Yr[j] + "," + Zr[j]);
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractCalculator.class.getName());
}
