package org.neodatis.odb.core.layers.layer4;

import java.util.Properties;
import org.neodatis.tool.wrappers.net.NeoDatisIpAddress;

/** To express parameters that must be passed to a remote server.
 * 
 * If base id is defined then filename is null. If filename is defined, then baseId is null
 * 
 * @author osmadja
 * 
 *
 */
public class IOSocketParameter implements IBaseIdentification {

    public static final int TYPE_DATABASE = 1;

    public static final int TYPE_TRANSACTION = 2;

    private String destinationHost;

    private int port;

    private String baseIdentifier;

    private int type;

    private String user;

    private transient String password;

    /** Used for TYPE_TRANSACTION, to buld the entire transaction file name*/
    private long dateTimeCreation;

    /** To know if client runs on the same vm than the server. It is the case, we client / server communication
	 * can be optimized.
	 */
    protected boolean clientAndServerRunInSameVM;

    protected Properties properties;

    public IOSocketParameter(String host, int port, String identifier, int type, String user, String password) {
        this(host, port, identifier, type, -1, user, password, false);
    }

    public IOSocketParameter(String host, int port, String identifier, int type, long dtCreation, String user, String password) {
        this(host, port, identifier, type, dtCreation, user, password, false);
    }

    public IOSocketParameter(String host, int port, String identifier, int type, long dtCreation, String user, String password, boolean clientAndServerRunOnSameVm) {
        this.properties = new Properties();
        this.destinationHost = host;
        if (destinationHost.indexOf(".") == -1) {
            destinationHost = NeoDatisIpAddress.get(destinationHost);
        }
        this.port = port;
        this.baseIdentifier = identifier;
        this.type = type;
        this.dateTimeCreation = dtCreation;
        this.user = user;
        this.password = password;
        this.clientAndServerRunInSameVM = clientAndServerRunOnSameVm;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public int getPort() {
        return port;
    }

    public String getBaseIdentifier() {
        return baseIdentifier;
    }

    public boolean canWrite() {
        return true;
    }

    public int getType() {
        return type;
    }

    public boolean isDatabase() {
        return type == TYPE_DATABASE;
    }

    public boolean isTransaction() {
        return type == TYPE_TRANSACTION;
    }

    public long getDateTimeCreation() {
        return dateTimeCreation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return user;
    }

    public void setUserName(String user) {
        this.user = user;
    }

    public String toString() {
        return baseIdentifier + "@" + destinationHost + ":" + port;
    }

    public String getIdentification() {
        return toString();
    }

    public boolean isNew() {
        return false;
    }

    public boolean isLocal() {
        return false;
    }

    public boolean clientAndServerRunInSameVM() {
        return clientAndServerRunInSameVM;
    }

    public String getDirectory() {
        return "";
    }

    public String getFullIdentification() {
        return getIdentification();
    }

    public Properties getProperties() {
        return properties;
    }
}
