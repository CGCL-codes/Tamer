package org.openscience.cdk.debug;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.AbstractCrystalTest;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.ICrystal;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the {@link DebugCrystal}.
 *
 * @cdk.module test-datadebug
 */
public class DebugCrystalTest extends AbstractCrystalTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new DebugCrystal();
            }
        });
    }

    @Test
    public void testDebugCrystal() {
        ICrystal crystal = new DebugCrystal();
        Assert.assertNotNull(crystal);
        Assert.assertEquals(0, crystal.getAtomCount());
        Assert.assertEquals(0, crystal.getBondCount());
    }

    @Test
    public void testDebugCrystal_IAtomContainer() {
        IAtomContainer acetone = newChemObject().getBuilder().newAtomContainer();
        IAtom c1 = acetone.getBuilder().newAtom("C");
        IAtom c2 = acetone.getBuilder().newAtom("C");
        IAtom o = acetone.getBuilder().newAtom("O");
        IAtom c3 = acetone.getBuilder().newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = acetone.getBuilder().newBond(c1, c2, IBond.Order.SINGLE);
        IBond b2 = acetone.getBuilder().newBond(c1, o, IBond.Order.DOUBLE);
        IBond b3 = acetone.getBuilder().newBond(c1, c3, IBond.Order.SINGLE);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        ICrystal crystal = new DebugCrystal(acetone);
        Assert.assertNotNull(crystal);
        Assert.assertEquals(4, crystal.getAtomCount());
        Assert.assertEquals(3, crystal.getBondCount());
    }
}
