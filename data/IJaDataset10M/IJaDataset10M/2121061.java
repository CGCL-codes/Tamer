package org.openscience.cdk.test.io;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.io.VASPReader;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-experimental
 */
public class VASPReaderTest extends CDKTestCase {

    private org.openscience.cdk.tools.LoggingTool logger;

    public VASPReaderTest(String name) {
        super(name);
        logger = new org.openscience.cdk.tools.LoggingTool(this);
    }

    public static Test suite() {
        return new TestSuite(VASPReaderTest.class);
    }

    public void testAccepts() {
        VASPReader reader = new VASPReader();
        assertTrue(reader.accepts(ChemFile.class));
    }

    public void testReading() throws Exception {
        String filename = "data/vasp/LiMoS2_optimisation_ISIF3.vasp";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        VASPReader reader = new VASPReader(ins);
        ChemFile chemFile = (ChemFile) reader.read(new ChemFile());
        assertNotNull(chemFile);
        org.openscience.cdk.interfaces.IChemSequence sequence = chemFile.getChemSequence(0);
        assertNotNull(sequence);
        assertEquals(6, sequence.getChemModelCount());
        org.openscience.cdk.interfaces.IChemModel model = sequence.getChemModel(0);
        assertNotNull(model);
        org.openscience.cdk.interfaces.ICrystal crystal = model.getCrystal();
        assertNotNull(crystal);
        assertEquals(16, crystal.getAtomCount());
        org.openscience.cdk.interfaces.IAtom atom = crystal.getAtom(0);
        assertNotNull(atom);
        assertNotNull(atom.getFractionalPoint3d());
    }
}
