package org.jfree.data.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.data.KeyedObject;

/**
 * Tests for the {@link KeyedObject} class.
 */
public class KeyedObjectTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(KeyedObjectTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public KeyedObjectTests(String name) {
        super(name);
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    public void testEquals() {
        KeyedObject ko1 = new KeyedObject("Test", "Object");
        KeyedObject ko2 = new KeyedObject("Test", "Object");
        assertTrue(ko1.equals(ko2));
        assertTrue(ko2.equals(ko1));
        ko1 = new KeyedObject("Test 1", "Object");
        ko2 = new KeyedObject("Test 2", "Object");
        assertFalse(ko1.equals(ko2));
        ko1 = new KeyedObject("Test", "Object 1");
        ko2 = new KeyedObject("Test", "Object 2");
        assertFalse(ko1.equals(ko2));
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        KeyedObject ko1 = new KeyedObject("Test", "Object");
        KeyedObject ko2 = null;
        try {
            ko2 = (KeyedObject) ko1.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Failed to clone.");
        }
        assertTrue(ko1 != ko2);
        assertTrue(ko1.getClass() == ko2.getClass());
        assertTrue(ko1.equals(ko2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        KeyedObject ko1 = new KeyedObject("Test", "Object");
        KeyedObject ko2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(ko1);
            out.close();
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            ko2 = (KeyedObject) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(ko1, ko2);
    }
}
