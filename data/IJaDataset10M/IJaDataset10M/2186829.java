package org.apache.harmony.security.tests.java.security.serialization;

import org.apache.harmony.security.tests.support.MyPermission;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Serialization tests for <code>Permission</code>
 */
public class PermissionTest extends SerializationTest {

    /**
     * @see com.intel.drl.test.SerializationTest#getData()
     */
    protected Object[] getData() {
        return new Object[] { new MyPermission(null), new MyPermission("IYF&*%^sd 43") };
    }
}
