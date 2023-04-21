package com.ivis.xprocess.framework.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;

/**
 * The Diagnoser runs over every loaded xelement in the Datasource,
 * calling diagnose() to locate and fix issues.
 *
 * All problems and fixes are output to the xProcess log.
 */
public class Diagnoser {

    /**
     * This is the number put into new data sources and those on which this
     * version of Diagnostics has been run
     */
    public static final int DIAGNOSTICS_BUILD_NUMBER_VALUE = 16350;

    /**
     * A string representation of DIAGNOSTICS_BUILD_NUMBER_VALUE
     */
    public static final String DIAGNOSTICS_BUILD_NUMBER = "DIAGNOSTICS_BUILD_NUMBER";

    private static final Logger logger = Logger.getLogger(Diagnoser.class.getName());

    private static Diagnoser messageUtility;

    public static Diagnoser getInstance() {
        if (messageUtility == null) {
            messageUtility = new Diagnoser();
        }
        return messageUtility;
    }

    /**
     * @param ph - the persistenceHelper used to load the Datasource
     * @return an end of running message
     */
    public String run(IPersistenceHelper ph) {
        Set<String> uuids = new HashSet<String>();
        uuids.addAll(ph.getFileIndex().getIDs());
        logger.log(Level.INFO, "\n############ Run Diagnostics Starting #############");
        StringBuilder log = new StringBuilder("");
        ArrayList<Integer> problemCounter = new ArrayList<Integer>();
        problemCounter.add(new Integer(0));
        for (String uuid : uuids) {
            Xelement element = ph.getElement(uuid);
            if (element != null) {
                element.diagnose(log, problemCounter);
            }
        }
        logger.log(Level.INFO, log.toString());
        logger.log(Level.INFO, "\n############ Run Diagnostics Complete #############");
        ph.getRootExchangeElementContainer().setIntProperty(DIAGNOSTICS_BUILD_NUMBER, DIAGNOSTICS_BUILD_NUMBER_VALUE);
        String successfulRun = "Diagnostics ran successfully - ";
        String warningMessage = "\n\nWARNING: There could be a delay while changes are saved. Please wait several seconds (or until disk activity is complete) before restarting xProcess.";
        if (problemCounter.get(0) == 0) {
            return successfulRun + "No errors were identified.";
        }
        if (problemCounter.get(0) == 1) {
            return successfulRun + "1 error was identified and fixed. " + warningMessage;
        }
        return successfulRun + problemCounter.get(0) + " errors were identified and fixed. " + warningMessage;
    }
}
