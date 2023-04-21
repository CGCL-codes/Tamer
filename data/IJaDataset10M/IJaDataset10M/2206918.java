package org.liris.schemerger.measurements.listeners;

import java.util.TreeMap;
import org.liris.schemerger.core.AbortException;
import org.liris.schemerger.measurements.EventHandler;
import org.liris.schemerger.measurements.ProcessLogger;

/**
 * Notified whenever the generation of all successors of a candidates chronicle
 * terminates.
 * 
 * @author Damien Cram
 * 
 */
public abstract class EndProcessingSuccessorsListener extends ProcessLogger {

    @Override
    protected void fillHandlerMap(TreeMap<Integer, EventHandler> map) {
        map.put(ProcessLogger.END_PROCESSING_SUCCESSORS, endProcSucc);
    }

    private int inCnt = 0;

    private EventHandler endProcSucc = new EventHandler() {

        public void handleEvent(Object... arr) throws AbortException {
            inCnt++;
            endProcSucc();
        }
    };

    public abstract void endProcSucc() throws AbortException;
}
