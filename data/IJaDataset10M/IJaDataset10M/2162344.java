package de.knup.jedi.jayshare;

import java.util.*;
import java.text.*;

/**
 * Lookup up service information of currently available users.
 *
 * @version 0.1 $Revision: 1.4 $
 * @author <a href='mailto:berni@knup.de'>B. Pietsch</a>
 */
public class ServiceLookupList {

    /**
   * Service information.
   */
    public static class Service {

        /**
     * Protocol (e.g. "http" or "ftp")
     */
        private String proto;

        /**
     * Hostname or IP address (e.g. "gary7.nsa.gov" or "208.47.125.33")
     */
        private String host;

        /**
     * Port number the server is listening at.
     */
        private int port;

        /**
     * Create a new Service information entry.
     */
        public Service(String proto, String host, int port) {
            this.proto = proto;
            this.host = host;
            this.port = port;
        }

        /**
     * @return the protocol name.
     */
        public String getProtocol() {
            return proto;
        }

        /**
     * @return the hostname.
     */
        public String getHostname() {
            return host;
        }

        /**
     * @return the port number.
     */
        public int getPort() {
            return port;
        }
    }

    /**
   * Lookup list (actually a tree).
   */
    private TreeMap tree;

    /**
   * Create a new ServiceLookupList.
   */
    public ServiceLookupList() {
        tree = new TreeMap(Collator.getInstance());
    }

    /**
   * Insert a new user and service information entry.
   * @param hashedID is the user's hashed JID.
   * @param service is the service information entry connected to the user.
   */
    public void setPair(String hashedID, Service service) {
        if (tree.containsKey(hashedID)) tree.remove(hashedID);
        tree.put(hashedID, service);
    }

    /**
   * Remove a user from the list.
   */
    public void removeHashedID(String hashedID) {
        tree.remove(hashedID);
    }

    /**
   * Search for the user's service information.
   */
    public Service getService(String hashedID) {
        return (Service) tree.get(hashedID);
    }
}
