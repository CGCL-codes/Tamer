package org.openscience.cdk.test.templates;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.AminoAcid;
import org.openscience.cdk.templates.AminoAcids;
import org.openscience.cdk.test.CDKTestCase;
import java.util.HashMap;

/**
 * @cdk.module test-pdb
 */
public class AminoAcidsTest extends CDKTestCase {

    public static Test suite() {
        return new TestSuite(AminoAcidsTest.class);
    }

    public void testCreateBondMatrix() {
        int[][] bonds = AminoAcids.aaBondInfo();
        assertNotNull(bonds);
    }

    public void testCreateAAs() {
        AminoAcid[] aas = AminoAcids.createAAs();
        assertNotNull(aas);
        assertEquals(20, aas.length);
        for (int i = 0; i < 20; i++) {
            assertNotNull(aas[i]);
            assertFalse(0 == aas[i].getAtomCount());
            assertFalse(0 == aas[i].getBondCount());
            assertNotNull(aas[i].getMonomerName());
            assertNotNull(aas[i].getProperty(AminoAcids.RESIDUE_NAME_SHORT));
            assertNotNull(aas[i].getProperty(AminoAcids.RESIDUE_NAME));
        }
    }

    public void testGetHashMapBySingleCharCode() {
        HashMap map = AminoAcids.getHashMapBySingleCharCode();
        assertNotNull(map);
        assertEquals(20, map.size());
        String[] aas = { "G", "A", "V", "L" };
        for (int i = 0; i < aas.length; i++) {
            AminoAcid aa = (AminoAcid) map.get(aas[i]);
            assertNotNull("Did not find AA for: " + aas[i], aa);
        }
    }

    public void testGetHashMapByThreeLetterCode() {
        HashMap map = AminoAcids.getHashMapByThreeLetterCode();
        assertNotNull(map);
        assertEquals(20, map.size());
        String[] aas = { "GLY", "ALA" };
        for (int i = 0; i < aas.length; i++) {
            AminoAcid aa = (AminoAcid) map.get(aas[i]);
            assertNotNull("Did not find AA for: " + aas[i], aa);
        }
    }
}
