package configurator.task.deployment;

import static configurator.task.deployment.ArchivePath.*;
import junit.framework.TestCase;

/**
 * @author Justin Tomich
 */
public class ArchivePathTest extends TestCase {

    public void testJarPathNullJarName() {
        try {
            new ArchivePath().jarPath("path", false, null);
            fail("doh! should hack hucked runtime");
        } catch (RuntimeException e) {
        }
    }

    public void testJarPathNullArchive() {
        final String jar = "jarName.jar";
        String p1 = new ArchivePath().jarPath(null, false, jar);
        assertEquals(LIB_DIR + jar, p1);
        String p2 = new ArchivePath().jarPath(null, true, jar);
        assertEquals(ROOT_DIR + jar, p2);
    }

    public void testJarPathWithArchive() {
        final String jar = "jarName.jar";
        final String archive = "/foo/" + jar;
        String p1 = new ArchivePath().jarPath(archive, false, jar);
        assertEquals(archive, p1);
        String p2 = new ArchivePath().jarPath(archive, true, jar);
        assertEquals(archive, p2);
    }

    public void testLibDirPath() {
        final String dir = "/dir";
        assertEquals(dir, new ArchivePath().libDirPath(dir, false));
        assertEquals(dir, new ArchivePath().libDirPath(dir, true));
        assertEquals(LIB_DIR, new ArchivePath().libDirPath(null, false));
        assertEquals(ROOT_DIR, new ArchivePath().libDirPath(null, true));
    }

    public void testClassesDirPath() {
        final String dir = "/dir";
        assertEquals(dir, new ArchivePath().classesDirPath(dir));
        assertEquals(CLASSES_DIR, new ArchivePath().classesDirPath(null));
    }

    public void archiveDirJarPath() {
        String jar = "foo.jar";
        assertEquals(LIB_DIR + jar, new ArchivePath().archiveDirJarPath(null, false, jar));
        assertEquals(ROOT_DIR + jar, new ArchivePath().archiveDirJarPath(null, true, jar));
        String dir = "/bar/";
        assertEquals(dir + jar, new ArchivePath().archiveDirJarPath(dir, false, jar));
        assertEquals(dir + jar, new ArchivePath().archiveDirJarPath(dir, true, jar));
    }
}
