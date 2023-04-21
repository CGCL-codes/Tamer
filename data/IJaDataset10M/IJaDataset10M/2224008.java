package Beans.Requests.NetworkManagement.Components;

/**
 * This class represents the configuration of a network's DHCP server.
 *
 * @author Angel Sanadinov
 */
public class DHCPServerConfiguration extends NetworkManagementComponent {

    private String serverIPAddress;

    private String serverNetworkMask;

    private String addressRangeLow;

    private String addressRangeHigh;

    private boolean serverState;

    /**
     * Constructs a DHCP server configuration object with the specified data.
     *
     * @param serverIPAddress the DHCP server IP address
     * @param serverNetworkMask the DHCP server network mask
     * @param addressRangeLow the low end of available addresses from the DHCP server
     * @param addressRangeHigh the high end of available addresses from the DHCP server
     * @param serverState DHCP server state (used when retrieving data)
     */
    public DHCPServerConfiguration(String serverIPAddress, String serverNetworkMask, String addressRangeLow, String addressRangeHigh, boolean serverState) {
        this.serverIPAddress = serverIPAddress;
        this.serverNetworkMask = serverNetworkMask;
        this.addressRangeLow = addressRangeLow;
        this.addressRangeHigh = addressRangeHigh;
        this.serverState = serverState;
    }

    /**
     * Retrieves the DHCP server's IP address.
     *
     * @return the IP address
     */
    public String getServerIPAddress() {
        return serverIPAddress;
    }

    /**
     * Retrieves the DHCP server's network mask
     *
     * @return the network mask
     */
    public String getServerNetworkMask() {
        return serverNetworkMask;
    }

    /**
     * Retrieves the low end of available addresses from the DHCP server.
     *
     * @return the lowest IP address
     */
    public String getAddressRangeLow() {
        return addressRangeLow;
    }

    /**
     * Retrieves the high end of available addresses from the DHCP server.
     *
     * @return the highest IP address
     */
    public String getAddressRangeHigh() {
        return addressRangeHigh;
    }

    /**
     * Retrieves the state of the DHCP server. <br>
     *
     * @return <code>true</code> if the DHCP server is enabled or <code>false</code> if
     *         not
     */
    public boolean getServerState() {
        return serverState;
    }

    @Override
    public boolean isValid() {
        if (serverIPAddress != null && serverNetworkMask != null && addressRangeLow != null && addressRangeHigh != null) return true; else return false;
    }
}
