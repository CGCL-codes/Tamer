package mydomain.usertypes;

/**
 * User type that should be persisted to a VARCHAR column.
 */
public class IPAddress {

    String ipAddress;

    public IPAddress(String ipAddr) {
        ipAddress = ipAddr;
    }

    public String toString() {
        return ipAddress;
    }
}
