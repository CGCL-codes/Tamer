package com.continuent.tungsten.router.resource;

import com.continuent.tungsten.commons.config.TungstenProperties;
import static com.continuent.tungsten.router.resource.ResourceType.*;

public class Replicator extends Resource {

    private static final long serialVersionUID = 8153881753668230575L;

    private int port = 0;

    private String host = null;

    private String role = null;

    private String source = null;

    private String dataServiceName = null;

    public Replicator(String name, String description) {
        super(REPLICATOR, name, description);
    }

    public Replicator(TungstenProperties props) {
        props.applyProperties(this, true);
    }

    /**
	 * @return the port
	 */
    public int getPort() {
        return port;
    }

    /**
	 * @param port the port to set
	 */
    public void setPort(int port) {
        this.port = port;
    }

    /**
	 * @return the host
	 */
    public String getHost() {
        return host;
    }

    /**
	 * @param host the host to set
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * @return the role
	 */
    public String getRole() {
        return role;
    }

    /**
	 * @param role the role to set
	 */
    public void setRole(String role) {
        this.role = role;
    }

    /**
	 * @return the source
	 */
    public String getSource() {
        return source;
    }

    /**
	 * @param source the source to set
	 */
    public void setSource(String source) {
        this.source = source;
    }

    public String getDataServiceName() {
        return dataServiceName;
    }

    public void setDataServiceName(String dataServiceName) {
        this.dataServiceName = dataServiceName;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append("(").append("source=").append(getSource());
        builder.append(", role=").append(getRole()).append(", address=").append(getHost());
        builder.append(":").append(getPort()).append(")");
        return builder.toString();
    }
}
