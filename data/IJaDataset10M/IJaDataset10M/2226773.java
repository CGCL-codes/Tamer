package org.openscience.cdk.test.reaction.type;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.type.BreakingBondReaction;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

/**
 * TestSuite that runs a test for the BreakingBondReactionTest.
 * Generalized Reaction: A=B => [A-]-[B+] + [A+]-[B-].
 *
 * @cdk.module test-reaction
 */
public class BreakingBondReactionTest extends CDKTestCase {

    private IReactionProcess type;

    /**
	 * Constructror of the BreakingBondReactionTest object
	 *
	 */
    public BreakingBondReactionTest() {
        type = new BreakingBondReaction();
    }

    public static Test suite() {
        return new TestSuite(BreakingBondReactionTest.class);
    }

    /**
	 * A unit test suite for JUnit. Reaction: 
	 * C(H)(H)=O => [C+](H)(H)-[O-] + [C+](H)=O +  
	 * Automatic sarch of the centre active.
	 *
	 * @return    The test suite
	 */
    public void testBB_AutomaticSearchCentreActiveFormaldehyde() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("C=O");
        HydrogenAdder adder = new HydrogenAdder();
        adder.addExplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        setOfReactants.addMolecule(molecule);
        Object[] params = { Boolean.FALSE };
        type.setParameters(params);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(5, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());
        Assert.assertEquals(2, setOfReactions.getReaction(1).getProductCount());
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("[C+]-[O-]");
        molecule2.addAtom(new Atom("H"));
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(0, 2, 1);
        molecule2.addBond(0, 3, 1);
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
        product = setOfReactions.getReaction(1).getProducts().getMolecule(0);
        molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("C=O");
        molecule2.getAtom(0).setFormalCharge(+1);
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(0, 2, 1);
        qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
        product = setOfReactions.getReaction(2).getProducts().getMolecule(0);
        molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("C=O");
        molecule2.getAtom(0).setFormalCharge(-1);
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(0, 2, 1);
        qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
    }

    /**
	 * A unit test suite for JUnit. Reaction: C=O => [C+]-[O-]
	 * Manually put of the centre active.
	 *
	 * @return    The test suite
	 */
    public void testBB_ManuallyPutCentreActiveFormaldehyde() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("C=O");
        HydrogenAdder adder = new HydrogenAdder();
        adder.addExplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        setOfReactants.addMolecule(molecule);
        molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        Object[] params = { Boolean.TRUE };
        type.setParameters(params);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("[C+]-[O-]");
        molecule2.addAtom(new Atom("H"));
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(0, 2, 1);
        molecule2.addBond(0, 3, 1);
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
    }

    /**
	 * A unit test suite for JUnit. Reaction: C=O => [C+]-[O-] + [C-]-[O+]
	 * Test of mapped between the reactant and product. Only is mapped the centre active.
	 *
	 * @return    The test suite
	 */
    public void testBB_MappingFormaldehyde() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("C=O");
        HydrogenAdder adder = new HydrogenAdder();
        adder.addExplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        setOfReactants.addMolecule(molecule);
        Object[] params = { Boolean.FALSE };
        type.setParameters(params);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        Assert.assertEquals(3, setOfReactions.getReaction(0).getMappingCount());
        IAtom mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(0));
        assertEquals(mappedProductA1, product.getAtom(0));
        IAtom mappedProductA2 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(1));
        assertEquals(mappedProductA2, product.getAtom(1));
        IBond mappedProductB1 = (IBond) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getBond(0));
        assertEquals(mappedProductB1, product.getBond(0));
    }

    /**
	 * A unit test suite for JUnit. Reaction: 
	 * F-CC => [F-] + [C+]C  
	 *
	 * @return    The test suite
	 */
    public void testBB_1() throws ClassNotFoundException, CDKException, java.lang.Exception {
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("FCC");
        HydrogenAdder adder = new HydrogenAdder();
        adder.addExplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        setOfReactants.addMolecule(molecule);
        molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        Object[] params = { Boolean.TRUE };
        type.setParameters(params);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(2, setOfReactions.getReaction(0).getProductCount());
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(1);
        IMolecule molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("[C+]C");
        molecule2.addAtom(new Atom("H"));
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(0, 2, 1);
        molecule2.addBond(0, 3, 1);
        molecule2.addAtom(new Atom("H"));
        molecule2.addAtom(new Atom("H"));
        molecule2.addAtom(new Atom("H"));
        molecule2.addBond(1, 4, 1);
        molecule2.addBond(1, 5, 1);
        molecule2.addBond(1, 6, 1);
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
        product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        molecule2 = (new SmilesParser(DefaultChemObjectBuilder.getInstance())).parseSmiles("[F-]");
        qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, qAC));
    }
}
