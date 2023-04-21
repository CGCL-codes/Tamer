package test.tx;

import junit.framework.*;
import org.ozoneDB.LocalDatabase;
import org.ozoneDB.core.*;
import org.ozoneDB.core.User;
import test.OzoneTestCase;

/**
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.7 $Date: 2004/10/21 18:33:59 $
 */
public class LockTest extends OzoneTestCase {

    public LockTest(String name) {
        super(name);
    }

    public void testExclusiveLock() throws Exception {
        if (!(db() instanceof LocalDatabase)) {
            return;
        }
        LocalDatabase db = (LocalDatabase) db();
        User owner = db.getServer().getUserManager().userForName(db.getUserName());
        Lock lock = new ExclusiveLock();
        Transaction ta1 = db.getServer().getTransactionManager().newTransaction(owner);
        assertFalse(lock.tryAcquire(ta1, Lock.LEVEL_WRITE) == Lock.NOT_ACQUIRED);
        assertFalse(lock.tryAcquire(ta1, Lock.LEVEL_READ) == Lock.NOT_ACQUIRED);
    }

    class TransactionThread extends Thread {

        LocalDatabase db;

        Transaction ta;

        User owner;

        TransactionThread(LocalDatabase _db, User _owner) {
            this.db = _db;
            this.owner = _owner;
        }

        public void run() {
            ta = db.getServer().getTransactionManager().newTransaction(owner);
        }

        int aquire(Lock lock, int lockLevel) {
            return lock.tryAcquire(ta, lockLevel);
        }
    }

    public void testSharedLock() {
    }

    public void testMROWLock() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new LockTest("testExclusiveLock"));
        suite.addTest(new LockTest("testSharedLock"));
        return suite;
    }

    public static void main(String[] args) {
    }
}
