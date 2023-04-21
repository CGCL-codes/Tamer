package org.jdbcfacade;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class CommandTestCase extends MockObjectTestCase {

    protected Mock createConnectionMock(String sql, PreparedStatement preparedStatement) {
        Mock mockConnection = null;
        mockConnection = mock(Connection.class);
        mockConnection.expects(once()).method("prepareStatement").with(eq(sql)).will(returnValue(preparedStatement));
        mockConnection.expects(once()).method("getWarnings").will(returnValue(null));
        return mockConnection;
    }

    protected Connection createMockedConnection(String sql, PreparedStatement preparedStatement) {
        Connection connection = null;
        Mock mockConnection = null;
        mockConnection = createConnectionMock(sql, preparedStatement);
        connection = (Connection) mockConnection.proxy();
        return connection;
    }

    protected ConnectionSource createMockedConnectionSource(String sql, PreparedStatement preparedStatement) {
        Connection connection = null;
        Mock mockConnectionSource = null;
        ConnectionSource connectionSource = null;
        connection = createMockedConnection(sql, preparedStatement);
        mockConnectionSource = mock(ConnectionSource.class);
        mockConnectionSource.expects(once()).method("getConnection").will(returnValue(connection));
        mockConnectionSource.expects(once()).method("returnConnection").with(eq(connection));
        connectionSource = (ConnectionSource) mockConnectionSource.proxy();
        return connectionSource;
    }

    private Mock createPreparedStatementMock(String executeMethodName) {
        Mock mockPreparedStatement = null;
        mockPreparedStatement = mock(PreparedStatement.class);
        mockPreparedStatement.expects(once()).method(executeMethodName).will(returnValue(1));
        mockPreparedStatement.expects(once()).method("getWarnings").will(returnValue(null));
        mockPreparedStatement.expects(once()).method("close");
        return mockPreparedStatement;
    }

    protected Mock createQueryPreparedStatementMock() {
        return createPreparedStatementMock("executeQuery");
    }

    protected Mock createUpdatePreparedStatementMock() {
        return createPreparedStatementMock("executeUpdate");
    }

    protected ConnectionSource createConnectionSourceThatThrowsExceptionOnGetConnection(String exceptionMessage) {
        SQLException sqlException;
        Mock mockConnectionSource;
        ConnectionSource connectionSource;
        sqlException = new SQLException(exceptionMessage);
        mockConnectionSource = mock(ConnectionSource.class);
        mockConnectionSource.expects(once()).method("getConnection").will(throwException(sqlException));
        connectionSource = (ConnectionSource) mockConnectionSource.proxy();
        return connectionSource;
    }
}
