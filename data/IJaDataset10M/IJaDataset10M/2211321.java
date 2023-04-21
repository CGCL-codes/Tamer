package org.springframework.jca.cci.connection;

import javax.resource.cci.Connection;
import org.springframework.transaction.support.ResourceHolderSupport;

/**
 * Connection holder, wrapping a CCI Connection.
 *
 * <p>CciLocalTransactionManager binds instances of this class
 * to the thread, for a given ConnectionFactory.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Thierry Templier
 * @author Juergen Hoeller
 * @since 1.2
 * @see CciLocalTransactionManager
 * @see ConnectionFactoryUtils
 */
public class ConnectionHolder extends ResourceHolderSupport {

    private final Connection connection;

    public ConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
