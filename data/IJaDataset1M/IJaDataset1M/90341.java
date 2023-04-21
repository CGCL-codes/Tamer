package org.nightlabs.jfire.servermanager.db;

public abstract class AbstractDatabaseAdapter implements DatabaseAdapter {

    private boolean closed = false;

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws DatabaseException {
        this.closed = true;
    }

    protected void assertOpen() throws DatabaseAdapterClosedException {
        if (isClosed()) throw new DatabaseAdapterClosedException("This DatabaseAdapter is already closed: " + this);
    }
}
