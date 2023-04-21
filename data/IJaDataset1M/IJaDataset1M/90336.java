package org.openscience.cdk.reaction.type;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.ReactionProcessTest;
import org.openscience.cdk.reaction.type.parameters.IParameterReact;
import org.openscience.cdk.reaction.type.parameters.SetReactionCenter;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

/**
 * TestSuite that runs a test for the TautomerizationReactionTest.
 * Generalized Reaction: X=Y-Z-H => X(H)-Y=Z.
 *
 * @cdk.module test-reaction
 */
public class TautomerizationReactionTest extends ReactionProcessTest {

    private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();

    /**
	 *  The JUnit setup method
	 */
    public TautomerizationReactionTest() throws Exception {
        setReaction(TautomerizationReaction.class);
    }

    /**
	  *  The JUnit setup method
	  */
    @Test
    public void testTautomerizationReaction() throws Exception {
        IReactionProcess type = new TautomerizationReaction();
        Assert.assertNotNull(type);
    }

    /**
	 * A unit test suite for JUnit for acetaldehyde. 
	 * Reaction: O=C-C-H => O(H)-C=C. 
	 * Automatic looking for active center.
	 *
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testInitiate_IMoleculeSet_IMoleculeSet() throws Exception {
        IReactionProcess type = new TautomerizationReaction();
        IMolecule molecule = getAcetaldehyde();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants.addMolecule(molecule);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.FALSE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule molecule2 = getEthenol();
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, queryAtom));
        IMoleculeSet setOfReactants2 = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants2.addMolecule(molecule2);
        IReactionSet setOfReactions2 = type.initiate(setOfReactants2, null);
        Assert.assertEquals(1, setOfReactions2.getReactionCount());
        Assert.assertEquals(1, setOfReactions2.getReaction(0).getProductCount());
        IMolecule product2 = setOfReactions2.getReaction(0).getProducts().getMolecule(0);
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule, queryAtom));
    }

    /**
	 * A unit test suite for JUnit for acetaldehyde. 
	 * Reaction: O=C-C-H => O(H)-C=C. 
	 * Manually putting for active center.
	 *
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testManuallyCentreActive() throws Exception {
        IReactionProcess type = new TautomerizationReaction();
        IMolecule molecule = getAcetaldehyde();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants.addMolecule(molecule);
        molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(4).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(3).setFlag(CDKConstants.REACTIVE_CENTER, true);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        IMolecule molecule2 = getEthenol();
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2, queryAtom));
        molecule2.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getAtom(6).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule2.getBond(5).setFlag(CDKConstants.REACTIVE_CENTER, true);
        IMoleculeSet setOfReactants2 = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        setOfReactants2.addMolecule(molecule2);
        IReactionSet setOfReactions2 = type.initiate(setOfReactants2, null);
        Assert.assertEquals(1, setOfReactions2.getReactionCount());
        Assert.assertEquals(1, setOfReactions2.getReaction(0).getProductCount());
        IMolecule product2 = setOfReactions2.getReaction(0).getProducts().getMolecule(0);
        queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product2);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule, queryAtom));
    }

    /**
	 * A unit test suite for JUnit.
	 * 
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testCDKConstants_REACTIVE_CENTER() throws Exception {
        IReactionProcess type = new TautomerizationReaction();
        IMoleculeSet setOfReactants = builder.newMoleculeSet();
        IMolecule molecule = getAcetaldehyde();
        molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getAtom(4).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER, true);
        molecule.getBond(3).setFlag(CDKConstants.REACTIVE_CENTER, true);
        setOfReactants.addMolecule(molecule);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        IMolecule reactant = setOfReactions.getReaction(0).getReactants().getMolecule(0);
        Assert.assertTrue(molecule.getAtom(0).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(0).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getAtom(4).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getAtom(4).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getBond(0).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(molecule.getBond(3).getFlag(CDKConstants.REACTIVE_CENTER));
        Assert.assertTrue(reactant.getBond(3).getFlag(CDKConstants.REACTIVE_CENTER));
    }

    /**
	 * A unit test suite for JUnit.
	 *  
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
    @Test
    public void testMapping() throws Exception {
        IReactionProcess type = new TautomerizationReaction();
        IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
        IMolecule molecule = getAcetaldehyde();
        setOfReactants.addMolecule(molecule);
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
        IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.FALSE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        Assert.assertEquals(7, setOfReactions.getReaction(0).getMappingCount());
        IAtom mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(0));
        Assert.assertEquals(mappedProductA1, product.getAtom(0));
        mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(1));
        Assert.assertEquals(mappedProductA1, product.getAtom(1));
        mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(2));
        Assert.assertEquals(mappedProductA1, product.getAtom(2));
        mappedProductA1 = (IAtom) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(4));
        Assert.assertEquals(mappedProductA1, product.getAtom(4));
        IBond mappedProductB1 = (IBond) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getBond(0));
        Assert.assertEquals(mappedProductB1, product.getBond(0));
        mappedProductB1 = (IBond) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getBond(1));
        Assert.assertEquals(mappedProductB1, product.getBond(1));
        mappedProductB1 = (IBond) ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getBond(3));
        Assert.assertEquals(mappedProductB1, product.getBond(5));
    }

    /**
	 * Get the Acetaldehyde structure.
	 * 
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return The IMolecule
	 * @throws CDKException
	 */
    private IMolecule getAcetaldehyde() throws CDKException {
        IMolecule molecule = builder.newMolecule();
        molecule.addAtom(builder.newAtom("O"));
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(0, 1, IBond.Order.DOUBLE);
        molecule.addAtom(builder.newAtom("C"));
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addAtom(builder.newAtom("H"));
        molecule.addBond(1, 3, IBond.Order.SINGLE);
        molecule.addBond(2, 4, IBond.Order.SINGLE);
        molecule.addBond(2, 5, IBond.Order.SINGLE);
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        return molecule;
    }

    /**
	 * Get the Ethenol structure.
	 * 
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2-3H,1H2
	 * 
	 * @return The IMolecule
	 * @throws CDKException
	 */
    private IMolecule getEthenol() throws CDKException {
        IMolecule molecule2 = builder.newMolecule();
        molecule2.addAtom(builder.newAtom("O"));
        molecule2.addAtom(builder.newAtom("C"));
        molecule2.addBond(0, 1, IBond.Order.SINGLE);
        molecule2.addAtom(builder.newAtom("C"));
        molecule2.addBond(1, 2, IBond.Order.DOUBLE);
        molecule2.addAtom(builder.newAtom("H"));
        molecule2.addAtom(builder.newAtom("H"));
        molecule2.addAtom(builder.newAtom("H"));
        molecule2.addAtom(builder.newAtom("H"));
        molecule2.addBond(1, 3, IBond.Order.SINGLE);
        molecule2.addBond(2, 4, IBond.Order.SINGLE);
        molecule2.addBond(2, 5, IBond.Order.SINGLE);
        molecule2.addBond(0, 6, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule2);
        return molecule2;
    }
}
