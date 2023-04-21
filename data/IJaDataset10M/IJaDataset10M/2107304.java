package org.openscience.jmol;

import com.baysmith.io.FileUtilities;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the ReaderFactory class.
 *
 * @author Bradley A. Smith (bradley@baysmith.com)
 */
public class ReaderFactoryTest extends TestCase {

    /**
   * Create a test case with given name.
   *
   * @param name this test case's name.
   */
    public ReaderFactoryTest(String name) {
        super(name);
    }

    /**
   * Returns a suite of tests contained in this test case.
   *
   * @return a suite of tests contained in this test case.
   */
    public static Test suite() {
        TestSuite suite = new TestSuite(ReaderFactoryTest.class);
        return suite;
    }

    /**
   *  Test directory for isolating testing operations.
   */
    File testDirectory;

    /**
   * Setup fixtures.
   */
    public void setUp() {
        testDirectory = new File(getClass().getName());
        FileUtilities.deleteAll(testDirectory);
        assertTrue("Unable to create test directory \"" + testDirectory.getName() + "\"", testDirectory.mkdir());
    }

    /**
   * Destroy fixtures.
   */
    public void tearDown() {
        testDirectory = null;
    }

    /**
   * Test identifying the various types of files.
   */
    public void testIdentifyingTypes() {
        try {
            File testFile = new File(testDirectory, "g98.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            FileReader input = new FileReader(testFile);
            ChemFileReader reader = ReaderFactory.createReader(input);
            if (!(reader instanceof Gaussian98Reader)) {
                fail("reader not a Gaussian98Reader");
            }
            input.close();
            testFile = new File(testDirectory, "test.cml");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof CMLReader)) {
                fail("reader not a CMLReader");
            }
            input.close();
            testFile = new File(testDirectory, "test.mol");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof MdlReader)) {
                fail("reader not a MdlReader");
            }
            input.close();
            testFile = new File(testDirectory, "test2.mol");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof MdlReader)) {
                fail("reader not a MdlReader");
            }
            input.close();
            testFile = new File(testDirectory, "abinit.in");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof ABINITReader)) {
                fail("reader not a ABINITReader");
            }
            input.close();
            testFile = new File(testDirectory, "test.pdb");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof PDBReader)) {
                fail("reader not a PDBReader");
            }
            input.close();
            testFile = new File(testDirectory, "test.xyz");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof XYZReader)) {
                fail("reader not a XYZReader");
            }
            input.close();
            testFile = new File(testDirectory, "g94.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof Gaussian94Reader)) {
                fail("reader not a Gaussian94Reader");
            }
            input.close();
            testFile = new File(testDirectory, "gamess.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof GamessReader)) {
                fail("reader not a GamessReader");
            }
            input.close();
            testFile = new File(testDirectory, "aces.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof Aces2Reader)) {
                fail("reader not a Aces2Reader");
            }
            input.close();
            testFile = new File(testDirectory, "mm1gp.in");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof GhemicalMMReader)) {
                fail("reader not a GhemicalMMReader");
            }
            input.close();
            testFile = new File(testDirectory, "adf.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof ADFReader)) {
                fail("reader not a ADFReader");
            }
            input.close();
            testFile = new File(testDirectory, "dalton.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof DaltonReader)) {
                fail("reader not a DaltonReader");
            }
            input.close();
            testFile = new File(testDirectory, "mopac7.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof Mopac7Reader)) {
                fail("reader not a Mopac7Reader");
            }
            input.close();
            testFile = new File(testDirectory, "mopac2002.out");
            FileUtilities.copyStreamToFile(getClass().getResourceAsStream("Test-" + testFile.getName()), testFile);
            input = new FileReader(testFile);
            reader = ReaderFactory.createReader(input);
            if (!(reader instanceof Mopac97Reader)) {
                fail("reader not a Mopac97Reader");
            }
            input.close();
        } catch (IOException ex) {
            fail(ex.toString());
        }
    }

    /**
   * Test invalid argument.
   */
    public void testInvalidArgument() {
        try {
            ChemFileReader reader = ReaderFactory.createReader(null);
            fail("IllegalArgmentException expected");
        } catch (IllegalArgumentException ex) {
        } catch (IOException ex) {
            fail(ex.toString());
        }
    }

    /**
   * Test empty input.
   */
    public void testEmptyInput() {
        try {
            StringReader input = new StringReader("");
            ChemFileReader reader = ReaderFactory.createReader(input);
            assertNull(reader);
        } catch (IOException ex) {
            fail(ex.toString());
        }
    }
}
