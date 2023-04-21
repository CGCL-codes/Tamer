package com.rhythm.commons.io;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Michael J. Lee @ Synergy Energy Holdings, LLC
 */
public class IOUtilsTest {

    private File file;

    private String fileText;

    @Before
    public void setup() {
        try {
            file = new File("/testfile.txt");
            file.createNewFile();
            fileText = "This is my test file";
        } catch (IOException ex) {
            System.out.println("FAILED ON SETUP..." + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void testSetup() {
        assertTrue(file.exists());
    }

    @Test
    public void testMultipleWriteFile() throws IOException {
        IOUtils.writeString(file, "1");
        IOUtils.writeString(file, "2");
        IOUtils.writeString(file, "3");
        System.out.println(IOUtils.readFile(file));
    }

    @Test
    public void testWriteFile() throws IOException {
        final String text = "Write this to my file";
        assertEquals("", IOUtils.readFile(file));
        IOUtils.writeString(file, text);
        assertEquals(text, IOUtils.readFile(file));
    }

    @Test
    public void testReadFile() throws IOException {
        assertEquals("", IOUtils.readFile(file));
        IOUtils.writeString(file, fileText);
        assertEquals(fileText, IOUtils.readFile(file));
    }
}
