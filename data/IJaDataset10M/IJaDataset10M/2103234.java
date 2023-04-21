package com.objectsql.datasource.decorator;

import com.objectsql.datasource.ExecutionPhase;
import java.sql.Connection;
import java.sql.SQLException;

public class ReadOnlyConnectionDecorator implements ConnectionDecorator {

    private ConnectionDecorator decorator;

    public ReadOnlyConnectionDecorator() {
        this.decorator = new NullDecorator();
    }

    public ReadOnlyConnectionDecorator(ConnectionDecorator decorator) {
        this.decorator = decorator;
    }

    public void decorate(Connection connection, ExecutionPhase phase) {
        try {
            if (ExecutionPhase.BEFORE_EXECUTE.equals(phase)) {
                connection.setReadOnly(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
