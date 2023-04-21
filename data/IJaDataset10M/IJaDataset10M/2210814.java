package com.android.unit_tests;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests serialization of user-level classes.
 */
public class SerializationTest extends TestCase {

    static class MySerializable implements Serializable {
    }

    @SmallTest
    public void testSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(new MySerializable());
        oout.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Object o = new ObjectInputStream(bin).readObject();
        assertTrue(o instanceof MySerializable);
    }
}
