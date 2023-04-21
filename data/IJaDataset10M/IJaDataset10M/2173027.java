package mirrormap;

/**
 * Receives notifications about the connection status of a local and remote {@link MirrorMap} instances.
 * <p>
 * Local connection events signal when local mirror maps are connected or disconnected from the server. The remote
 * connection events signal when peer mirror maps connect or disconnect from the server.
 * <p>
 * Knowing about peer mirror map connect/disconnects is useful if one of the peers is actually sourcing the data for the
 * map; in situations where it knows there are no further peers of the map it can stop feeding the map with data from
 * external systems, say.
 * 
 * @author Ramon Servadei
 */
public interface IMirrorMapConnectionListener {

    /**
     * Received when a local {@link MirrorMap} is connected to its server. The instance will receive remote changes.
     * 
     * @param mirrorMapName
     *            the name of the mirror map that has connected
     */
    void localConnected(String mirrorMapName);

    /**
     * Received when a {@link MirrorMap} is disconnected from its server. The instance will no longer be receiving
     * remote changes.
     * 
     * @param mirrorMapName
     *            the name of the mirror map that was disconnected
     */
    void localDisconnected(String mirrorMapName);

    /**
     * Received when any {@link MirrorMap} connects to the server. This is triggered by remote peers as well as the
     * local instance connecting.
     * 
     * @param mirrorMapName
     *            the name of the mirror map that has connected
     * @param remoteHost
     *            the remote host of the mirror map that disconnected
     * @param remotePort
     *            the TCP port of the remote host
     */
    void peerConnected(String mirrorMapName, String remoteHost, int remotePort);

    /**
     * Received when any {@link MirrorMap} disconnects from the server. This is only triggered by remote peer
     * disconnects. <b>Disconnecting the local instance will not trigger this.</b>
     * 
     * @param mirrorMapName
     *            the name of the mirror map that has disconnected
     * @param remoteHost
     *            the remote host of the mirror map that disconnected
     * @param remotePort
     *            the TCP port of the remote host
     */
    void peerDisconnected(String mirrorMapName, String remoteHost, int remotePort);
}
