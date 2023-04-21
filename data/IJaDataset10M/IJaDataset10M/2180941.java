package document.softwareDocument.structure;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ACER 3614
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ document.softwareDocument.structure.NodeDocumentTest.class, document.softwareDocument.structure.AttributDocumentTest.class, document.softwareDocument.structure.ValueDocumentTest.class, document.softwareDocument.structure.DocumentRepresentationTest.class })
public class StructureSuite {

    /**
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
}
