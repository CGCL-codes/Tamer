package tests.api.java.net;

import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import java.net.NetPermission;

@TestTargetClass(NetPermission.class)
public class NetPermissionTest extends junit.framework.TestCase {

    /**
     * @tests java.net.NetPermission#NetPermission(java.lang.String)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "NetPermission", args = { java.lang.String.class })
    public void test_ConstructorLjava_lang_String() {
        NetPermission n = new NetPermission("requestPasswordAuthentication");
        assertEquals("Returned incorrect name", "requestPasswordAuthentication", n.getName());
    }

    /**
     * @tests java.net.NetPermission#NetPermission(java.lang.String,
     *        java.lang.String)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "NetPermission", args = { java.lang.String.class, java.lang.String.class })
    public void test_ConstructorLjava_lang_StringLjava_lang_String() {
        NetPermission n = new NetPermission("requestPasswordAuthentication", null);
        assertEquals("Returned incorrect name", "requestPasswordAuthentication", n.getName());
        NetPermission n1 = new NetPermission("requestPasswordAuthentication", "");
        assertEquals("", n1.getActions());
    }

    /**
     * Sets up the fixture, for example, open a network connection. This method
     * is called before a test is executed.
     */
    protected void setUp() {
    }

    /**
     * Tears down the fixture, for example, close a network connection. This
     * method is called after a test is executed.
     */
    protected void tearDown() {
    }
}
