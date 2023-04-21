package org.personalsmartspace.pss_sm_synchroniser.impl;

import org.personalsmartspace.pss_sm_common.impl.ServiceMgmtException;
import org.personalsmartspace.sre.ems.api.IEventMgr;
import org.personalsmartspace.onm.api.IPeerGroupMgr;

/**
 * This provides an interface for the service finder
 */
public interface IServiceFinder {

    /**
     * open the service finders
     */
    public void openServiceFinders();

    /**
     * close the service finders
     */
    public void closeServiceFinders();

    /**
     * @return the Events Manager
     */
    public IEventMgr getEventsManager() throws ServiceMgmtException;

    public IPeerGroupMgr getPeerGroupMgr() throws ServiceMgmtException;
}
