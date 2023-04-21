package Beans.Requests.MachineManagement.Components;

import org.virtualbox_3_2.NATProtocol;

/**
 * This class represents a port forwarding rule of a virtual machine's NAT engine.
 *
 * @author Angel Sanadinov
 */
public class NATEngineForwardingRule extends MachineManagementComponent {

    private String name;

    private NATProtocol protocol;

    private String hostIP;

    private int hostPort;

    private String guestIP;

    private int guestPort;

    /**
     * Constructs a port forwarding rule settings object.
     *
     * @param name the name of the rule
     * @param protocol the protocol that will be used
     * @param hostIP the interface IP to which the rule will apply (can be <code>null</code>)
     * @param hostPort the host port number, on which to listen
     * @param guestIP the guest IP, to which the NAT engine will forward packets
     * @param guestPort the guest port number, to which the packets will be forwarded
     */
    public NATEngineForwardingRule(String name, NATProtocol protocol, String hostIP, int hostPort, String guestIP, int guestPort) {
        this.name = name;
        this.protocol = protocol;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
        this.guestIP = guestIP;
        this.guestPort = guestPort;
    }

    /**
     * Retrieves the name of the port forwarding rule.
     *
     * @return the rule name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the port forwarding rule's protocol.
     *
     * @return the protocol
     */
    public NATProtocol getProtocol() {
        return protocol;
    }

    /**
     * Retrieves the host interface's IP, on which to listen for packets.
     *
     * @return the host IP
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * Retrieves the host port, on which to listen for packets.
     *
     * @return the host port
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * Retrieves the IP address of the guest, to which packets are forwarded.
     *
     * @return the guest IP
     */
    public String getGuestIP() {
        return guestIP;
    }

    /**
     * Retrieves the guest port, to which packets are forwarded.
     *
     * @return
     */
    public int getGuestPort() {
        return guestPort;
    }

    @Override
    public boolean isValid() {
        if (name != null && protocol != null && hostPort >= 0 && hostPort <= 65535 && guestPort >= 0 && guestPort <= 65535) return true; else return false;
    }
}
