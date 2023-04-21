package it.unibg.cs.jtvguide.test;

import it.unibg.cs.jtvguide.util.FileUtils;
import java.io.File;
import java.util.regex.Pattern;
import junit.framework.TestCase;

/**
 * A class to test fileutils
 * @author Michele Bologna, Sebastiano Rota
 *
 */
public class FileUtilsTest extends TestCase {

    public void setUp() throws Exception {
    }

    public void tearDown() throws Exception {
    }

    /**
	 * Test for count the uncommented lines in a file, either if the file exists or not
	 */
    public void testUncommentedLinesCount() {
        assertEquals(3, FileUtils.uncommentedLinesCount(new File("examples/tv_grab.conf")));
        assertEquals(0, FileUtils.uncommentedLinesCount(new File("examples/tv_grab2.conf")));
        assertEquals(-1, FileUtils.uncommentedLinesCount(new File("examples/NON_EXIST")));
    }

    /**
	 * Test for search a pattern inside a file, either if the pattern exists or not and if file exists or not
	 */
    public void testGrep() {
        assertEquals(true, FileUtils.grep(new File("examples/tv_grab.conf"), Pattern.compile("canale")));
        assertEquals(false, FileUtils.grep(new File("examples/tv_grab.conf"), Pattern.compile("NON_EXIST")));
        assertEquals(false, FileUtils.grep(new File("examples/NON_EXIST"), Pattern.compile("canale")));
    }
}
