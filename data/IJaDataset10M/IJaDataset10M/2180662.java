package org.shalma.persistence.internal;

import org.shalma.persistence.EntityCursor;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;

public class PrimaryIndexImpl<PK, E> implements org.shalma.persistence.PrimaryIndex<PK, E> {

    private PrimaryIndex index;

    public PrimaryIndexImpl(PrimaryIndex primaryIndex) {
        this.index = primaryIndex;
    }

    public long count() {
        try {
            return index.count();
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public boolean delete(PK key) throws org.shalma.persistence.DatabaseException {
        try {
            return index.delete(key);
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public EntityCursor<E> entities() {
        try {
            return new EntityCursorImpl<E>(index.entities());
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public EntityCursor<E> entities(PK fromKey, boolean fromInclusive, PK toKey, boolean toInclusive) {
        try {
            return new EntityCursorImpl<E>(index.entities(fromKey, fromInclusive, toKey, toInclusive));
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public E get(PK key) {
        try {
            return (E) index.get(key);
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public EntityCursor<PK> keys(PK fromKey, boolean fromInclusive, PK toKey, boolean toInclusive) {
        try {
            return new EntityCursorImpl<PK>(index.keys(fromKey, fromInclusive, toKey, toInclusive));
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }

    public E put(E entity) {
        try {
            return (E) index.put(entity);
        } catch (DatabaseException e) {
            throw new org.shalma.persistence.DatabaseException(e);
        }
    }
}
