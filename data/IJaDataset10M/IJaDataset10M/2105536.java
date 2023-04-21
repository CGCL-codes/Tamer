package org.bhf.security.common;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.security.auth.Subject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Test the <code>Role</code> class.
 */
public class TestRole extends TestCase {

    public TestRole(String testName) {
        super(testName);
    }

    /**
     * Verifies that <code>Role</code>s with the same name compare as equal and have the same
     * hash code.
     * @throws Exception Error
     */
    public void testEquality() throws Exception {
        assertTrue("Equality 1", new Role("foobar").equals(new Role("foobar")));
        assertFalse("Equality 2", new Role("foobar").equals(new Role("foobar2")));
        assertFalse("Equality 3", new Role("foo2bar").equals(new Role("foobar")));
        assertFalse("Equality 4", new Role("foobar").equals(null));
        assertTrue("Hash code 1", new Role("foobar").hashCode() == new Role("foobar").hashCode());
    }

    /**
     * This test serializes a <code>Role</code> and verifies that the unserialized version is equal to the
     * original.
     * @throws Exception Error
     */
    public void testSerialization() throws Exception {
        Role x = new Role("foobar");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(x);
        out.close();
        Role y = (Role) new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();
        assertTrue("Serialization equiality", x.equals(y));
    }

    /**
     * This test creates a SUbject with a <code>Role</code> as a public credential and ensures that the
     * <code>Role</code> can be retrieved and is equal to the original.
     * @throws Exception Error
     */
    public void testWithSubject() throws Exception {
        Subject s = new Subject();
        Role x = new Role("foobar");
        s.getPrincipals().add(x);
        Role y = Role.getRoles(s)[0];
        assertTrue("Serialization equiality", x.equals(y));
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(new TestSuite(TestRole.class));
    }
}
