package bman.tools;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JFileTest {

    String file1 = "File1.txt";

    String content1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Test
    public void testCreateFile() {
        try {
            JFile.write(file1, content1);
        } catch (Exception e) {
            System.out.println("An exception occured in testCreateFile.");
            e.printStackTrace();
            fail();
        }
    }

    /**
	 *  Important note: Test for Create File1 needs to be run before this test.
	 */
    @Test
    public void testContentsOfFile1() {
        try {
            testCreateFile();
            String result = new String(JFile.read(file1));
            assertEquals("JFile.read() is not reading the values correctly.", content1, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An exception occured in testContentsOfFile1().");
        }
    }

    @Test(expected = java.io.FileNotFoundException.class)
    public void testReadingFileThatDoesNotExists() throws Exception {
        JFile.read("nonexistenfile_12345642345");
    }

    @After
    public void tearDown() {
        File f = new File(file1);
        f.delete();
    }
}
