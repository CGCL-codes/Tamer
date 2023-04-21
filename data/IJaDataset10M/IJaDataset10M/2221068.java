package j8086emu.model.progcode;

import j8086emu.model.progcode.files.Saver;
import org.junit.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author vlad
 */
public class SaverTest {

    public SaverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of saveInside method, of class Saver.
     */
    @Test
    public void testSave() {
        System.out.println("saveTest");
        String selectedFile = new java.io.File("").getAbsolutePath() + "/test_save.asm";
        String programInString = "SEGMENT .text\nGLOBAL _start\n_start:\nMOV ax,dx";
        Saver saver = new Saver();
        saver.save(selectedFile, programInString);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("test_save.asm"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaverTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String str1 = null;
            if (bufferedReader != null) {
                str1 = bufferedReader.readLine();
            }
            String str2 = null;
            if (bufferedReader != null) {
                str2 = bufferedReader.readLine();
            }
            assertEquals(str1 + " " + str2, "SEGMENT .text GLOBAL _start");
        } catch (IOException ex) {
            Logger.getLogger(SaverTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
