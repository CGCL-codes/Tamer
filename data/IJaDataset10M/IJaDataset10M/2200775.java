package java.security.cert;

/**
 * Parameters for CertStores that are retrieved via the <i>lightweight
 * directory access protocol</i> (<b>LDAP</b>).
 *
 * @see CertStore
 */
public class LDAPCertStoreParameters implements CertStoreParameters {

    /** The default LDAP port. */
    private static final int LDAP_PORT = 389;

    /** The server name. */
    private final String serverName;

    /** The LDAP port. */
    private final int port;

    /**
   * Create a new LDAPCertStoreParameters object, with a servername of
   * "localhost" and a port of 389.
   */
    public LDAPCertStoreParameters() {
        this("localhost", LDAP_PORT);
    }

    /**
   * Create a new LDAPCertStoreParameters object, with a specified
   * server name and a port of 389.
   *
   * @param serverName The LDAP server name.
   * @throws NullPointerException If <i>serverName</i> is null.
   */
    public LDAPCertStoreParameters(String serverName) {
        this(serverName, LDAP_PORT);
    }

    /**
   * Create a new LDAPCertStoreParameters object, with a specified
   * server name and port.
   *
   * @param serverName The LDAP server name.
   * @param port       The LDAP port.
   * @throws NullPointerException If <i>serverName</i> is null.
   */
    public LDAPCertStoreParameters(String serverName, int port) {
        if (serverName == null) throw new NullPointerException();
        this.serverName = serverName;
        this.port = port;
    }

    public Object clone() {
        return new LDAPCertStoreParameters(serverName, port);
    }

    /**
   * Return the server name.
   *
   * @return The server name.
   */
    public String getServerName() {
        return serverName;
    }

    /**
   * Return the port.
   *
   * @return the port.
   */
    public int getPort() {
        return port;
    }

    /**
   * Return a string representation of these parameters.
   *
   * @return The string representation of these parameters.
   */
    public String toString() {
        return "LDAPCertStoreParameters: [ serverName: " + serverName + "; port: " + port + " ]";
    }
}
