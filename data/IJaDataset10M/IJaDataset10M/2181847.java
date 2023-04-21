package org.openscience.cdk.test.qsar.descriptors.bond;

import javax.vecmath.Point3d;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IBondDescriptor;
import org.openscience.cdk.test.qsar.descriptors.DescriptorTest;

/**
 * Tests for bond descriptors.
 *
 * @cdk.module test-qsarbond
 */
public abstract class BondDescriptorTest extends DescriptorTest {

    protected IBondDescriptor descriptor;

    public BondDescriptorTest() {
    }

    public BondDescriptorTest(String name) {
        super(name);
    }

    public void setDescriptor(Class descriptorClass) throws Exception {
        if (descriptor == null) {
            Object descriptor = (Object) descriptorClass.newInstance();
            if (!(descriptor instanceof IBondDescriptor)) {
                throw new CDKException("The passed descriptor class must be a IBondDescriptor");
            }
            this.descriptor = (IBondDescriptor) descriptor;
        }
        super.setDescriptor(descriptorClass);
    }

    public void testCalculate_IBond() throws Exception {
        IAtomContainer mol = someoneBringMeSomeWater();
        DescriptorValue v = descriptor.calculate(mol.getBond(0), mol);
        assertNotNull(v);
        assertNotSame("The descriptor did not calculate any value.", 0, v.getValue().length());
    }

    /**
	 * Checks if the given labels are consistent.
	 * 
	 * @throws Exception Passed on from calculate.
	 */
    public void testLabels() throws Exception {
        IAtomContainer mol = someoneBringMeSomeWater();
        DescriptorValue v = descriptor.calculate(mol.getBond(0), mol);
        assertNotNull(v);
        String[] names = v.getNames();
        assertNotNull("The descriptor must return labels using the getNames() method.", names);
        assertNotSame("At least one label must be given.", 0, names.length);
        for (int i = 0; i < names.length; i++) {
            assertNotNull("A descriptor label may not be null.", names[i]);
            assertNotSame("The label string must not be empty.", 0, names[i].length());
        }
        assertNotNull(v.getValue());
        int valueCount = v.getValue().length();
        assertEquals("The number of labels must equals the number of values.", names.length, valueCount);
    }

    private IMolecule someoneBringMeSomeWater() {
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule();
        IAtom c1 = DefaultChemObjectBuilder.getInstance().newAtom("O");
        c1.setPoint3d(new Point3d(0.0, 0.0, 0.0));
        IAtom h1 = DefaultChemObjectBuilder.getInstance().newAtom("H");
        h1.setPoint3d(new Point3d(1.0, 0.0, 0.0));
        IAtom h2 = DefaultChemObjectBuilder.getInstance().newAtom("H");
        h2.setPoint3d(new Point3d(-1.0, 0.0, 0.0));
        mol.addAtom(c1);
        mol.addAtom(h1);
        mol.addAtom(h2);
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        return mol;
    }
}
