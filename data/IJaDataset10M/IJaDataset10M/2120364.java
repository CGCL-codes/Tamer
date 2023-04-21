package com.sun.jndi.ldap;

import java.io.OutputStream;
import javax.naming.InterruptedNamingException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import com.sun.jndi.ldap.pool.PoolCallback;
import com.sun.jndi.ldap.pool.PooledConnection;
import com.sun.jndi.ldap.pool.PooledConnectionFactory;

/**
 * Creates an LdapClient. Encapsulates the parameters required to create
 * an LdapClient and provides methods for returning appropriate exceptions
 * to throw when acquiring a pooled LdapClient fails.
 *
 * @author Rosanna Lee
 */
final class LdapClientFactory implements PooledConnectionFactory {

    private final String host;

    private final int port;

    private final String socketFactory;

    private final int connTimeout;

    private final int readTimeout;

    private final OutputStream trace;

    LdapClientFactory(String host, int port, String socketFactory, int connTimeout, int readTimeout, OutputStream trace) {
        this.host = host;
        this.port = port;
        this.socketFactory = socketFactory;
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;
        this.trace = trace;
    }

    public PooledConnection createPooledConnection(PoolCallback pcb) throws NamingException {
        return new LdapClient(host, port, socketFactory, connTimeout, readTimeout, trace, pcb);
    }

    public String toString() {
        return host + ":" + port;
    }
}
