package dsb.bar.flowmeter.fus;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Service interface for the Flowmeter Update Service.
 */
public interface FlowmeterUpdateService extends Remote {

    /**
	 * Name of the remote object implementing this interface.
	 */
    public static final String NAME = "FUS";

    /**
	 * Register a callback with the Flowmeter Update Service.
	 * 
	 * @param callback
	 *            The callback that will receive updates. Should be non-
	 *            <code>null</code> and should not already be registered.
	 * @throws RemoteException
	 *             An error occurred while invoking the remote method.
	 */
    public void registerCallback(FlowmeterUpdateCallback callback, Meter meter) throws RemoteException;

    /**
	 * Unregister a callback from the Flowmeter Update Service.
	 * 
	 * @param callback
	 *            The callback that must be unregistered. Should be non-
	 *            <code>null</code> and should already be registered.
	 * @throws RemoteException
	 *             An error occurred while invoking the remote method.
	 */
    public void unregisterCallback(FlowmeterUpdateCallback callback) throws RemoteException;
}
