package org.openscience.cdk.reaction.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.ReactionProcessTest;
import org.openscience.cdk.reaction.type.parameters.IParameterReact;
import org.openscience.cdk.reaction.type.parameters.SetReactionCenter;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

/**
 * TestSuite that runs a test for the SharingChargeDBReactionTest.
 * Generalized Reaction: [A+]=B => A| + [B+].
 *
 * @cdk.module test-reaction
 */
public class SharingChargeSBReactionTest extends ReactionProcessTest {

    private final LonePairElectronChecker lpcheck = new LonePairElectronChecker();

    private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();

    /**
	 *  The JUnit setup method
	 */
    public SharingChargeSBReactionTest() throws Exception {
        setReaction(SharingChargeSBReaction.class);
    }

    /**
	  *  The JUnit setup method
	  */
    @Test
    public void testSharingChargeSBReaction() throws Exception {
        IReactionProcess type = new SharingChargeSBReaction();
        Assert.assertNotNull(type);
    }

    /**
	 * A unit test suite for JUnit. Reaction:  methoxymethane.
	 * C[O+]!-!C =>  CO + [C+]
	 *           
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testInitiate_IMoleculeSet_IMoleculeSet() throws Exception {
        IReactionProcess type = new SharingChargeSBReaction();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = getMolecule1();
        setOfReactants.addMolecule(molecule);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.FALSE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(3, setOfReactions.getReactionCount());
        Assert.assertEquals(2, setOfReactions.getReaction(0).getProductCount());
        IMolecule product1 = setOfReactions.getReaction(1).getProducts().getMolecule(0);
        IMolecule molecule1 = getMolecule2();
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product1);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule1, queryAtom));
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        IMolecule expected2 = getMolecule2();
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product2, queryAtom));
    }

    /**
	 * A unit test suite for JUnit. Reaction:  methoxymethane.
	 * C[O+]!-!C =>  CO + [C+]
	 * Manually put of the center active.
	 *
	 * @return    The test suite
	 */
    @Test
    public void testManuallyCentreActive() throws Exception {
        IReactionProcess type = new SharingChargeSBReaction();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = getMolecule1();
        setOfReactants.addMolecule(molecule);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(2, setOfReactions.getReaction(0).getProductCount());
        IMolecule product1 = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule molecule1 = getMolecule2();
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product1);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule1, queryAtom));
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        IMolecule expected2 = getMolecule3();
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product2, queryAtom));
    }

    /**
	 * A unit test suite for JUnit.
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testCDKConstants_REACTIVE_CENTER() throws Exception {
        IReactionProcess type = new SharingChargeSBReaction();
        IMoleculeSet setOfReactants = builder.newMoleculeSet();
        IMolecule molecule = getMolecule1();
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        setOfReactants.addMolecule(molecule);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        IMolecule reactant = setOfReactions.getReaction(0).getReactants().getMolecule(0);
        Assert.assertTrue(molecule.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
    }

    /**
	 * A unit test suite for JUnit.
	 *  
	 * @return    The test suite
	 */
    @Test
    public void testMapping() throws Exception {
        IReactionProcess type = new SharingChargeSBReaction();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = getMolecule1();
        setOfReactants.addMolecule(molecule);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        IMolecule product1 = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        Assert.assertEquals(2, setOfReactions.getReaction(0).getMappingCount());
        IAtom mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(1));
        Assert.assertEquals(mappedProductA1, product1.getAtom(1));
        mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(2));
        Assert.assertEquals(mappedProductA1, product2.getAtom(0));
    }

    /**
	 * Test to recognize if this IMolecule_1 matches correctly into the CDKAtomTypes.
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws CDKException 
	 */
    @Test
    public void testAtomTypesMolecule1() throws CDKException, ClassNotFoundException, Exception {
        IMolecule moleculeTest = getMolecule1();
        makeSureAtomTypesAreRecognized(moleculeTest);
    }

    /**
	 * Test to recognize if this IMolecule_2 matches correctly into the CDKAtomTypes.
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws CDKException 
	 */
    @Test
    public void testAtomTypesMolecule2() throws CDKException, ClassNotFoundException, Exception {
        IMolecule moleculeTest = getMolecule2();
        makeSureAtomTypesAreRecognized(moleculeTest);
    }

    /**
	 * get the molecule 1: C[O+]!-!C
	 * 
	 * @return The IMolecule
	 */
    private IMolecule getMolecule1() throws Exception {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("C"));
        molecule.addAtom(builder.newAtom("O"));
        molecule.getAtom(1).setFormalCharge(+1);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.SINGLE);
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(0, 3, IBond.Order.SINGLE);
        molecule.addBond(0, 4, IBond.Order.SINGLE);
        molecule.addBond(0, 5, IBond.Order.SINGLE);
        molecule.addBond(1, 6, IBond.Order.SINGLE);
        molecule.addBond(2, 7, IBond.Order.SINGLE);
        molecule.addBond(2, 8, IBond.Order.SINGLE);
        molecule.addBond(2, 9, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        lpcheck.saturate(molecule);
        return molecule;
    }

    /**
	 * get the molecule 2: [C+]
	 * 
	 * @return The IMolecule
	 */
    private IMolecule getMolecule2() throws Exception {
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("O"));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(0, 4, IBond.Order.SINGLE);
        expected1.addBond(1, 5, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        lpcheck.saturate(expected1);
        return expected1;
    }

    /**
	 * get the molecule 3: [C+]
	 * 
	 * @return The IMolecule
	 */
    private IMolecule getMolecule3() throws Exception {
        IMolecule expected2 = builder.newMolecule();
        expected2.addAtom(builder.newAtom("C"));
        expected2.getAtom(0).setFormalCharge(+1);
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addBond(0, 1, IBond.Order.SINGLE);
        expected2.addBond(0, 2, IBond.Order.SINGLE);
        expected2.addBond(0, 3, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected2);
        return expected2;
    }

    /**
	 * Test to recognize if a IMolecule matcher correctly identifies the CDKAtomTypes.
	 * 
	 * @param molecule          The IMolecule to analyze
	 * @throws CDKException
	 */
    private void makeSureAtomTypesAreRecognized(IMolecule molecule) throws CDKException {
        Iterator<IAtom> atoms = molecule.atoms().iterator();
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
        while (atoms.hasNext()) {
            IAtom nextAtom = atoms.next();
            Assert.assertNotNull("Missing atom type for: " + nextAtom, matcher.findMatchingAtomType(molecule, nextAtom));
        }
    }

    /**
	 * A unit test suite for JUnit. Reaction:.
	 * C[N+]!-!C => CN + [C+]
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testNsp3ChargeSingleB() throws Exception {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("C"));
        molecule.addAtom(builder.newAtom("N"));
        molecule.getAtom(1).setFormalCharge(+1);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.SINGLE);
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(0, 3, IBond.Order.SINGLE);
        molecule.addBond(0, 4, IBond.Order.SINGLE);
        molecule.addBond(0, 5, IBond.Order.SINGLE);
        molecule.addBond(1, 6, IBond.Order.SINGLE);
        molecule.addBond(1, 7, IBond.Order.SINGLE);
        molecule.addBond(2, 8, IBond.Order.SINGLE);
        molecule.addBond(2, 9, IBond.Order.SINGLE);
        molecule.addBond(2, 10, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        lpcheck.saturate(molecule);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants.addMolecule(molecule);
        IReactionProcess type = new SharingChargeSBReaction();
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
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
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        lpcheck.saturate(expected1);
        IMolecule product1 = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        QueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected1);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product1, queryAtom));
        IMolecule expected2 = builder.newMolecule();
        expected2.addAtom(builder.newAtom("C"));
        expected2.getAtom(0).setFormalCharge(+1);
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addBond(0, 1, IBond.Order.SINGLE);
        expected2.addBond(0, 2, IBond.Order.SINGLE);
        expected2.addBond(0, 3, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected2);
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product2, queryAtom));
    }

    /**
	 * A unit test suite for JUnit. Reaction:.
	 * C=[N+]!-!C => C=N + [C+]
	 *
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testNsp2ChargeSingleB() throws Exception {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("C"));
        molecule.addAtom(builder.newAtom("N"));
        molecule.getAtom(1).setFormalCharge(1);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.DOUBLE);
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(0, 3, IBond.Order.SINGLE);
        molecule.addBond(0, 4, IBond.Order.SINGLE);
        molecule.addBond(1, 5, IBond.Order.SINGLE);
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        molecule.addBond(2, 7, IBond.Order.SINGLE);
        molecule.addBond(2, 8, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        lpcheck.saturate(molecule);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants.addMolecule(molecule);
        IReactionProcess type = new SharingChargeSBReaction();
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("C"));
        expected1.addAtom(builder.newAtom("N"));
        expected1.addBond(0, 1, IBond.Order.DOUBLE);
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 2, IBond.Order.SINGLE);
        expected1.addBond(0, 3, IBond.Order.SINGLE);
        expected1.addBond(1, 4, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        lpcheck.saturate(expected1);
        IMolecule product1 = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        QueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected1);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product1, queryAtom));
        IMolecule expected2 = builder.newMolecule();
        expected2.addAtom(builder.newAtom("C"));
        expected2.getAtom(0).setFormalCharge(+1);
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addBond(0, 1, IBond.Order.SINGLE);
        expected2.addBond(0, 2, IBond.Order.SINGLE);
        expected2.addBond(0, 3, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected2);
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product2, queryAtom));
    }

    /**
	 * A unit test suite for JUnit. Reaction:.
	 * [F+]!-!C => F + [C+]
	 *
	 * @return    The test suite
	 */
    @Test
    public void testFspChargeSingleB() throws Exception {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("F"));
        molecule.getAtom(0).setFormalCharge(+1);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(0, 2, IBond.Order.SINGLE);
        molecule.addBond(1, 3, IBond.Order.SINGLE);
        molecule.addBond(1, 4, IBond.Order.SINGLE);
        molecule.addBond(1, 5, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        lpcheck.saturate(molecule);
        molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants.addMolecule(molecule);
        IReactionProcess type = new SharingChargeSBReaction();
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        IMolecule expected1 = builder.newMolecule();
        expected1.addAtom(builder.newAtom("F"));
        expected1.addAtom(builder.newAtom("H"));
        expected1.addBond(0, 1, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected1);
        lpcheck.saturate(expected1);
        IMolecule product1 = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        QueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected1);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product1, queryAtom));
        IMolecule expected2 = builder.newMolecule();
        expected2.addAtom(builder.newAtom("C"));
        expected2.getAtom(0).setFormalCharge(+1);
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addAtom(builder.newAtom("H"));
        expected2.addBond(0, 1, IBond.Order.SINGLE);
        expected2.addBond(0, 2, IBond.Order.SINGLE);
        expected2.addBond(0, 3, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(expected2);
        IMolecule product2 = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(expected2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(product2, queryAtom));
    }
}
