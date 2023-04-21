package com.volantis.mcs.repository;

public class DeprecatedConnection implements RepositoryConnection {

    private final DeprecatedRepository repository;

    private final RepositoryConnection connection;

    public DeprecatedConnection(DeprecatedRepository repository, RepositoryConnection connection) {
        this.repository = repository;
        this.connection = connection;
    }

    public boolean beginOperationSet() throws RepositoryException {
        return connection.beginOperationSet();
    }

    public boolean endOperationSet() throws RepositoryException {
        return connection.endOperationSet();
    }

    public boolean abortOperationSet() throws RepositoryException {
        return connection.abortOperationSet();
    }

    public boolean supportsOperationSets() {
        return connection.supportsOperationSets();
    }

    public void disconnect() throws RepositoryException {
        connection.disconnect();
    }

    public boolean isConnected() throws RepositoryException {
        return connection.isConnected();
    }

    public Repository getRepository() {
        return repository;
    }

    public RepositoryConnection getUnderLyingConnection() throws RepositoryException {
        return connection;
    }
}
