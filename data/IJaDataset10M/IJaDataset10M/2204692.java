package org.openscience.cdk.test.io;

import java.io.InputStream;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.PCCompoundASNReader;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.LoggingTool;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

/**
 * @cdk.module test-io
 */
public class PCCompoundASNReaderTest extends CDKTestCase {

    private LoggingTool logger;

    public PCCompoundASNReaderTest(String name) {
        super(name);
        logger = new LoggingTool(this);
    }

    public static Test suite() {
        return new TestSuite(PCCompoundASNReaderTest.class);
    }

    public void testAccepts() {
        PCCompoundASNReader reader = new PCCompoundASNReader();
        assertTrue(reader.accepts(ChemFile.class));
    }

    public void testReading() throws Exception {
        String filename = "data/pc-asn/cid1.asn";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        PCCompoundASNReader reader = new PCCompoundASNReader(ins);
        IChemFile cFile = (IChemFile) reader.read(new ChemFile());
        List containers = ChemFileManipulator.getAllAtomContainers(cFile);
        assertEquals(1, containers.size());
        assertTrue(containers.get(0) instanceof IMolecule);
        IMolecule molecule = (IMolecule) containers.get(0);
        assertNotNull(molecule);
        assertEquals(31, molecule.getAtomCount());
        assertNotNull(molecule.getAtom(3));
        assertEquals("O", molecule.getAtom(3).getSymbol());
        assertNotNull(molecule.getAtom(4));
        assertEquals("N", molecule.getAtom(4).getSymbol());
        assertEquals(30, molecule.getBondCount());
        assertNotNull(molecule.getBond(3));
        assertEquals(molecule.getAtom(2), molecule.getBond(3).getAtom(0));
        assertEquals(molecule.getAtom(11), molecule.getBond(3).getAtom(1));
        assertEquals("InChI=1/C9H17NO4/c1-7(11)14-8(5-9(12)13)6-10(2,3)4/h8H,5-6H2,1-4H3", molecule.getProperty(CDKConstants.INCHI));
        assertEquals("CC(=O)OC(CC(=O)[O-])C[N+](C)(C)C", molecule.getProperty(CDKConstants.SMILES));
    }
}
