package org.mili.core.io;

import java.io.*;
import org.apache.commons.io.*;
import org.junit.*;
import org.mili.test.*;
import static org.junit.Assert.*;

/**
 * @author Michael Lieshoff
 */
public class FileVersionFunctionTest {

    private static final File DIR = TestUtils.getTmpFolder(FileVersionFunctionTest.class);

    private static final File FILE_1 = new File(DIR, "test-1.0.0.jar");

    private static final File FILE_2 = new File(DIR, "my-1.0.txt");

    private FileVersionFunction function = null;

    @BeforeClass
    public static void beforeClass() throws Exception {
        if (DIR.exists()) {
            FileUtils.deleteDirectory(DIR);
        }
        DIR.mkdirs();
        FileUtils.writeStringToFile(FILE_1, "a");
        FileUtils.writeStringToFile(FILE_2, "b");
    }

    @Before
    public void setUp() throws Exception {
        this.function = new FileVersionFunction();
    }

    @Test
    public void shouldConstruct() {
        new FileVersionFunction();
    }

    @Test
    public void shouldEvaluateFile1() {
        FileInfo info = this.function.evaluate(FILE_1);
        assertEquals("jar", info.getExtension());
        assertEquals(FILE_1, info.getFile());
        assertEquals("test", info.getName());
        assertEquals("1.0.0", info.getVersion());
    }

    @Test
    public void shouldEvaluateFile2() {
        FileInfo info = this.function.evaluate(FILE_2);
        assertEquals("txt", info.getExtension());
        assertEquals(FILE_2, info.getFile());
        assertEquals("my", info.getName());
        assertEquals("1.0", info.getVersion());
    }
}
