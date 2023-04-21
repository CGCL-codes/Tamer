package org.gvsig.remoteClient;

import java.io.File;

public abstract class OGCProtocolHandler {

    /**
	 * procotol handler name
	 */
    protected String name;

    /**
     * protocol handler version
     */
    protected String version;

    /**
     * host of the WMS to connect
     */
    protected String host;

    /**
     *  port number of the comunication channel of the WMS to connect
     */
    protected String port;

    /**
	 * @return Returns the host.
	 */
    public String getHost() {
        return host;
    }

    /**
	 * @param host The host to set.
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the port.
	 */
    public String getPort() {
        return port;
    }

    /**
	 * @param port The port to set.
	 */
    public void setPort(String port) {
        this.port = port;
    }

    /**
	 * @return Returns the version.
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * @param version The version to set.
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * parses the data retrieved by the Capabilities XML document
     * 
     */
    public abstract boolean parseCapabilities(File f);
}
