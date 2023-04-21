package jshomeorg.simplytrain.service.exceptions;

import jshomeorg.simplytrain.service.*;
import jshomeorg.simplytrain.service.trackObjects.*;
import jshomeorg.simplytrain.train.fulltrain;

/**
 *
 * @author js
 */
public class TrainStoppedException extends java.lang.Exception {

    stopObject so;

    fulltrain ft;

    track tr;

    int distance = 0;

    /**
     * Creates a new instance of <code>TrainStoppedException</code> without detail message.
     */
    public TrainStoppedException(fulltrain t1, track t2, stopObject s) {
        super("Train Stopped Exception: " + t1 + " <-> " + t2);
        ft = t1;
        tr = t2;
        so = s;
    }

    public TrainStoppedException(TrainStoppedException tse, int d) {
        super(tse);
        ft = tse.ft;
        tr = tse.tr;
        so = tse.so;
        distance = d;
    }

    public int getDistance() {
        return distance;
    }

    public stopObject getStopObject() {
        return so;
    }
}
