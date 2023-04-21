package org.apache.harmony.logging.tests.java.util.logging;

import junit.framework.TestCase;
import org.apache.harmony.logging.internal.nls.Messages;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.ErrorManager;
import dalvik.annotation.AndroidOnly;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(Messages.class)
public class MessagesTest extends TestCase {

    private Messages m = null;

    public void setUp() throws Exception {
        super.setUp();
        m = new Messages();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Just check signature, cannot make use of mock, method depend on luni", method = "getString", args = { java.lang.String.class })
    @AndroidOnly("harmony specific")
    public void testGetString_String() {
        m.getString(new String());
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Juste check signature, cannot make use of mock, depend on luni", method = "getString", args = { java.lang.String.class, java.lang.Object.class })
    @AndroidOnly("harmony specific")
    public void testGetString_StringObject() {
        m.getString(new String(), new Object());
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Juste check signature, cannot make use of mock, depend on luni", method = "getString", args = { java.lang.String.class, int.class })
    @AndroidOnly("harmony specific")
    public void testGetString_StringInt() {
        m.getString(new String(), 0);
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Juste check signature, cannot make use of mock, depend on luni", method = "getString", args = { java.lang.String.class, char.class })
    @AndroidOnly("harmony specific")
    public void testGetString_StringChar() {
        m.getString(new String(), 'a');
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Juste check signature, cannot make use of mock, depend on luni", method = "getString", args = { java.lang.String.class, java.lang.Object.class, java.lang.Object.class })
    @AndroidOnly("harmony specific")
    public void testGetString_StringObjectObject() {
        m.getString(new String(), new Object(), new Object());
    }

    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Juste check signature, cannot make use of mock, depend on luni", method = "getString", args = { java.lang.String.class, java.lang.Object[].class })
    @AndroidOnly("harmony specific")
    public void testGetString_StringObjectArray() {
        m.getString(new String(), new Object[1]);
    }
}
