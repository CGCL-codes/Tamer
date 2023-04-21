package org.openscience.cdk.test.tools.manipulator;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.SetOfAtomContainers;
import org.openscience.cdk.SetOfMolecules;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.manipulator.SetOfAtomContainersManipulator;

/**
 * @cdk.module test
 *
 * @author     Kai Hartmann
 * @cdk.created    2004-02-20
 */
public class SetOfAtomContainersManipulatorTest extends CDKTestCase {

    SetOfMolecules som = new SetOfMolecules();

    public SetOfAtomContainersManipulatorTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SetOfAtomContainersManipulatorTest.class);
        return suite;
    }

    public void testRemoveAtomAndConnectedElectronContainers() {
        AtomContainer ac = new AtomContainer();
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addBond(new Bond(ac.getAtomAt(0), ac.getAtomAt(1), 1));
        ac.addBond(new Bond(ac.getAtomAt(1), ac.getAtomAt(2), 1));
        SetOfAtomContainers soac = new SetOfAtomContainers();
        soac.addAtomContainer(ac);
        SetOfAtomContainersManipulator.removeAtomAndConnectedElectronContainers(soac, ac.getAtomAt(1));
        assertEquals(2, soac.getAtomContainerCount(), 0.000001);
    }
}
