package org.jtools.rjdbc.driver.impl;

import java.sql.SQLException;
import java.sql.Savepoint;
import org.jpattern.factory.Factory;

final class RJDBCSavepoint extends AbstractRJDBCObject implements Savepoint {

    private final int savepointId;

    private final String savepointName;

    protected RJDBCSavepoint(Factory<DriverRequest> requestFactory, long entityId, int savepointId, String savepointName) {
        super(requestFactory, entityId);
        this.savepointId = savepointId;
        this.savepointName = savepointName;
    }

    @Override
    public int getSavepointId() throws SQLException {
        return savepointId;
    }

    @Override
    public String getSavepointName() throws SQLException {
        return savepointName;
    }
}
