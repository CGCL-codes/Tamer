package test.de.sicari.kernel;

import java.io.FilePermission;
import java.security.Permission;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import de.sicari.kernel.GenericPermissionCollection;
import de.sicari.kernel.ServiceInvocationPermission;

/**
 * This is the <i>JUnit</i> test for the
 * {@link GenericPermissionCollection} class.
 *
 * @author Matthias Pressfreund
 * @version $Id: GenericPermissionCollectionTest.java 204 2007-07-11 19:26:55Z jpeters $
 */
public class GenericPermissionCollectionTest {

    protected GenericPermissionCollection gpc1_;

    protected GenericPermissionCollection gpc2_;

    protected GenericPermissionCollection gpc3_;

    @Before
    public void setUp() {
        gpc1_ = new GenericPermissionCollection();
        gpc2_ = new GenericPermissionCollection(ServiceInvocationPermission.class);
        Set<Class<? extends Permission>> perms = new HashSet<Class<? extends Permission>>();
        perms.add(ServiceInvocationPermission.class);
        perms.add(FilePermission.class);
        gpc3_ = new GenericPermissionCollection(perms);
    }

    @After
    public void tearDown() {
        gpc1_ = null;
        gpc2_ = null;
        gpc3_ = null;
    }

    @Test
    public void testAddPermission() {
        try {
            gpc1_.add(new ServiceInvocationPermission("/test", "*"));
            gpc1_.add(new RuntimePermission("test"));
            gpc1_.add(new FilePermission("/", "read,write"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc2_.add(new ServiceInvocationPermission("/test", "*"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc2_.add(new RuntimePermission("test"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc2_.add(new FilePermission("/", "read,write"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc3_.add(new ServiceInvocationPermission("/test", "*"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc3_.add(new RuntimePermission("test"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc3_.add(new FilePermission("/", "read,write"));
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testImpliesPermission() {
        gpc1_.add(new ServiceInvocationPermission("/test", "*"));
        gpc1_.add(new RuntimePermission("test"));
        gpc1_.add(new FilePermission("/", "read,write"));
        assertTrue(gpc1_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc1_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertTrue(gpc1_.implies(new FilePermission("/", "read")));
        assertFalse(gpc1_.implies(new FilePermission("/test", "read")));
        assertTrue(gpc1_.implies(new RuntimePermission("test")));
        assertFalse(gpc1_.implies(new RuntimePermission("test1")));
        gpc2_.add(new ServiceInvocationPermission("/test", "*"));
        assertTrue(gpc2_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc2_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertFalse(gpc2_.implies(new FilePermission("/", "read")));
        gpc3_.add(new ServiceInvocationPermission("/test", "*"));
        gpc3_.add(new FilePermission("/", "read,write"));
        assertTrue(gpc3_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc3_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertTrue(gpc3_.implies(new FilePermission("/", "read")));
        assertFalse(gpc3_.implies(new FilePermission("/test", "read")));
        assertFalse(gpc3_.implies(new RuntimePermission("test")));
    }

    @Test
    public void testElements() {
        Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(new ServiceInvocationPermission("/test", "*"));
        permissions.add(new RuntimePermission("test"));
        permissions.add(new FilePermission("/", "read,write"));
        for (Iterator<Permission> itr = permissions.iterator(); itr.hasNext(); ) {
            gpc1_.add(itr.next());
        }
        Enumeration<Permission> elements = gpc1_.elements();
        try {
            while (elements.hasMoreElements()) {
                permissions.remove(elements.nextElement());
            }
        } catch (Exception e) {
            fail();
        }
        assertTrue(permissions.isEmpty());
    }
}
