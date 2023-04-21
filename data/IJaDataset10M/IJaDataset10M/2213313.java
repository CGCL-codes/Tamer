package gloodb.tutorials.simple;

import static org.junit.Assert.*;
import gloodb.CannotCompleteTxException;
import gloodb.Repository;
import gloodb.tutorials.RepositoryFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * SimpleTransactionTest demonstrates Repository control behaviour.
 * <p>
 * Sample code:<br>
 * <a href="http://code.google.com/p/gloodb/source/browse/trunk/GlooDB/GlooDBTutorial/src/test/java/gloodb/tutorials/simple/SimpleTransactionTest.java"> SimpleTransactionTest.java</a> 
 * </p>
 */
public class SimpleTransactionTest {

    private static Repository repository;

    /**
	 * Sets up the repository. Invoke the test with:
	 * <ul>
	 * <li>-Dtype=Memory option for testing against an in memory repository.
	 * <li>-Dtype=File for testing against a file based repository
	 * implementation.
	 * </ul>
	 */
    @BeforeClass
    public static void setUp() {
        repository = RepositoryFactory.createRepository("target/Tutorials/SimpleTransactionTest");
    }

    /**
	 * Begin / Commit behaviour. Once commited, the Repository is marked
	 * completed.
	 */
    @Test
    public void testCommit() {
        Repository tx = repository.begin();
        assertTrue(tx.canCommit());
        assertTrue(tx.canComplete());
        tx.commit();
        assertFalse(tx.canCommit());
        assertFalse(tx.canComplete());
    }

    /**
	 * Begin / Rollback behaviour. Once rollbacked, the Repository is marked
	 * completed.
	 */
    @Test
    public void testRollback() {
        Repository tx = repository.begin();
        assertTrue(tx.canCommit());
        assertTrue(tx.canComplete());
        tx.rollback();
        assertFalse(tx.canCommit());
        assertFalse(tx.canComplete());
    }

    /**
	 * Mark for rollback behaviour. Transactions marked for rollback only cannot
	 * commit but only rollback.
	 */
    @Test
    public void testMarkForRollback() {
        Repository tx = repository.begin();
        assertTrue(tx.canCommit());
        assertTrue(tx.canComplete());
        tx.markForRollback();
        assertFalse(tx.canCommit());
        assertTrue(tx.canComplete());
        try {
            tx.commit();
            fail("Transactions marked for rollback cannot commit");
        } catch (CannotCompleteTxException ccte) {
        }
        tx.rollback();
    }
}
