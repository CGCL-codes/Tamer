package gloodb.tutorials.simple;

import static org.junit.Assert.*;
import gloodb.Repository;
import gloodb.tutorials.RepositoryFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * SimpleRemoveTest demonstrates removing a persistent object.
 * <p>
 * Tutorials:<br>
 * <a href="http://code.google.com/p/gloodb/wiki/TransactionalRemove">Remove a
 * persistent object using transactions</a><br>
 * <a href="http://code.google.com/p/gloodb/wiki/NonTransactionalRemove">Remove a
 * persistent object without using transactions</a><br>
 * </p>
 * <p>
 * Sample code:<br>
 * <a href="http://code.google.com/p/gloodb/source/browse/trunk/GlooDB/GlooDBTutorial/src/test/java/gloodb/tutorials/simple/SimpleRemoveTest.java"
 * >SimpleRemoveTest.java</a>
 * </p>
 * 
 */
public class SimpleRemoveTest {

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
        repository = RepositoryFactory.createRepository("target/Tutorials/SimpleUpdateTest");
    }

    /**
	 * An object with the same identity as the new object we want to remove
	 * should already exist in the repository.
	 */
    @Before
    public void preCondition() {
        SimplePersistent persistentObject = repository.store(new SimplePersistent("1").setValue("Preexistent"));
        assertNotNull(persistentObject);
        assertEquals("Preexistent", persistentObject.getValue());
    }

    /**
	 * Removes a persistent object using the top level (null) transaction.
	 */
    @Test
    public void testNonTransactionalRemove() {
        SimplePersistent removedObject = (SimplePersistent) repository.remove("1");
        assertNotNull(removedObject);
        assertEquals("Preexistent", removedObject.getValue());
        assertFalse(repository.contains("1"));
    }

    /**
	 * Removes a persistent object using transactions.
	 */
    @Test
    public void testTransactionalRemove() {
        Repository tx = repository.begin();
        try {
            SimplePersistent removedObject = (SimplePersistent) tx.remove("1");
            assertNotNull(removedObject);
            assertEquals("Preexistent", removedObject.getValue());
            assertFalse(tx.contains("1"));
            tx.commit();
        } catch (RuntimeException ex) {
            tx.rollback();
            throw ex;
        }
    }

    /**
	 * Rollbacks a remove transaction.
	 */
    @Test
    public void testRemoveRollback() {
        Repository tx = repository.begin();
        SimplePersistent removedObject = (SimplePersistent) tx.remove("1");
        assertNotNull(removedObject);
        assertEquals("Preexistent", removedObject.getValue());
        assertFalse(tx.contains("1"));
        tx.rollback();
        assertTrue(repository.contains("1"));
    }

    @AfterClass
    public static void cleanUp() {
        repository.remove("1");
    }
}
