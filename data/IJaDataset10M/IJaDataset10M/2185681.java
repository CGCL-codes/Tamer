package task;

import data.Options;
import data.OptionsTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Brian
 */
public class OptionsWriteTaskTest {

    private Options options;

    public OptionsWriteTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        options = new Options();
        options.setExtensions(".mp3; .avi");
        options.setMusicDirectory(new File("C:/"));
        options.setiTunesXMLFile(OptionsTest.TEST_XML_FILE);
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testWrite() {
        System.out.println("doInBackground");
        File optionsFile = new File(Options.OPTIONS_FILE_NAME);
        if (optionsFile.exists()) {
            System.out.println(optionsFile.delete());
        }
        OptionsWriteTask owt = new OptionsWriteTask(options);
        owt.doInBackground();
        Options o = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(optionsFile));
            o = (Options) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        assertTrue(optionsFile.length() > 0);
        assertArrayEquals(options.getExtensions(), o.getExtensions());
        assertEquals(options.getMusicDirectory(), o.getMusicDirectory());
        assertEquals(options.getiTunesXMLFile(), o.getiTunesXMLFile());
        assertEquals(options.getiTunesXMLMusicDir(), o.getiTunesXMLMusicDir());
    }
}
