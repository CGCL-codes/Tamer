package ro.codemart.installer.utils.file;

import junit.framework.TestCase;
import ro.codemart.commons.file.FileUtils;
import ro.codemart.installer.OperationTestHelper;
import ro.codemart.installer.core.utils.file.JarOperation;
import ro.codemart.installer.core.utils.file.UnzipOperation;
import ro.codemart.installer.core.operation.CommandOperation;
import ro.codemart.installer.core.InstallerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Test the Jar/Unjar operations.
 * It takes one jar, explodes it, then creates a cloned jar from the exploded jar dir.
 * The original manifest is not passed in the constructor of the JarOperation
 * todo-bf: If test fails with NPE, make sure you add the .jar to the resources file list
 */
public class JarOperationNoManifestTest extends TestCase {

    protected File explodedJarDir;

    protected static final String ORIG_JAR_NAME = "test.jar";

    protected static final String COPY_JAR_NAME = "copy-test.jar";

    protected File original;

    protected File cloned;

    protected JarFile originalJar;

    protected JarFile clonedJar;

    protected static final String RESOURCES_PATH = "/resources/jaroperation/test";

    @Override
    protected void setUp() throws Exception {
        OperationTestHelper.initOperationContext();
        URL folderURL = getClass().getResource(RESOURCES_PATH);
        File folder = new File(folderURL.toURI());
        explodedJarDir = new File(folder, "explodedJar");
        assertTrue("The exploded jar directory" + explodedJarDir.getAbsolutePath() + " should not exist", !explodedJarDir.exists());
        URL jarURL = getClass().getResource(RESOURCES_PATH + File.separatorChar + ORIG_JAR_NAME);
        original = new File(jarURL.toURI());
        assertTrue("The original jar " + ORIG_JAR_NAME + " should exist", original.exists());
        originalJar = new JarFile(original);
        cloned = new File(folder, COPY_JAR_NAME);
        assertTrue("The cloned jar " + COPY_JAR_NAME + " should not exist", !cloned.exists());
        UnzipOperation unjarOp = new UnzipOperation(original, explodedJarDir);
        unjarOp.execute();
        assertTrue("The exploded jar directory" + explodedJarDir.getAbsolutePath() + " should be created", explodedJarDir.exists());
        JarOperation jarOp = new JarOperation(explodedJarDir, cloned, null);
        jarOp.execute();
        clonedJar = new JarFile(cloned);
        assertTrue("The cloned jar should be created", cloned.exists());
    }

    @Override
    protected void tearDown() throws Exception {
        FileUtils.deleteDir(explodedJarDir);
        clonedJar.close();
        cloned.delete();
    }

    /**
     * Test if the original and the cloned jar have same nr of entries
     *
     * @throws Exception if something goes wrong
     */
    public void testNrOfEntries() throws Exception {
        assertEquals("Should have same size:", originalJar.size(), clonedJar.size());
    }

    /**
     * Test if the original and the cloned manifest are the same
     *
     * @throws Exception if something goes wrong
     */
    public void testManifests() throws Exception {
        Manifest mf1 = originalJar.getManifest();
        Manifest mf2 = originalJar.getManifest();
        assertEquals("The manifests should be equal", mf1, mf2);
    }

    /**
     * Test that the total sum of jar entries for the 2 jars is the same
     *
     * @throws Exception if something goes wrong
     */
    public void testSizeForJarEntries() throws Exception {
        assertEquals("The total sum for jar entries of the 2 jars should be equal!", getJarEntriesSize(originalJar), getJarEntriesSize(clonedJar));
    }

    /**
     * Compare the contents of those 2 jars
     *
     * @throws Exception if something goes wrong
     */
    public void testByteContents() throws Exception {
        compareJarStreams(original, cloned);
    }

    /**
     * Test each of the 2 corresponding jar entries for the jars
     *
     * @throws Exception if something goes wrong
     */
    public void testJarEntries() throws Exception {
        testJarFiles(originalJar, clonedJar);
    }

    /**
     * Test the execution of the original and cloned jar if the are executable
     *
     * @throws Exception if something goes wrong
     */
    public void testExecuteJar() throws Exception {
        String command = "java -jar ";
        if (isJarExecutable(originalJar) && isJarExecutable(clonedJar)) {
            File path = new File(getClass().getResource(RESOURCES_PATH).toURI());
            CommandOperation cmd1 = new CommandOperation(command + ORIG_JAR_NAME, path, true);
            CommandOperation cmd2 = new CommandOperation(command + COPY_JAR_NAME, path, true);
            try {
                cmd1.execute();
                cmd2.execute();
            } catch (InstallerException e) {
                fail("Could not execute any of the jars. Reason: " + e.getMessage());
            }
            assertEquals("The output result should be the same:", cmd1.getOutputMessage(), cmd2.getOutputMessage());
            assertEquals("The error result should be the same:", cmd1.getErrorMessage(), cmd2.getErrorMessage());
        }
    }

    /**
     * Checks that a jar is executable
     *
     * @param jar the jar to be tested
     * @return true if the jar is executable
     * @throws IOException if any errors
     */
    private boolean isJarExecutable(JarFile jar) throws IOException {
        Manifest mf = jar.getManifest();
        return mf.getMainAttributes().getValue("Main-Class") != null;
    }

    /**
     * Return the ordered set of jar entries. NOTE: JarEntry has the "hashCode" on name, but lacks the "equals"
     *
     * @param jar the jar whose entries are to be added
     * @return the ordered set of jar entries
     */
    private Set<JarEntry> getOrderedJarEntriesForJar(JarFile jar) {
        Set<JarEntry> result = new HashSet<JarEntry>();
        for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = (JarEntry) entries.nextElement();
            result.add(entry);
        }
        return result;
    }

    private void testJarFiles(JarFile jar1, JarFile jar2) {
        Set<JarEntry> set1 = getOrderedJarEntriesForJar(jar1);
        Set<JarEntry> set2 = getOrderedJarEntriesForJar(jar2);
        Iterator it1 = set1.iterator();
        Iterator it2 = set2.iterator();
        try {
            while (it1.hasNext()) {
                JarEntry e1 = (JarEntry) it1.next();
                JarEntry e2 = (JarEntry) it2.next();
                testJarEntries(e1, e2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("The jar files do not have the same nr of entries!");
        }
    }

    /**
     * Test that 2 jar entries are the "same"
     *
     * @param jarEntry1 the first jar entry
     * @param jarEntry2 the second jar entry
     */
    private void testJarEntries(JarEntry jarEntry1, JarEntry jarEntry2) {
        assertEquals("Jar Entries should have same name: ", jarEntry1.getName(), jarEntry2.getName());
        assertEquals("Jar Entries should have same size:", jarEntry1.getSize(), jarEntry2.getSize());
        assertEquals("Jar Entries should have same CRC:", jarEntry1.getCrc(), jarEntry2.getCrc());
        assertEquals("Jar Entries should have same comment:", jarEntry1.getComment(), jarEntry2.getComment());
        assertEquals("Jar Entries should have same time:", jarEntry1.getTime(), jarEntry2.getTime());
        assertEqualByteArrays(jarEntry1.getExtra(), jarEntry2.getExtra());
    }

    /**
     * Returns the size for the jar
     *
     * @param jar the file denoting a jar
     * @return the size of a jar
     */
    private long getJarEntriesSize(JarFile jar) throws Exception {
        long size = 0L;
        for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
            size += ((JarEntry) e.nextElement()).getSize();
        }
        jar.close();
        return size;
    }

    /**
     * Compares the streams for the 2 jars
     *
     * @param jar1 the original jar
     * @param jar2 the cloned jar
     * @throws Exception if something goes wrong
     */
    private void compareJarStreams(File jar1, File jar2) throws Exception {
        int kilo = 1024;
        byte[] array1 = new byte[kilo];
        byte[] array2 = new byte[kilo];
        InputStream fis1 = new FileInputStream(jar1);
        InputStream fis2 = new FileInputStream(jar2);
        int length;
        try {
            while ((length = fis1.read(array1)) != -1) {
                fis2.read(array2, 0, length);
                assertEqualByteArrays(array1, array2);
            }
        } finally {
            fis1.close();
            fis2.close();
        }
    }

    public void assertEqualByteArrays(byte[] array1, byte[] array2) {
        if ((array1 == null && array2 != null) || (array1 != null && array2 == null)) {
            fail("One of the arrays is null");
        }
        if (array1 != null && array2 != null) {
            for (int i = 0; i < array1.length; i++) {
                assertEquals("The " + i + "-th byte pairs should be equal", array1[i], array2[i]);
            }
        }
    }
}
