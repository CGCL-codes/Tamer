package xplanetconfigurator.gui;

import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *<br>
 * $Author: wiedthom $<br>
 * $Date: 2005/10/26 17:01:07 $<br>
 * $Revision: 1.1.1.1 $<br>
 *
 *
 * @author  tom
 */
public class XPlanetStarter extends SwingWorker<String, String> {

    private static int instanceNumber;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private XPlanetTimer timer;

    public XPlanetStarter() {
        instanceNumber++;
    }

    @Override
    protected void done() {
        this.logger.fine("...DONE");
    }

    @Override
    protected String doInBackground() throws Exception {
        this.logger.fine("About to start XPlanetTimer...");
        this.getTimer().run();
        return "Started Timer";
    }

    /**
     * @return the timer
     */
    public XPlanetTimer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(XPlanetTimer timer) {
        this.timer = timer;
    }
}
