package net.sourceforge.appgen.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import net.sourceforge.appgen.util.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Byeongkil Woo
 */
public class FileUtilsTest {

    public static final String FILE_NAME = "FileUtilsTestFile.txt";

    @Test
    public void testLn() {
        assertEquals(System.getProperty("line.separator", "\n"), FileUtils.ln());
    }

    @Test
    public void testCopyFileFile() {
        try {
            File in = new File(ClassLoader.getSystemResource("net/sourceforge/appgen/util/" + FILE_NAME).toURI());
            File out = new File(System.getProperty("java.io.tmpdir"), FILE_NAME);
            FileUtils.copy(in, out);
            assertTrue(out.exists());
            assertTrue(in.length() == out.length());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCopyInputStreamOutputStream() {
        try {
            File in = new File(ClassLoader.getSystemResource("net/sourceforge/appgen/util/" + FILE_NAME).toURI());
            File out = new File(System.getProperty("java.io.tmpdir"), FILE_NAME);
            FileUtils.copy(new FileInputStream(in), new FileOutputStream(out));
            assertTrue(out.exists());
            assertTrue(in.length() == out.length());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
