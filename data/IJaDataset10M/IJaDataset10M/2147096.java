package com.volantis.mcs.repository;

/**
 * An interface for repository connections that wrap other repository
 * connections.
 */
public interface WrappedRepositoryConnection {

    /**
     * Get the wrapped repository connection
     * 
     * @return The wrapped repository connection.
     * @throws RepositoryException A problem in the repository.
     */
    public RepositoryConnection getWrappedRepositoryConnection() throws RepositoryException;
}
