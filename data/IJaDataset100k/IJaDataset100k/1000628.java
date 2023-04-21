package org.antdepo.common;

/**
 * represents info about a node where commands can be executed
 */
public class NodeDesc implements INodeDesc {

    protected static final String USER_AT_HOSTNAME_REGEX = "([^@])+@([^@])+";

    /**
     * reference to the nodename
     */
    private final String nodename;

    /**
     * reference to the fwkNode. this is initialized to the {@link org.antdepo.common.Framework#getFrameworkNodeName()}
     */
    private final String fwkNode;

    /**
     * Base constructor.
     *
     * @param nodename    Name of node
     * @param fwkNodeName Name of framework node
     */
    protected NodeDesc(final String nodename, final String fwkNodeName) {
        this.nodename = nodename;
        this.fwkNode = fwkNodeName;
    }

    /**
     * Factory method. Calls base constructor.
     *
     * @param nodename    Name of node
     * @param fwkNodeName name of framework node name
     * @return new instance of NodeDesc
     */
    public static NodeDesc create(final String nodename, final String fwkNodeName) {
        return new NodeDesc(nodename, fwkNodeName);
    }

    /**
     * getter to nodename
     *
     * @return value of nodename
     */
    public String getNodename() {
        return nodename;
    }

    /**
     * Checks if node is local by comparing {@link org.antdepo.common.Framework#getFrameworkNodeName}
     * to {@link #nodename}. Embedded usernames are stripped out before the comparison is made.
     * @return true if local
     */
    public boolean isLocal() {
        final String hostname;
        if (containsUserName()) {
            hostname = extractHostname();
        } else {
            hostname = nodename;
        }
        final String fwkNodeHostname;
        if (containsUserName(fwkNode)) {
            fwkNodeHostname = extractHostname(fwkNode);
        } else {
            fwkNodeHostname = fwkNode;
        }
        return fwkNodeHostname.equals(hostname);
    }

    /**
     * Checks if node is remote by checking if node is NOT local.
     *
     * @return true if not local.
     */
    public boolean isNotLocal() {
        return !isLocal();
    }

    /**
     * Getter to fwkNode
     *
     * @return framework nodename
     */
    public String getFrameworkNodename() {
        return fwkNode;
    }

    /**
     * prints out information useful for debugging.
     *
     * @return string representation of object state
     */
    public String toString() {
        return "NodeDesc{" + "nodename=" + nodename + ", fwkNode=" + fwkNode + ", islocal=" + isLocal() + "}";
    }

    public boolean equals(final INodeDesc node) {
        return nodename.equals(node.getNodename()) && fwkNode.equals(node.getFrameworkNodename());
    }

    public int hashCode() {
        return nodename.hashCode();
    }

    /**
     * Checks if nodename contains a user name
     *
     * @param nodename hostname value
     * @return true if matches a "user@host" pattern
     */
    public boolean containsUserName(final String nodename) {
        if (null == nodename) throw new IllegalArgumentException("Null hostname value");
        return nodename.matches(USER_AT_HOSTNAME_REGEX);
    }

    public boolean containsUserName() {
        return containsUserName(getNodename());
    }

    public String extractUserName(final String nodename) {
        if (containsUserName(nodename)) {
            return nodename.substring(0, nodename.indexOf("@"));
        } else {
            throw new IllegalArgumentException("nodename does not contain a user: " + nodename);
        }
    }

    public String extractUserName() {
        return extractUserName(nodename);
    }

    public String extractHostname(final String nodename) {
        if (containsUserName(nodename)) {
            return nodename.substring(nodename.indexOf("@") + 1, nodename.length());
        } else {
            return nodename;
        }
    }

    public String extractHostname() {
        return extractHostname(nodename);
    }
}
