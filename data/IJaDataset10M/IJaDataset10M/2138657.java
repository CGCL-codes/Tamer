package com.telstra.ess.reporting.spi.lease;

import java.util.StringTokenizer;
import com.telstra.ess.*;
import com.telstra.ess.configuration.*;
import com.telstra.ess.event.*;
import com.telstra.ess.lease.*;
import com.telstra.ess.logging.*;
import com.telstra.ess.reporting.ReportingEventManager;
import com.telstra.ess.reporting.spi.*;

public class LeasedEssReportingEventManagerFactory implements EssReportingEventManagerFactory {

    private EssComponent factoryComp = new EssServiceComponent("LeasedEssAlarmManagerFactory");

    private ConfigurationManager confMgr = null;

    private EssLogger logger = null;

    private static final Object lockObject = new Object();

    public LeasedEssReportingEventManagerFactory() {
        super();
        logger = LoggingManager.getEssLoggerInstance(factoryComp);
    }

    public ReportingEventManager getReportingEventManager(EssComponent comp) throws EssFactoryException {
        if (comp == null) {
            throw new EssFactoryException("Could not get alarm manager instance - component was null");
        } else if (comp.getName() == null) {
            throw new EssFactoryException("Could not get alarm manager instance - component name was null");
        }
        if (confMgr == null) {
            confMgr = ConfigurationManager.getInstance(comp);
        }
        synchronized (lockObject) {
            LeasedResource r = LeaseManager.findLeasedResource(comp, ReportingEventManager.class);
            if (r != null) {
                r.renew();
                return (ReportingEventManager) r.getResource();
            }
            ReportingEventManager am = new ReportingEventManager(comp);
            try {
                logger.debug("Loading listeners...");
                String listeners = confMgr.getConfigurationItem("reporting.listeners", null);
                if (listeners != null) {
                    StringTokenizer st = new StringTokenizer(listeners, ", \n");
                    while (st.hasMoreElements()) {
                        String className = st.nextToken();
                        try {
                            Object obj = Class.forName(className).newInstance();
                            if (obj != null) {
                                if (obj instanceof EssEventListener) {
                                    EssEventListener listener = (EssEventListener) obj;
                                    am.getDispatcher().addEssEventListener(listener);
                                    logger.debug("Listener: " + className + ", is valid and associated");
                                } else {
                                    logger.warn(className + " was not a valid EssEventListener, ignoring");
                                }
                            } else {
                                logger.warn(className + " could not be initialized, aborting");
                            }
                        } catch (Throwable t) {
                            logger.error("Could not load alarming listener", t);
                        }
                    }
                } else {
                    logger.debug("No listeners found, attaching no listeners");
                }
            } catch (Throwable t) {
                logger.error("Could not load listeners", t);
            }
            r = new LeasedResource(comp, am);
            LeaseManager.addLeasedResource(r);
            return am;
        }
    }
}
