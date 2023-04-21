package org.openscience.cdk.similarity;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;
import java.util.BitSet;

/**
 * @cdk.module test-extra
 */
public class TanimotoTest extends CDKTestCase {

    boolean standAlone = false;

    @Test
    public void testTanimoto1() throws java.lang.Exception {
        Molecule mol1 = MoleculeFactory.makeIndole();
        Molecule mol2 = MoleculeFactory.makePyrrole();
        Fingerprinter fingerprinter = new Fingerprinter();
        BitSet bs1 = fingerprinter.getFingerprint(mol1);
        BitSet bs2 = fingerprinter.getFingerprint(mol2);
        float tanimoto = Tanimoto.calculate(bs1, bs2);
        if (standAlone) System.out.println("Tanimoto: " + tanimoto);
        if (!standAlone) Assert.assertEquals(0.3939, tanimoto, 0.01);
    }

    @Test
    public void testTanimoto2() throws java.lang.Exception {
        Molecule mol1 = MoleculeFactory.makeIndole();
        Molecule mol2 = MoleculeFactory.makeIndole();
        Fingerprinter fingerprinter = new Fingerprinter();
        BitSet bs1 = fingerprinter.getFingerprint(mol1);
        BitSet bs2 = fingerprinter.getFingerprint(mol2);
        float tanimoto = Tanimoto.calculate(bs1, bs2);
        if (standAlone) System.out.println("Tanimoto: " + tanimoto);
        if (!standAlone) Assert.assertEquals(1.0, tanimoto, 0.001);
    }

    @Test
    public void testTanimoto3() throws java.lang.Exception {
        double[] f1 = { 1, 2, 3, 4, 5, 6, 7 };
        double[] f2 = { 1, 2, 3, 4, 5, 6, 7 };
        float tanimoto = Tanimoto.calculate(f1, f2);
        if (standAlone) System.out.println("Tanimoto: " + tanimoto);
        if (!standAlone) Assert.assertEquals(1.0, tanimoto, 0.001);
    }

    @Test
    public void visualTestR00258() throws java.lang.Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        String smiles1 = "O=C(O)CCC(=O)C(=O)O";
        String smiles2 = "O=C(O)C(N)CCC(=O)O";
        String smiles3 = "O=C(O)C(N)C";
        String smiles4 = "CC(=O)C(=O)O";
        IMolecule molecule1 = sp.parseSmiles(smiles1);
        IMolecule molecule2 = sp.parseSmiles(smiles2);
        IMolecule molecule3 = sp.parseSmiles(smiles3);
        IMolecule molecule4 = sp.parseSmiles(smiles4);
        Fingerprinter fingerprinter = new Fingerprinter(1024, 6);
        BitSet bs1 = fingerprinter.getFingerprint(molecule1);
        BitSet bs2 = fingerprinter.getFingerprint(molecule2);
        BitSet bs3 = fingerprinter.getFingerprint(molecule3);
        BitSet bs4 = fingerprinter.getFingerprint(molecule4);
        float tanimoto1 = Tanimoto.calculate(bs1, bs2);
        float tanimoto2 = Tanimoto.calculate(bs1, bs3);
        float tanimoto3 = Tanimoto.calculate(bs1, bs4);
        float tanimoto4 = Tanimoto.calculate(bs2, bs3);
        float tanimoto5 = Tanimoto.calculate(bs2, bs4);
        float tanimoto6 = Tanimoto.calculate(bs3, bs4);
        System.out.println("Similarity 1 vs. 2: " + tanimoto1);
        System.out.println("Similarity 1 vs. 3: " + tanimoto2);
        System.out.println("Similarity 1 vs. 4: " + tanimoto3);
        System.out.println("Similarity 2 vs. 3: " + tanimoto4);
        System.out.println("Similarity 2 vs. 4: " + tanimoto5);
        System.out.println("Similarity 3 vs. 4: " + tanimoto6);
    }
}
