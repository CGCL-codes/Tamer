package mydomain.usertypes;

/**
 * Sample class that is persistable that has a field of a usertype.
 */
public class Machine {

    String hostName;

    IPAddress ip;

    public Machine(String host, IPAddress ip) {
        this.hostName = host;
        this.ip = ip;
    }

    public String getHostname() {
        return hostName;
    }

    public IPAddress getIP() {
        return ip;
    }

    public String toString() {
        return "Machine : " + hostName + " [" + ip.toString() + "]";
    }
}
