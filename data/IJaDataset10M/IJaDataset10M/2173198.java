package org.objectstyle.cayenne.unit;

import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.conf.Configuration;
import org.objectstyle.cayenne.util.Util;

/**
 * Superclass of test cases requiring multiple DataContexts with 
 * the same parent DataDomain.
 * 
 * @author Andrei Adamchik
 */
public abstract class MultiContextTestCase extends CayenneTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        deleteTestData();
    }

    protected void fixSharedConfiguration() {
        Configuration config = Configuration.getSharedConfiguration();
        if (getDomain() != config.getDomain()) {
            if (config.getDomain() != null) {
                config.removeDomain(config.getDomain().getName());
            }
            config.addDomain(getDomain());
        }
    }

    /**
     * Helper method to create a new DataContext with the ObjectStore
     * state being the mirror of the given context. This is done by
     * serializing/deserializing the DataContext.
     */
    protected DataContext mirrorDataContext(DataContext context) throws Exception {
        fixSharedConfiguration();
        DataContext mirror = (DataContext) Util.cloneViaSerialization(context);
        assertNotSame(context, mirror);
        assertNotSame(context.getObjectStore(), mirror.getObjectStore());
        if (context.isUsingSharedSnapshotCache()) {
            assertSame(context.getObjectStore().getDataRowCache(), mirror.getObjectStore().getDataRowCache());
        } else {
            assertNotSame(context.getObjectStore().getDataRowCache(), mirror.getObjectStore().getDataRowCache());
        }
        return mirror;
    }
}
