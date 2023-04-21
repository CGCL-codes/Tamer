package de.objectcode.time4u.store;

import java.util.Collection;

public interface ISyncStore {

    String getSyncTarget();

    boolean isWorkItemIncremental() throws RepositoryException;

    long getLatestProjectRevision() throws RepositoryException;

    IDMapping getProjectMapping() throws RepositoryException;

    IDMapping getTaskMapping() throws RepositoryException;

    void markProjectSynchronized(long revision, IDMapping projectMapping, IDMapping taskMapping) throws RepositoryException;

    Collection<Day> getDirtyDays() throws RepositoryException;

    void markWorkItemSynchronized(Collection<Day> days) throws RepositoryException;
}
