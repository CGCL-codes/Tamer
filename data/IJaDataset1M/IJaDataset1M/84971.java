package it.unipr.ce.dsg.deus.impl.process;

import java.util.ArrayList;
import java.util.Properties;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.util.Distributions;

/**
 * This class represents a Homogeneous Poisson Process defined within a time interval 
 * between vt = 0 and vt = VTmax. It accepts parameters called "meanArrival" (float) 
 * that is used to compute next event's triggering time, "startVtThreshold" (float) 
 * and "stopVtThreshold" (float) which are the limits of the time interval in 
 * which the Poisson process is defined.
 * 
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 */
public class RectangularPulsePoissonProcess extends Process {

    private static final String MEAN_ARRIVAL = "meanArrival";

    private static final String START_VT_THRESHOLD = "startVtThreshold";

    private static final String STOP_VT_THRESHOLD = "stopVtThreshold";

    private float meanArrival = 0;

    private float startVtThreshold = 0;

    private float stopVtThreshold = 0;

    public RectangularPulsePoissonProcess(String id, Properties params, ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents) throws InvalidParamsException {
        super(id, params, referencedNodes, referencedEvents);
        initialize();
    }

    public float getMeanArrival() {
        return meanArrival;
    }

    public float getStartVtThreshold() {
        return startVtThreshold;
    }

    public float getStopVtThreshold() {
        return stopVtThreshold;
    }

    public void initialize() throws InvalidParamsException {
        if (params.getProperty(START_VT_THRESHOLD) == null) throw new InvalidParamsException(START_VT_THRESHOLD + " param is expected.");
        try {
            startVtThreshold = Float.parseFloat(params.getProperty(START_VT_THRESHOLD));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(START_VT_THRESHOLD + " must be a valid float value.");
        }
        if (params.getProperty(STOP_VT_THRESHOLD) == null) throw new InvalidParamsException(STOP_VT_THRESHOLD + " param is expected.");
        try {
            stopVtThreshold = Float.parseFloat(params.getProperty(STOP_VT_THRESHOLD));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(STOP_VT_THRESHOLD + " must be a valid float value.");
        }
        if (startVtThreshold > stopVtThreshold) throw new InvalidParamsException(STOP_VT_THRESHOLD + " must be higher than " + START_VT_THRESHOLD);
        if (params.getProperty(MEAN_ARRIVAL) == null) throw new InvalidParamsException(MEAN_ARRIVAL + " param is expected.");
        try {
            meanArrival = Float.parseFloat(params.getProperty(MEAN_ARRIVAL));
        } catch (NumberFormatException ex) {
            throw new InvalidParamsException(MEAN_ARRIVAL + " must be a valid float value.");
        }
    }

    public float getNextTriggeringTime(Event event, float virtualTime) {
        if (virtualTime < startVtThreshold) return virtualTime + startVtThreshold; else if ((virtualTime >= startVtThreshold) && (virtualTime < stopVtThreshold)) return virtualTime + (float) Distributions.exp(event.getEventRandom(), (float) 1 / meanArrival); else return virtualTime + Engine.getDefault().getMaxVirtualTime();
    }
}
