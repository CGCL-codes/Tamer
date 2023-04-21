package org.unicore.resources;

/**
 * Resource for StorageServers such as bulk storage and tape archives and 
 * backup facilities as well as convential file systems not modelled by
 * other classes.
 *
 * @author D. Snelling (fecit)
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 3
 *
 * @version $Id: StorageServer.java,v 1.3 2004/06/06 18:37:24 svenvdb Exp $
 *
 **/
public class StorageServer extends PathedStorage {

    static final long serialVersionUID = -1531669629777247147L;

    public StorageServer() {
        this("", 0.0, 0.0, 0.0, (String) null, (String) null);
    }

    /**
     * Create a new StorageServer.
     *
     * @param description A description of the StorageServer
     * @param maxrequest Maximum amount of storage allowed (in megabytes)
     * @param defaultrequest Amount of storage granted if no specific request received (in megabytes)
     * @param minrequest Minimum amount of storage allowed (in megabytes)
     * @param path The subdirectory within the StorageServer
     * @param server_name Name of the storage server
     *
     * @since AJO 4.0
     *
     **/
    public StorageServer(String description, double maxrequest, double defaultrequest, double minrequest, String path, String server_name) {
        super(description, maxrequest, defaultrequest, minrequest, path);
        setName(server_name);
    }

    /**
     * Set the name of the storage server.
     *
     **/
    public void setName(String server_name) {
        if (server_name == null) {
            super.setName("StorageServer");
        } else {
            super.setName(server_name);
        }
    }

    /**
     * StorageServers are equals if they have the same name
     *
     **/
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() == getClass()) {
            return ((StorageServer) o).getName().equals(getName());
        } else {
            return false;
        }
    }

    /**
     * Return a hash code derived from the name of the StorageServer.
     *
     **/
    public int hashCode() {
        return getName().hashCode();
    }
}
