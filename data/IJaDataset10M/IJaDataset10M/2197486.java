package net.sf.contrail.core.impl.directory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.contrail.gaevfs.DirectoryDescriptor;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockReleaseFailedException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class DatastoreLockFactory extends LockFactory {

    public static final String KIND = DatastoreLockFactory.class.getName();

    protected DatastoreService _datastore;

    protected DirectoryDescriptor _directory;

    protected Entity _locksRoot;

    public class DatastoreLock extends Lock {

        Entity _entity = null;

        String _name;

        public DatastoreLock(String lockFileName) {
            _name = lockFileName;
        }

        public boolean obtain() throws IOException {
            Transaction transaction = _datastore.beginTransaction();
            try {
                Entity entity = null;
                try {
                    Key key = KeyFactory.createKey(_locksRoot.getKey(), "lock", _name);
                    entity = _datastore.get(key);
                    return false;
                } catch (EntityNotFoundException x) {
                }
                entity = new Entity("lock", _name, _locksRoot.getKey());
                _datastore.put(entity);
                transaction.commit();
                _entity = entity;
                transaction = null;
                return true;
            } finally {
                if (transaction != null) {
                    try {
                        transaction.rollback();
                    } catch (Throwable t) {
                    }
                }
            }
        }

        public void release() throws LockReleaseFailedException {
            if (_entity == null) return;
            Transaction transaction = _datastore.beginTransaction();
            try {
                try {
                    _datastore.delete(_entity.getKey());
                    transaction.commit();
                    transaction = null;
                } catch (Throwable x) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Datastore Failure", x);
                    throw new LockReleaseFailedException(x.getMessage());
                }
                return;
            } finally {
                if (transaction != null) {
                    try {
                        transaction.rollback();
                    } catch (Throwable t) {
                    }
                }
            }
        }

        public boolean isLocked() {
            return _entity != null;
        }

        public String toString() {
            return "DatastoreLock@" + _name;
        }
    }

    public DatastoreLockFactory(DatastoreService datastoreService, DirectoryDescriptor directoryDescriptor) {
        _datastore = datastoreService;
        _directory = directoryDescriptor;
        _locksRoot = createLockEntityRoot();
    }

    protected Entity createLockEntityRoot() {
        Key key = KeyFactory.createKey(_directory.getKey(), KIND, ".locks");
        Entity entity = null;
        try {
            entity = _datastore.get(key);
        } catch (EntityNotFoundException x) {
            entity = new Entity(KIND);
            entity.setProperty(Entity.KEY_RESERVED_PROPERTY, key);
            _datastore.put(entity);
        }
        return entity;
    }

    @Override
    public void clearLock(String lockName) throws IOException {
        if (lockPrefix != null) {
            lockName = lockPrefix + "-" + lockName;
        }
        Key lockKey = KeyFactory.createKey(_locksRoot.getKey(), "lock", lockName);
        _datastore.delete(lockKey);
    }

    @Override
    public Lock makeLock(String lockName) {
        if (lockPrefix != null) {
            lockName = lockPrefix + "-" + lockName;
        }
        return new DatastoreLock(lockName);
    }
}
