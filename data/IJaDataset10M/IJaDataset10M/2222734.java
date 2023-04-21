package org.jhotdraw.test.util;

import org.jhotdraw.util.SerializationStorageFormat;
import junit.framework.TestCase;

public class SerializationStorageFormatTest extends TestCase {

    private SerializationStorageFormat serializationstorageformat;

    /**
	 * Constructor SerializationStorageFormatTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public SerializationStorageFormatTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public SerializationStorageFormat createInstance() throws Exception {
        return new SerializationStorageFormat();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        serializationstorageformat = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        serializationstorageformat = null;
        super.tearDown();
    }

    public void testCreateFileDescription() throws Exception {
    }

    public void testStore() throws Exception {
    }

    public void testRestore() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
