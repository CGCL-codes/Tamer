package es.unizar.tecnodiscap.osgi4ami.device.simpleHMI.extended;

import es.unizar.tecnodiscap.osgi4ami.device.simpleHMI.InputSimpleHMICluster;
import es.unizar.tecnodiscap.osgi4ami.device.simpleHMI.SimpleHMI;

/**
 * Interface proposal for a simpleHMI button
 * @author Unizar
 */
public interface ButtonSimpleHMI extends SimpleHMI, InputSimpleHMICluster {

    /**
     * Key value identifying the event BUTTON_PRESSED
     */
    public static final int EVENT_BUTTON_PRESSED = 1;
}
