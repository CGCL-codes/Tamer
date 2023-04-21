package com.xerox.VTM.engine;

import java.util.Date;

/**
 * Curve control point animation
 * @author Emmanuel Pietriga
 */
abstract class GCurveCtrl extends GAnimation {

    /** step values for (r,theta) polar coords */
    PolarCoords[] steps;

    void start() {
        now = new Date();
        startTime = now.getTime();
        started = true;
    }
}
