package org.dbunit;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for {@link IDatabaseConnection} events.
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 978 $ $Date: 2009-02-27 15:20:23 -0500 (Fri, 27 Feb 2009) $
 * @since 2.4.4
 */
public interface IOperationListener {

    /**
     * Is invoked immediately after a connection was newly created or an existing
     * connection is retrieved to do some work on it. It should be used to initialize the 
     * {@link DatabaseConfig} of the connection with user defined parameters.
     * @param connection The database connection 
     * @since 2.4.4
     */
    public void connectionRetrieved(IDatabaseConnection connection);

    /**
     * Notification of the completion of the {@link IDatabaseTester#onSetup()} method.
     * Should close the given connection if desired.
     * @param connection The database connection 
     * @since 2.4.4
     */
    public void operationSetUpFinished(IDatabaseConnection connection);

    /**
     * Notification of the completion of the {@link IDatabaseTester#onTearDown()} method
     * Should close the given connection if desired.
     * @param connection The database connection 
     * @since 2.4.4
     */
    public void operationTearDownFinished(IDatabaseConnection connection);

    /**
     * Simple implementation of the {@link IOperationListener} that does <b>not</b> close
     * the database connection after setUp and tearDown.
     * Can be used via {@link IDatabaseTester#setOperationListener(IOperationListener)} to avoid that connections are closed.
     * @since 2.4.5
     */
    public static final IOperationListener NO_OP_OPERATION_LISTENER = new IOperationListener() {

        private final Logger logger = LoggerFactory.getLogger(IDatabaseTester.class);

        public void connectionRetrieved(IDatabaseConnection connection) {
            logger.trace("connectionCreated(connection={}) - start", connection);
        }

        public void operationSetUpFinished(IDatabaseConnection connection) {
            logger.trace("operationSetUpDone(connection={}) - start", connection);
        }

        public void operationTearDownFinished(IDatabaseConnection connection) {
            logger.trace("operationTearDownDone(connection={}) - start", connection);
        }
    };
}
