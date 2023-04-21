package jshomeorg.simplytrain.service.trainCommands;

import java.util.Iterator;
import jshomeorg.simplytrain.train.fulltrain;
import jshomeorg.simplytrain.service.odsHashSet;
import jshomeorg.simplytrain.service.trackObjects.*;
import jshomeorg.simplytrain.service.trainCommandExecutor;

/**
 *
 * @author js
 */
public class gotoDestination extends trainCommand {

    destinationObject destination = null;

    /** Creates a new instance of gotoDestination */
    public gotoDestination() {
        super("fahre zu");
    }

    public Object clone() {
        return new gotoDestination();
    }

    public void setDestination(destinationObject d) {
        destination = d;
    }

    public destinationObject getDestination() {
        return destination;
    }

    public String toString() {
        if (destination != null) return name + " " + destination.getRegion() + "/" + destination.getName(); else return name;
    }

    public odsHashSet getData() {
        odsHashSet h = super.getData();
        h.add(new objectDataStorage("destination", "Ziel", destination != null ? destination.getTemporaryHash() : 0));
        return h;
    }

    /**
     * set parameters of trackObject
     * @param value todHashSet with parameters, missing parameters are not changed
     * @abstract
     */
    public void setData(odsHashSet hm) {
        for (Iterator<objectDataStorage> it = hm.iterator(); it.hasNext(); ) {
            objectDataStorage key = it.next();
            if (key.getKey().compareTo("destination") == 0) {
                int h = key.getIntValue();
                for (trackObject to : trackObject.allto.keySet()) {
                    if (to instanceof destinationObject && to.getTemporaryHash() == h) {
                        destination = (destinationObject) to;
                    }
                }
            }
        }
        super.setData(hm);
    }

    public boolean isDestination(trainCommandExecutor tce, destinationObject d) {
        boolean b = destination == d;
        if (b) {
            tce.storeLocalValue("matched", true + "");
        }
        return b;
    }

    public boolean isDestination(trainCommandExecutor tce, String d) {
        boolean b = destination.getName().equals(d);
        if (b) {
            tce.storeLocalValue("matched", true + "");
        }
        return b;
    }

    public boolean finished(trainCommandExecutor tce) {
        String o = tce.getLocalValue("matched");
        if (o != null && Boolean.parseBoolean(o)) {
            tce.getTrain().setTemporaryvMaxTicks(fulltrain.NOTEMPVMAX);
            return true;
        }
        return false;
    }
}
