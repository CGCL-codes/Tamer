package gloodb.tutorials.simple;

import static org.junit.Assert.*;
import gloodb.KeyViolationException;
import gloodb.Repository;
import gloodb.tutorials.RepositoryFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CreateWithKeyViolationTest demonstrates creating duplicated persistent objects
 * (objects with same identity).
 * <p>Tutorials:<br> 
 * <a href="http://code.google.com/p/gloodb/wiki/NonTransactionalCreate">Create a persistent object without using transactions</a><br>
 * <a href="http://code.google.com/p/gloodb/wiki/TransactionalCreate">Create a persistent object using transactions</a><br>
 * </p>
 * <p>Sample code:<br> <a href="http://code.google.com/p/gloodb/source/browse/trunk/GlooDB/GlooDBTutorial/src/test/java/gloodb/tutorials/simple/CreateWithKeyViolationTest.java">CreateWithKeyViolation.java</a></p>
 */
public class CreateWithKeyViolationTest {

    private static Repository repository;

    /**
	 * Sets up the repository. Invoke the test with:
	 * <ul><li> -Dtype=Memory option for testing against an in memory repository. 
	 * <li>-Dtype=File for testing against a file based repository implementation.
	 * </ul>
	 */
    @BeforeClass
    public static void setUp() {
        repository = RepositoryFactory.createRepository("target/Tutorials/CreateWithKeyViolationTest");
        repository.store(new SimplePersistent("1").setValue("Preexistent"));
    }

    /**
	 * An object with the same identity as the new object we want to create 
	 * should already exist in the repository.
	 */
    @Before
    public void preCondition() {
        SimplePersistent persistentObject = (SimplePersistent) repository.restore("1");
        assertNotNull(persistentObject);
        assertEquals("Preexistent", persistentObject.getValue());
    }

    /**
	 * Try to create a duplicated object using the top level (null) transaction.
	 * A KeyViolationException is thrown and the object is not created.
	 */
    @Test
    public void testNonTransactionalCreate() {
        SimplePersistent persistentObject = new SimplePersistent("1");
        persistentObject.setValue("First Value");
        try {
            repository.create(persistentObject);
            fail("Can only create objects with unique identities.");
        } catch (KeyViolationException kve) {
        }
    }

    /**
	 * Try to create a duplicated object using transactions.
	 * A KeyViolationException is thrown and the object is not created.
	 */
    @Test
    public void testTransactionalCreate() {
        SimplePersistent persistentObject = new SimplePersistent("1");
        persistentObject.setValue("First Value");
        Repository tx = repository.begin();
        try {
            tx.create(persistentObject);
            fail("Can only create objects with unique identities.");
        } catch (KeyViolationException kve) {
            assertTrue(tx.canCommit());
            tx.rollback();
        } catch (RuntimeException ex) {
            tx.rollback();
            throw ex;
        }
    }

    /**
	  * Assert that the preexistent object has not been changed.
	  */
    @After
    public void postCondition() {
        SimplePersistent persistentObject = (SimplePersistent) repository.restore("1");
        assertNotNull(persistentObject);
        assertEquals("Preexistent", persistentObject.getValue());
    }

    /**
	 * Removes the preexistent object.
	 */
    @AfterClass
    public static void cleanUp() {
        repository.remove("1");
    }
}
