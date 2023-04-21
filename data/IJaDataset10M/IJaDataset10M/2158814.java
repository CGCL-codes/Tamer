package org.openscience.cdk.atomtype;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.SingleElectron;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.reaction.type.ElectronImpactNBEReaction;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.reaction.type.AdductionSodiumLPReactionTest;
import org.openscience.cdk.reaction.type.HeterolyticCleavageSBReactionTest;
import org.openscience.cdk.reaction.type.HomolyticCleavageReactionTest;
import org.openscience.cdk.reaction.type.RadicalSiteInitiationHReactionTest;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * @cdk.module test-reaction
 */
public class ReactionStructuresTest extends CDKTestCase {

    private static final IChemObjectBuilder builder;

    private static final CDKAtomTypeMatcher matcher;

    static {
        builder = NoNotificationChemObjectBuilder.getInstance();
        matcher = CDKAtomTypeMatcher.getInstance(builder);
    }

    /**
	 * Constructor of the ReactionStructuresTest.
	 */
    public ReactionStructuresTest() {
        super();
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       SharingChargeDBReactionTest#testAtomTypesMolecule1()
	 */
    @Test
    public void testM0() throws Exception {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("C"));
        molecule.addSingleElectron(new SingleElectron(molecule.getAtom(0)));
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.DOUBLE);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(0, 3, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(1, 4, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(2, 5, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(2, 7, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.radical.sp2", "C.sp2", "C.sp3", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, molecule.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = molecule.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(molecule, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HeterolyticCleavageSBReactionTest#testCspSingleB()
	 */
    @Test
    public void testM4() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.getAtom(1).setFormalCharge(+1);
        expected1.addBond(0, 1, IBond.Order.TRIPLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp", "C.plus.sp1", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testCsp2SingleB()
	 */
    @Test
    public void testM5() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(1, 4, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp2", "C.radical.sp2", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testCsp2SingleB()
	 */
    @Test
    public void testM6() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.TRIPLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp", "C.radical.sp1", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testCsp2DoubleB()
	 */
    @Test
    public void testM7() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(2)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(0, 5, IBond.Order.SINGLE);
        expected1.addBond(1, 6, IBond.Order.SINGLE);
        expected1.addBond(2, 7, IBond.Order.SINGLE);
        expected1.addBond(2, 8, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp3", "C.radical.planar", "C.radical.planar", "H", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testCspDoubleB()
	 */
    @Test
    public void testM8() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(2)));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(2, 5, IBond.Order.SINGLE);
        expected1.addBond(2, 6, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp2", "C.radical.sp2", "C.radical.planar", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testNsp3SingleB()
	 */
    @Test
    public void testM9() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(1, 5, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp3", "N.sp3.radical", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testNsp2SingleB()
	 */
    @Test
    public void testM10() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp2", "N.sp2.radical", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testOsp2SingleB()
	 */
    @Test
    public void testM13() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("O"));
        expected1.getAtom(0).setFormalCharge(1);
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(0)));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addBond(1, 3, IBond.Order.SINGLE);
        String[] expectedTypes = { "O.plus.radical", "C.radical.planar", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testFspSingleB()
	 */
    @Test
    public void testM14() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("F"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(0)));
        String[] expectedTypes = { "F.radical" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       HomolyticCleavageReactionTest#testOsp2SingleB()
	 */
    @Test
    public void testM15() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("O"));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(0, 5, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp3", "O.sp3.radical", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       ElectronImpactNBEReaction#testNsp2SingleB()
	 */
    @Test
    public void testM17() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("N"));
        expected1.getAtom(0).setFormalCharge(1);
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(0)));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(1, 3, IBond.Order.SINGLE);
        expected1.addBond(1, 4, IBond.Order.SINGLE);
        String[] expectedTypes = { "N.plus.sp2.radical", "C.sp2", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       ElectronImpactNBEReaction#testNsp3SingleB()
	 */
    @Test
    public void testM18() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.getAtom(1).setFormalCharge(1);
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(1, 5, IBond.Order.SINGLE);
        expected1.addBond(1, 6, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp3", "N.plus.sp3.radical", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       ElectronImpactNBEReaction#testNsp3SingleB()
	 */
    @Test
    public void testM19() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.getAtom(1).setFormalCharge(1);
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(1, 4, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp2", "N.plus.sp2.radical", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see       RadicalSiteInitiationHReactionTest#testManuallyCentreActive()
	 */
    @Test
    public void testM20() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("H"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(0)));
        String[] expectedTypes = { "H.radical" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 */
    @Test
    public void testM21() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("Na"));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        String[] expectedTypes = { "H", "Na" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see AdductionSodiumLPReactionTest
	 */
    @Test
    public void testM22() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("O"));
        expected1.getAtom(0).setFormalCharge(1);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("Na"));
        expected1.addBond(1, 3, IBond.Order.SINGLE);
        expected1.addBond(2, 4, IBond.Order.SINGLE);
        expected1.addBond(2, 5, IBond.Order.SINGLE);
        expected1.addBond(2, 6, IBond.Order.SINGLE);
        expected1.addBond(0, 7, IBond.Order.SINGLE);
        String[] expectedTypes = { "O.plus.sp2", "C.sp2", "C.sp3", "H", "H", "H", "H", "Na" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see AdductionSodiumLPReactionTest
	 */
    @Test
    public void testM23() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("R"));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.getAtom(3).setFormalCharge(1);
        expected1.addBond(2, 3, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(3, 4, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(4, 5, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(5, 6, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(1, 7, IBond.Order.SINGLE);
        expected1.addBond(1, 8, IBond.Order.SINGLE);
        expected1.addBond(2, 9, IBond.Order.SINGLE);
        expected1.addBond(2, 10, IBond.Order.SINGLE);
        expected1.addBond(3, 11, IBond.Order.SINGLE);
        expected1.addBond(4, 12, IBond.Order.SINGLE);
        expected1.addBond(4, 13, IBond.Order.SINGLE);
        expected1.addBond(5, 14, IBond.Order.SINGLE);
        expected1.addBond(5, 15, IBond.Order.SINGLE);
        expected1.addBond(6, 16, IBond.Order.SINGLE);
        expected1.addBond(6, 17, IBond.Order.SINGLE);
        expected1.addBond(6, 18, IBond.Order.SINGLE);
        String[] expectedTypes = { "R", "C.sp3", "C.sp3", "C.sp2", "C.sp3", "C.sp3", "C.sp3", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see HomolyticCleavageReactionTest#testNsp2DoubleB
	 */
    @Test
    public void testM24() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(0)));
        expected1.addAtom(builder.newAtom("C"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(0, 5, IBond.Order.SINGLE);
        expected1.addBond(2, 6, IBond.Order.SINGLE);
        expected1.addBond(2, 7, IBond.Order.SINGLE);
        String[] expectedTypes = { "C.sp3", "N.radical.planar", "C.radical.planar", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see HomolyticCleavageReactionTest#testNsp2DoubleB
	 */
    @Test
    public void testM25() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("O"));
        expected1.addSingleElectron(builder.newSingleElectron(expected1.getAtom(1)));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        String[] expectedTypes = { "C.sp3", "O.sp3.radical", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
        }
    }

    /**
	 * A unit test suite for JUnit. Compound and its fragments to be tested
	 * @throws Exception 
	 * 
	 * @see HomolyticCleavageReactionTest#testNsp2DoubleB
	 */
    @Test
    public void testM26() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("F"));
        expected1.getAtom(0).setFormalCharge(1);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.getAtom(2).setFormalCharge(-1);
        expected1.addBond(1, 2, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(2, 3, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(3, 4, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(4, 5, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("C"));
        expected1.addBond(5, 6, IBond.Order.DOUBLE);
        expected1.addBond(6, 1, IBond.Order.SINGLE);
        addExplicitHydrogens(expected1);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.saturate(expected1);
        String[] expectedTypes = { "F.plus", "C.sp2", "C.minus.planar", "C.sp2", "C.sp2", "C.sp2", "C.sp2", "H", "H", "H", "H", "H" };
        Assert.assertEquals(expectedTypes.length, expected1.getAtomCount());
        for (int i = 0; i < expectedTypes.length; i++) {
            IAtom nextAtom = expected1.getAtom(i);
            IAtomType perceivedType = matcher.findMatchingAtomType(expected1, nextAtom);
            System.out.println(nextAtom);
            Assert.assertNotNull("Missing atom type for: " + nextAtom, perceivedType);
            System.out.println(perceivedType.getAtomTypeName());
            Assert.assertEquals("Incorrect atom type perceived for: " + nextAtom, expectedTypes[i], perceivedType.getAtomTypeName());
            nextAtom.setHybridization(null);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
            IAtomType type = matcher.findMatchingAtomType(expected1, nextAtom);
            System.out.println(type.getAtomTypeName());
            Assert.assertNotNull(type);
        }
    }
}
