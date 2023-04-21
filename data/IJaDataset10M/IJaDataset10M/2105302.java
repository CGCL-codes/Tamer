package fr.esrf.TangoApi.events;

import java.util.EventListener;

/**
 *
 * @author  pascal_verdier
 */
public interface ITangoPeriodicListener extends EventListener {

    public void periodic(TangoPeriodicEvent e);
}
