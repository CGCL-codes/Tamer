package org.apache.tools.ant.taskdefs;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.BuildFileTest;

/**
 * Tests CVSLogin task.
 *
 */
public class CVSPassTest extends BuildFileTest {

    private final String EOL = System.getProperty("line.separator");

    private static final String JAKARTA_URL = ":pserver:anoncvs@jakarta.apache.org:/home/cvspublic Ay=0=h<Z";

    private static final String XML_URL = ":pserver:anoncvs@xml.apache.org:/home/cvspublic Ay=0=h<Z";

    private static final String TIGRIS_URL = ":pserver:guest@cvs.tigris.org:/cvs AIbdZ,";

    public CVSPassTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/cvspass.xml");
    }

    public void testNoCVSRoot() {
        try {
            executeTarget("test1");
            fail("BuildException not thrown");
        } catch (BuildException e) {
            assertEquals("cvsroot is required", e.getMessage());
        }
    }

    public void testNoPassword() {
        try {
            executeTarget("test2");
            fail("BuildException not thrown");
        } catch (BuildException e) {
            assertEquals("password is required", e.getMessage());
        }
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testPassFile() throws Exception {
        executeTarget("test3");
        File f = new File(getProjectDir(), "testpassfile.tmp");
        assertTrue("Passfile " + f + " not created", f.exists());
        assertEquals(JAKARTA_URL + EOL, readFile(f));
    }

    public void testPassFileDuplicateEntry() throws Exception {
        executeTarget("test4");
        File f = new File(getProjectDir(), "testpassfile.tmp");
        assertTrue("Passfile " + f + " not created", f.exists());
        assertEquals(JAKARTA_URL + EOL + TIGRIS_URL + EOL, readFile(f));
    }

    public void testPassFileMultipleEntry() throws Exception {
        executeTarget("test5");
        File f = new File(getProjectDir(), "testpassfile.tmp");
        assertTrue("Passfile " + f + " not created", f.exists());
        assertEquals(JAKARTA_URL + EOL + XML_URL + EOL + TIGRIS_URL + EOL, readFile(f));
    }

    private String readFile(File f) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            StringBuffer buf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line + EOL);
            }
            return buf.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
