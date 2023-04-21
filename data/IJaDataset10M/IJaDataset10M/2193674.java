package org.nakedobjects.object.defaults;

import org.nakedobjects.container.configuration.ComponentException;
import org.nakedobjects.container.configuration.ConfigurationException;
import org.nakedobjects.object.NakedObjectStore;
import org.nakedobjects.object.NakedObjectStoreAdvancedTestCase;
import org.nakedobjects.object.ObjectStoreException;
import junit.framework.TestSuite;

public class TransientObjectStoreAdvancedTest extends NakedObjectStoreAdvancedTestCase {

    public TransientObjectStoreAdvancedTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TransientObjectStoreAdvancedTest.class));
    }

    public NakedObjectStore installObjectStore() throws ObjectManagerException {
        return new TransientObjectStore();
    }

    protected void restartObjectStore() throws ObjectManagerException, Exception, ConfigurationException, ComponentException {
    }
}
