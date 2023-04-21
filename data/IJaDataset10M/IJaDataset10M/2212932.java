package net.andycole.xmiparser.parser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URL;
import java.io.File;
import java.util.Map;
import com.adobe.ac.pmd.files.impl.FileUtils;
import com.adobe.ac.pmd.files.IFlexFile;
import net.andycole.xmiparser.transfer.TransferClass;
import net.andycole.xmiparser.transfer.TransferAttribute;
import net.andycole.xmiparser.transfer.TransferOperation;

/**
 * Tests that the correct modifiers (static, public etc) are found for the given
 * attributes and operations in the given class.
 * @author Andrew Cole
 */
public class ModifiersTest {

    public ModifiersTest() {
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

    @Test
    public void testGetFields() {
        URL dirUrl = this.getClass().getResource("/modifiersSources");
        try {
            String dirPath = dirUrl.toURI().getPath();
            File sourceDirectory = new File(dirPath);
            Map<String, IFlexFile> files = FileUtils.computeFilesList(sourceDirectory, null, "", null);
            assertEquals("Only one file found", 1, files.values().size());
            IFlexFile[] filesArray = new IFlexFile[1];
            filesArray = files.values().toArray(filesArray);
            ASFileParser parser = new ASFileParser();
            TransferClass classInfo = parser.parse(filesArray[0].getFilePath(), filesArray[0].getPackageName());
            TransferAttribute[] attributes = classInfo.getAttributes();
            assertEquals("Five attributes found", 5, attributes.length);
            TransferOperation[] methods = classInfo.getMethods();
            assertEquals("Three methods found", 3, methods.length);
            assertFalse("", attributes[0].isStatic());
            assertTrue("", attributes[1].isStatic());
            assertFalse("", attributes[3].isStatic());
            assertTrue("", attributes[3].isConst());
            assertFalse("", attributes[4].isConst());
            assertFalse("", methods[0].isStatic());
        } catch (Exception e) {
            fail("modifiers file could not be parsed " + e);
        }
    }
}
