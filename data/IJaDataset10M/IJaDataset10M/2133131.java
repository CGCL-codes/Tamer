package net.sourceforge.jabm.report;

import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import org.apache.log4j.Logger;

/**
 * A report which initialises its variables at the start of each simulation
 * and updates them at the end of each simulation.
 * 
 * @author Steve Phelps
 *
 */
public class SimulationFinishedReport extends AbstractReport implements Report {

    static Logger logger = Logger.getLogger(SimulationFinishedReport.class);

    @Override
    public void eventOccurred(SimEvent event) {
        super.eventOccurred(event);
        if (event instanceof SimulationStartingEvent) {
            reportVariables.initialise((SimulationStartingEvent) event);
        }
        if (event instanceof SimulationFinishedEvent) {
            reportVariables.compute((SimulationFinishedEvent) event);
        }
        if (event instanceof BatchFinishedEvent) {
            reportVariables.dispose(event);
        }
    }
}
