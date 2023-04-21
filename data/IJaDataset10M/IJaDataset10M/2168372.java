package com.ixora.common.remote;

/**
 * @author Daniel Moraru
 */
public final class RMIPingableFactory implements PingableFactory {

    /**
	 * RMI registry port.
	 */
    private int regPort;

    /**
	 * Remote object name as registered with the rmiregistry.
	 */
    private String remoteObjectName;

    /**
	 * Service ID.
	 */
    private int serviceID;

    /**
	 * Constructor for RMIPingableFactory.
     * @param remoteObjectName the remote object name as registered with
     * the RMI registry
     * @param rmiRegistryPort the RMI registry port number; if 0 the default
     * port number is used
     * @param serviceID
	 */
    public RMIPingableFactory(String remoteObjectName, int rmiRegistryPort, int serviceID) {
        super();
        if (remoteObjectName == null) {
            throw new IllegalArgumentException("null remote object name");
        }
        this.remoteObjectName = remoteObjectName;
        this.regPort = rmiRegistryPort;
        this.serviceID = serviceID;
    }

    /**
	 * @see com.ixora.common.remote.PingableFactory#getPingable(String)
	 */
    public Pingable getPingable(String host) {
        try {
            return new RMIPingable((PingableRemote) java.rmi.Naming.lookup("rmi://" + host + (regPort == 0 ? "" : (":" + regPort)) + "/" + remoteObjectName));
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * @see com.ixora.common.remote.PingableFactory#getServiceID()
	 */
    public int getServiceID() {
        return this.serviceID;
    }
}
