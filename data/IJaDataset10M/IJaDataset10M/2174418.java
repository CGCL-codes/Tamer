package org.argouml.model.mdr;

import junit.framework.TestCase;
import org.apache.log4j.PropertyConfigurator;
import org.argouml.model.UmlException;

/**
 * Base test case for MDR Model tests.
 */
public abstract class AbstractMDRModelImplementationTestCase extends TestCase {

    /**
     * The ModelImplementation.<p>
     *
     * The reason for not having this as a member variable that is created by
     * {@link #setUp()} is that the MDR is the initialized several times and
     * the current implementation fails on the second initialization.
     */
    protected static MDRModelImplementation modelImplementation;

    /**
     * Initialization state.
     */
    private boolean initialized = false;

    protected void init() {
        try {
            PropertyConfigurator.configure(getClass().getClassLoader().getResource("org/argouml/resource/info_console.lcf"));
            System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName", "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
            System.setProperty("org.netbeans.lib.jmi.Logger", "0");
            System.setProperty("org.netbeans.mdr.Logger", "0");
            modelImplementation = new MDRModelImplementation();
            initialized = true;
        } catch (UmlException e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (!initialized) init();
    }
}
