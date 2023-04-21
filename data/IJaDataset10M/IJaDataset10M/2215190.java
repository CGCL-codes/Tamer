package documenttypesupport;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kasun
 */
public class AnyToTextConverterTest {

    public AnyToTextConverterTest() {
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
     * Test of convertFilesInFolder method, of class AnyToTextConverter.
     */
    @Test
    public void testConvertFilesInFolder() {
        System.out.println("convertFilesInFolder");
        String folderPath = "";
        AnyToTextConverter instance = null;
        instance.convertFilesInFolder(folderPath);
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertSingleFile method, of class AnyToTextConverter.
     */
    @Test
    public void testConvertSingleFile() {
        System.out.println("convertSingleFile");
        String fileName = "";
        AnyToTextConverter instance = null;
        instance.convertSingleFile(fileName);
        fail("The test case is a prototype.");
    }
}
