package Beans.Requests.MachineManagement.Components;

import org.virtualbox_3_2.VRDPAuthType;

/**
 * This class represents the VRDP settings of a virtual machine.
 *
 * @author Angel Sanadinov
 */
public class VRDPSettings extends MachineManagementComponent {

    private boolean serverState;

    private String serverPorts;

    private String serverAddress;

    private VRDPAuthType authenticationType;

    private int authenticationTimeout;

    private boolean allowMultipleConnections;

    private boolean reuseSingleConnection;

    private boolean rdpVideoChannelState;

    private int videoChannelQuality;

    /**
     * Constructs a VRDP settings object with the specified data.
     *
     * @param state VRDP server state
     * @param ports port(s) used by the VRDP server (comma separated)
     * @param address VRDP server address
     * @param authType authentication type
     * @param timeout authentication timeout (in ms)
     * @param multiConnections allow multiple connections?
     * @param reuseSingleConnection reuse single connection?
     * @param videoChannel RDP video channel state
     * @param vcQuality video channel quality (in percents)
     */
    public VRDPSettings(boolean state, String ports, String address, VRDPAuthType authType, int timeout, boolean multiConnections, boolean reuseSingleConnection, boolean videoChannel, int vcQuality) {
        this.serverState = state;
        this.serverPorts = ports;
        this.serverAddress = address;
        this.authenticationType = authType;
        this.authenticationTimeout = timeout;
        this.allowMultipleConnections = multiConnections;
        this.reuseSingleConnection = reuseSingleConnection;
        this.rdpVideoChannelState = videoChannel;
        this.videoChannelQuality = vcQuality;
    }

    /**
     * Retrieves the VRDP server state.
     *
     * @return <code>true</code> if the VRDP server is enabled or <code>false</code>
     *         if it is not
     */
    public boolean getServerState() {
        return serverState;
    }

    /**
     * Retrieves the port(s) used by the VRDP server.
     *
     * @return the VRDP server port(s) (multiple ports are separated by commas)
     */
    public String getServerPorts() {
        return serverPorts;
    }

    /**
     * Retrieves the VRDP server address.
     *
     * @return the VRDP server address
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Retrieves the VRDP authentication type.
     *
     * @return the authentication type
     */
    public VRDPAuthType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Retrieves the authentication timeout.
     *
     * @return the timeout in ms
     */
    public int getAuthenticationTimeout() {
        return authenticationTimeout;
    }

    /**
     * Retrieves the multiple connections allowed field state.
     *
     * @return <code>true</code> if multiple connections are allowed or
     *         <code>false</code> if they are not
     */
    public boolean getAllowMultipleConnections() {
        return allowMultipleConnections;
    }

    /**
     * Retrieves the reuse single connection field state.
     *
     * @return <code>true</code> if a single connection is to be reused or
     *         <code>false</code> if not
     */
    public boolean getReuseSingleConnection() {
        return reuseSingleConnection;
    }

    /**
     * Retrieves the state of the RDP video channel.
     *
     * @return <code>true</code> if the RDP channel is supported or <code>false</code>
     *         if it is not supported
     */
    public boolean getRdpVideoChannelState() {
        return rdpVideoChannelState;
    }

    /**
     * Retrieves the quality of the video channel.
     *
     * @return the video channel quality in percents.
     */
    public int getVideoChannelQuality() {
        return videoChannelQuality;
    }

    @Override
    public boolean isValid() {
        if (!serverState || (serverAddress != null && authenticationType != null && authenticationTimeout > 0 && (!rdpVideoChannelState || videoChannelQuality >= 0))) return true; else return false;
    }
}
