package org.openscience.cdk.test.atomtype;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.StructGenMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-core
 */
public class StructGenMatcherTest extends CDKTestCase {

    public StructGenMatcherTest(String name) {
        super(name);
    }

    public void setUp() {
    }

    public static Test suite() {
        return new TestSuite(StructGenMatcherTest.class);
    }

    public void testStructGenMatcher() throws ClassNotFoundException, CDKException {
        StructGenMatcher matcher = new StructGenMatcher();
        assertNotNull(matcher);
    }

    public void testFindMatchingAtomType_IAtomContainer_IAtom() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Molecule mol = new Molecule();
        Atom atom = new Atom("C");
        atom.setHydrogenCount(4);
        mol.addAtom(atom);
        StructGenMatcher atm = new StructGenMatcher();
        IAtomType matched = atm.findMatchingAtomType(mol, atom);
        assertNotNull(matched);
        assertEquals("C", matched.getSymbol());
    }

    public void testN3() throws ClassNotFoundException, CDKException, java.lang.Exception {
        Molecule mol = new Molecule();
        Atom atom = new Atom("N");
        atom.setHydrogenCount(3);
        mol.addAtom(atom);
        StructGenMatcher atm = new StructGenMatcher();
        IAtomType matched = atm.findMatchingAtomType(mol, atom);
        assertNotNull(matched);
        assertEquals("N", matched.getSymbol());
    }
}
