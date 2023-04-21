package com.ixora.rms.services;

import java.rmi.RemoteException;
import java.util.Collection;
import com.ixora.rms.HostInformation;
import com.ixora.rms.HostState;
import com.ixora.common.Service;
import com.ixora.common.remote.ServiceState;
import com.ixora.rms.exception.RMSException;

/**
 * HostMonitorService.
 * @author Daniel Moraru
 */
public interface HostMonitorService extends Service {

    /**
	 * HostMonitorService listener.
	 */
    public interface Listener {

        /**
		 * Invoked when the state of the given service on the given host has changed.
		 * @param host
		 * @param serviceID the service for which the state has changed as in HostReachability
		 * @param state
		 */
        void hostStateChanged(String host, int serviceID, ServiceState state);

        /**
		 * Invoked when the host info needs updating.
		 * @param host
		 * @param info
		 */
        void updateHostInfo(String host, HostInformation info);

        /**
		 * Invoked before a host is removed.
		 * @param host
		 */
        void aboutToRemoveHost(String host);
    }

    /**
	 * Adds a listener.
	 * @param listener
	 */
    void addListener(Listener listener);

    /**
	 * Removes a listener.
	 * @param listener
	 */
    void removeListener(Listener listener);

    /**
	 * @param host
	 * @return the state of the given host
	 */
    HostState getHostState(String host);

    /**
	 * @return the state of all hosts
	 */
    HostState[] getHostsStates();

    /**
	 * @param host
	 * @return the host info for the given host
	 * @throws RMSException
	 */
    HostInformation getHostInfo(String host) throws RMSException, RemoteException;

    /**
	 * @return the host info for all the hosts
	 * @throws RMSException
	 */
    HostInformation[] getHostsInfo() throws RMSException, RemoteException;

    /**
	 * Registers the given hosts for monitoring.
	 * @param hosts
	 * @param waitForState
	 */
    void addHosts(Collection<String> hosts, boolean waitForState);

    /**
	 * Registers the given host for monitoring.
	 * @param host
	 * @param waitForState
	 */
    void addHost(String host, boolean waitForState);

    /**
	 * Deregisters the given hosts from monitoring.
	 * @param hosts
	 */
    void removeHosts(Collection<String> hosts);

    /**
	 * Deregisters the given host from monitoring.
	 * @param host
	 */
    void removeHost(String host);
}
