package org.openscience.cdk.io;

import java.io.InputStream;
import java.io.StringReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NNMolecule;
import org.openscience.cdk.tools.LoggingTool;

/**
 * TestCase for the reading MDL V3000 mol files using one test file.
 *
 * @cdk.module test-io
 *
 * @see org.openscience.cdk.io.MDLReader
 * @see org.openscience.cdk.io.SDFReaderTest
 */
public class MDLV3000ReaderTest extends SimpleChemObjectReaderTest {

    private static LoggingTool logger;

    @BeforeClass
    public static void setup() throws Exception {
        logger = new LoggingTool(MDLV3000ReaderTest.class);
        setSimpleChemObjectReader(new MDLV3000Reader(), "data/mdl/molV3000.mol");
    }

    @Test
    public void testAccepts() {
        MDLV3000Reader reader = new MDLV3000Reader();
        Assert.assertTrue(reader.accepts(Molecule.class));
    }

    /**
     * @cdk.bug 1571207
     */
    @Test
    public void testBug1571207() throws Exception {
        String filename = "data/mdl/molV3000.mol";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV3000Reader reader = new MDLV3000Reader(ins);
        IMolecule m = (IMolecule) reader.read(new NNMolecule());
        Assert.assertNotNull(m);
        Assert.assertEquals(31, m.getAtomCount());
        Assert.assertEquals(34, m.getBondCount());
        IAtom atom = m.getAtom(0);
        Assert.assertNotNull(atom);
        Assert.assertNotNull(atom.getPoint2d());
        Assert.assertEquals(10.4341, atom.getPoint2d().x, 0.0001);
        Assert.assertEquals(5.1053, atom.getPoint2d().y, 0.0001);
    }

    @Test
    public void testEmptyString() throws Exception {
        String emptyString = "";
        MDLV3000Reader reader = new MDLV3000Reader(new StringReader(emptyString));
        try {
            reader.read(new NNMolecule());
            Assert.fail("Should have received a CDK Exception");
        } catch (CDKException cdkEx) {
            Assert.assertEquals("Expected a header line, but found nothing.", cdkEx.getMessage());
        }
    }
}
