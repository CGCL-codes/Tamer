package org.tigr.cloe.model.facade.datastoreFacade.lock;

import java.sql.*;
import org.tigr.common.Application;
import org.tigr.seq.datastore.*;
import org.tigr.seq.util.*;
import org.tigr.seq.tdb.exceptions.*;

public class TDBLock implements ILock {

    /**
    * An exception to be thrown if we want to abort the locking process,
    * probably because the user hit a cancel button. */
    public class AbortProcessException extends Exception {

        /**
         * 
         */
        private static final long serialVersionUID = 1618793238618420298L;
    }

    /**
    * The integer lock ID from the project database, editor_lock.id field 
    (initially null). */
    private Integer lockID;

    /**
    * A reference to the <code>Object</code> which owns the lock. */
    private Object owner;

    /**
    * A textual description of the task for which this lock is used, e.g.:
    * "Open Collection 2047". */
    private String task;

    /**
    * The user's Sybase login name. */
    private String login;

    /**
    * The project database name. */
    private String DBName;

    /**
    * The physical location (machine name) of the client machine. */
    private String machineName;

    /**
    * Constructor.
    *
    * @param    pOwner          The <code>Object</code> which is requesting
    *                           the lock.  Only this "owner" of the lock can
    *                           successfully remove the lock.
    * @param    pDBName         The name of the database which needs the lock.
    * @param    pTask           A task description, such as "Open collection 220".
    * @param    pWho            The Sybase login name of the user.
    * @param    pWhere          The name of the client machine.

    */
    public TDBLock(Object pOwner, String pDBName, String pTask, String pWho, String pWhere) {
        this.owner = pOwner;
        this.DBName = pDBName;
        this.task = pTask;
        this.login = pWho;
        this.machineName = pWhere;
        this.lockID = null;
    }

    public TDBLock() {
    }

    public int getID() throws DataStoreException {
        this.acquireLock();
        return this.lockID.intValue();
    }

    public void lockAssembly(int pAsmID) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockAssembly(this.DBName, pAsmID, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void unlockAssembly(int pAsmID) throws DataStoreException {
        if (this.lockID != null) {
            try {
                Application.getDatastore().getDao().unlockAssembly(this.DBName, pAsmID, this.lockID.intValue());
            } catch (SQLException sqle) {
                throw DataStoreException.repackageException(sqle);
            }
        }
    }

    public void lockAssemblyRange(int pAsmID, int pLeftBase, int pRightBase) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockAssemblyRange(this.DBName, pAsmID, pLeftBase, pRightBase, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void lockAssemblyObjects(int pAsmID) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockAssemblyObjects(this.DBName, pAsmID, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void lockCollectionSequences(int pCollectionID) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockCollectionSequences(this.DBName, pCollectionID, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void lockCollectionAssemblies(int pCollectionID) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockCollectionAssemblies(this.DBName, pCollectionID, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void unlockAssemblies() throws DataStoreException {
        if (this.lockID != null) {
            try {
                Application.getDatastore().getDao().unlockAssemblies(this.DBName, this.lockID.intValue());
            } catch (SQLException sqle) {
                throw DataStoreException.repackageException(sqle);
            }
        }
    }

    public void lockSequence(String pSeqName) throws DataStoreException {
        Flag cancelFlag = CancelHelper.getCancelFlag();
        try {
            this.acquireLock();
            if (cancelFlag.get()) {
                throw new AbortProcessException();
            }
            Application.getDatastore().getDao().lockSequence(this.DBName, pSeqName, this.lockID.intValue());
        } catch (AbortProcessException ape) {
        } catch (SQLException sqle) {
            throw DataStoreException.repackageException(sqle);
        } catch (TDBException te) {
            throw new DataStoreException(te.getMessage());
        }
    }

    public void unlockSequence(String pSeqName) throws DataStoreException {
        if (this.lockID != null) {
            try {
                Application.getDatastore().getDao().unlockSequence(this.DBName, pSeqName, this.lockID.intValue());
            } catch (SQLException sqle) {
                throw DataStoreException.repackageException(sqle);
            }
        }
    }

    /**
    * Acquire a new master lock record, if we don't already have one.
    *
    * @throws   DataStoreException.
    */
    private void acquireLock() throws DataStoreException {
        if (this.lockID == null) {
            Flag cancelFlag = CancelHelper.getCancelFlag();
            try {
                if (cancelFlag.get()) {
                    throw new AbortProcessException();
                }
                this.lockID = new Integer(Application.getDatastore().getDao().createLock(this.DBName, this.task, this.login, this.machineName));
            } catch (AbortProcessException ape) {
            } catch (SQLException sqle) {
                throw DataStoreException.repackageException(sqle);
            }
        }
    }

    public void removeLock(Object pCaller) throws DataStoreException {
        if (this.lockID != null && this.owner == pCaller) {
            try {
                Application.getDatastore().getDao().removeLocks(this.DBName, this.lockID.intValue());
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                throw DataStoreException.repackageException(sqle);
            }
            this.lockID = null;
        }
    }

    public void transferLockedObjects(ILock pOtherLock) throws DataStoreException {
        try {
            Application.getDatastore().getDao().assimilateLock(this.DBName, this.getID(), pOtherLock.getID());
        } catch (SQLException e) {
            throw new DataStoreException(e);
        }
    }

    /**
    * This method overrides Object.finalize(), and guarantees that the lock
    * will get deleted from the database before the TDBLock object is
    * garbage collected (unless <code>this.persistent</code> is <code>true</code>).
    *
    * @throws   DataStoreException.
    */
    protected void finalize() throws Throwable {
        this.removeLock(this.owner);
    }

    public void setID(int lockID) throws DataStoreException {
        this.lockID = lockID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((DBName == null) ? 0 : DBName.hashCode());
        result = PRIME * result + ((lockID == null) ? 0 : lockID.hashCode());
        result = PRIME * result + ((login == null) ? 0 : login.hashCode());
        result = PRIME * result + ((machineName == null) ? 0 : machineName.hashCode());
        result = PRIME * result + ((owner == null) ? 0 : owner.hashCode());
        result = PRIME * result + ((task == null) ? 0 : task.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TDBLock other = (TDBLock) obj;
        if (DBName == null) {
            if (other.DBName != null) return false;
        } else if (!DBName.equals(other.DBName)) return false;
        if (lockID == null) {
            if (other.lockID != null) return false;
        } else if (!lockID.equals(other.lockID)) return false;
        if (login == null) {
            if (other.login != null) return false;
        } else if (!login.equals(other.login)) return false;
        if (machineName == null) {
            if (other.machineName != null) return false;
        } else if (!machineName.equals(other.machineName)) return false;
        if (owner == null) {
            if (other.owner != null) return false;
        } else if (!owner.equals(other.owner)) return false;
        if (task == null) {
            if (other.task != null) return false;
        } else if (!task.equals(other.task)) return false;
        return true;
    }
}
