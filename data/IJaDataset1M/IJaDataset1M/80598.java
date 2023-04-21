package org.openscience.cdk.qsar.descriptors.atomic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import javax.vecmath.Point3d;

/**
 * TestSuite that runs all QSAR tests.
 *
 * @cdk.module test-qsaratomic
 */
public class DistanceToAtomDescriptorTest extends AtomicDescriptorTest {

    public DistanceToAtomDescriptorTest() {
    }

    @Before
    public void setUp() throws Exception {
        setDescriptor(DistanceToAtomDescriptor.class);
    }

    @Test
    public void testDistanceToAtomDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IAtomicDescriptor descriptor = new DistanceToAtomDescriptor();
        Object[] params = { Integer.valueOf(2) };
        descriptor.setParameters(params);
        Molecule mol = new Molecule();
        Atom a0 = new Atom("C");
        mol.addAtom(a0);
        a0.setPoint3d(new Point3d(1.2492, -0.2810, 0.0000));
        Atom a1 = new Atom("C");
        mol.addAtom(a1);
        a1.setPoint3d(new Point3d(0.0000, 0.6024, -0.0000));
        Atom a2 = new Atom("C");
        mol.addAtom(a2);
        a2.setPoint3d(new Point3d(-1.2492, -0.2810, 0.0000));
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        Assert.assertEquals(2.46, ((DoubleResult) descriptor.calculate(mol.getAtom(0), mol).getValue()).doubleValue(), 0.1);
    }
}
