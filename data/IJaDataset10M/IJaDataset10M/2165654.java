package org.opennms.netmgt.vulnscand;

/**
 * Class that holds the return values when parsing a port/protocol field from
 * Nessus.
 */
public class PortValues {

    int port;

    String protocol;

    public PortValues() {
        port = -1;
        protocol = null;
    }

    public void useDefaults() {
        protocol = "unknown";
    }

    public boolean isValid() {
        if ((protocol != null)) return true; else return false;
    }

    public String toString() {
        return ("port: " + port + "\nprotocol: " + protocol + "\n");
    }
}
