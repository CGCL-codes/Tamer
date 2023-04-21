package com.threerings.jpkg;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.tar.TarInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DestRootWalkerTest extends TestTarFile {

    @Test
    public void testWalkDestRoot() throws Exception {
        final File destroot = dummyDestroot();
        final PackageTarFile tar = new PackageTarFile(TestData.TEMP_DIR, new PermissionsMap());
        try {
            final DestrootWalker walker = new DestrootWalker(destroot, tar);
            walker.walk();
            tar.close();
            TarInputStream input = null;
            try {
                input = getTarInput(tar);
                assertEquals(ROOT_DIR, input.getNextEntry().getName());
                assertEquals(ROOT_DIR_FILE, input.getNextEntry().getName());
                assertEquals(ROOT_FILE, input.getNextEntry().getName());
                assertNull(input.getNextEntry());
            } finally {
                IOUtils.closeQuietly(input);
            }
        } finally {
            tar.delete();
            FileUtils.deleteDirectory(destroot);
        }
    }

    /**
     * Dummy up a destroot instead of using a tree full of .svn entries.
     */
    private File dummyDestroot() throws Exception {
        final File destroot = File.createTempFile("jpkgtest", "destroot");
        destroot.delete();
        destroot.mkdir();
        final String destrootPath = destroot.getAbsolutePath();
        FileUtils.touch(new File(destrootPath, ROOT_FILE));
        final File rootDir = new File(destrootPath, ROOT_DIR);
        rootDir.mkdir();
        FileUtils.touch(new File(destrootPath, ROOT_DIR_FILE));
        return destroot;
    }

    /** Test files and directories. */
    public static final String ROOT_FILE = "file.txt";

    public static final String ROOT_DIR = "directory/";

    public static final String ROOT_DIR_FILE = ROOT_DIR + "file.txt";
}
