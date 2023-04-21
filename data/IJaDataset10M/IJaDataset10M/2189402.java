package org.icenigrid.gridsam.core.plugin.connector.condor;

import java.util.Set;
import org.icenigrid.gridsam.core.MutableJobInstance;

/**
 * Interface representing a listener for condor event
 */
public interface CondorEventListener {

    /**
     * get the set event codes interested by this event listener
     * 
     * @return Set of event code (string)
     */
    public Set getEventCodes();

    /**
     * condor event received for a particular job
     * 
     * @param pInstance
     *            the job instance the event is related
     * @param pEventCode
     *            the event code
     * @param pDesc
     *            a textual description from the condor sub-system
     */
    public void onEvent(MutableJobInstance pInstance, String pEventCode, String pDesc);
}
