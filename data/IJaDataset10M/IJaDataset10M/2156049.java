package cauldron.vfs.impl;

import filemanager.vfs.impl.JarArchive;
import filemanager.vfs.PermissionIfc;
import filemanager.vfs.ReadableFileIfc;
import java.io.File;
import org.apache.commons.vfs2.FileObject;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author sahaqiel
 */
public class JarArchiveTest {

    private final JarArchive jarArk;

    public JarArchiveTest() {
        jarArk = new JarArchive("jar:///home/sahaqiel/code/portl-svn_new/proj/Cauldron/cauldron.jar");
    }

    /**
   * Test of addFileListener method, of class JarArchive.
   */
    @Test
    public void testAddFileListener() {
    }

    /**
   * Test of exists method, of class JarArchive.
   */
    @Test
    public void testExists() {
        assertTrue("exists()", jarArk.exists());
    }

    /**
   * Test of getBackingObject method, of class JarArchive.
   */
    @Test
    public void testGetBackingObject() {
        Object backingObject = jarArk.getBackingObject();
        assertNotNull("getBackingObject()", backingObject);
        boolean found = false;
        if (backingObject instanceof File) {
            found = true;
        } else if (backingObject instanceof FileObject) {
            found = true;
        }
        assertTrue(found);
    }

    /**
   * Test of getLastModified method, of class JarArchive.
   */
    @Test
    public void testGetLastModified() {
    }

    /**
   * Test of getMimeType method, of class JarArchive.
   */
    @Test
    public void testGetMimeType() {
    }

    /**
   * Test of getName method, of class JarArchive.
   */
    @Test
    public void testGetName() {
        assertNotNull("getName()", jarArk.getName());
    }

    /**
   * Test of getParent method, of class JarArchive.
   */
    @Test
    public void testGetParent() {
        ReadableFileIfc parent = jarArk.getParent();
        assertNotNull("getParent()", parent);
    }

    /**
   * Test of getPermissions method, of class JarArchive.
   */
    @Test
    public void testGetPermissions() {
        PermissionIfc perm = jarArk.getPermissions();
        assertNotNull("getPermissions()", perm);
    }

    /**
   * Test of getPrefix method, of class JarArchive.
   */
    @Test
    public void testGetPrefix() {
        assertSame("getPrefix()", "jar://", jarArk.getPrefix());
    }

    /**
   * Test of getURI method, of class JarArchive.
   */
    @Test
    public void testGetURI() {
        String exp = "jar:file:///home/sahaqiel/BatchFront.jar!/";
        out("getURI() " + jarArk.getURI());
    }

    /**
   * Test of getType method, of class JarArchive.
   */
    @Test
    public void testGetType() {
        assertNotNull("getType()", jarArk.getType());
    }

    /**
   * Test of isHidden method, of class JarArchive.
   */
    @Test
    public void testIsHidden() {
        assertFalse("isHidden()", jarArk.isHidden());
    }

    /**
   * Test of removeListeners method, of class JarArchive.
   */
    @Test
    public void testRemoveListeners() {
    }

    /**
   * Test of compareTo method, of class JarArchive.
   */
    @Test
    public void testCompareTo() {
    }

    public void out(String msg) {
        System.out.println(getClass() + ": " + msg);
    }
}
