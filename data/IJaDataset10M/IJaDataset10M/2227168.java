package org.openscience.cdk.test.isomorphism;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.OrderQueryBond;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.SymbolAndChargeQueryAtom;
import org.openscience.cdk.isomorphism.matchers.SymbolQueryAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.AnyAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.AnyOrderQueryBond;
import org.openscience.cdk.isomorphism.matchers.smarts.ImplicitHCountAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.SMARTSAtom;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module  test-smarts
 * @cdk.require java1.4+
 */
public class SMARTSTest extends CDKTestCase {

    public SMARTSTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SMARTSTest.class);
    }

    public void testStrictSMARTS() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer atomContainer = sp.parseSmiles("CC(=O)OC(=O)C");
        QueryAtomContainer query = new QueryAtomContainer();
        SymbolQueryAtom atom1 = new SymbolQueryAtom();
        atom1.setSymbol("N");
        SymbolQueryAtom atom2 = new SymbolQueryAtom();
        atom2.setSymbol("C");
        query.addAtom(atom1);
        query.addAtom(atom2);
        query.addBond(new OrderQueryBond(atom1, atom2, 2));
        assertFalse(UniversalIsomorphismTester.isSubgraph(atomContainer, query));
    }

    public void testSMARTS() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer atomContainer = sp.parseSmiles("CC(=O)OC(=O)C");
        QueryAtomContainer query = new QueryAtomContainer();
        AnyAtom atom1 = new AnyAtom();
        SymbolQueryAtom atom2 = new SymbolQueryAtom();
        atom2.setSymbol("C");
        query.addAtom(atom1);
        query.addAtom(atom2);
        query.addBond(new OrderQueryBond(atom1, atom2, 2));
        assertTrue(UniversalIsomorphismTester.isSubgraph(atomContainer, query));
    }

    private IAtomContainer createEthane() {
        IAtomContainer container = new org.openscience.cdk.AtomContainer();
        IAtom carbon = new org.openscience.cdk.Atom("C");
        IAtom carbon2 = carbon.getBuilder().newAtom("C");
        carbon.setHydrogenCount(3);
        carbon2.setHydrogenCount(3);
        container.addAtom(carbon);
        container.addAtom(carbon2);
        container.addBond(carbon.getBuilder().newBond(carbon, carbon2, 1));
        return container;
    }

    public void testImplicitHCountAtom() throws Exception {
        IAtomContainer container = createEthane();
        QueryAtomContainer query1 = new QueryAtomContainer();
        SMARTSAtom atom1 = new ImplicitHCountAtom(3);
        SMARTSAtom atom2 = new ImplicitHCountAtom(3);
        query1.addAtom(atom1);
        query1.addAtom(atom2);
        query1.addBond(new OrderQueryBond(atom1, atom2, 1));
        assertTrue(UniversalIsomorphismTester.isSubgraph(container, query1));
    }

    public void testImplicitHCountAtom2() throws Exception {
        IAtomContainer container = createEthane();
        QueryAtomContainer query1 = new QueryAtomContainer();
        SMARTSAtom atom1 = new ImplicitHCountAtom(3);
        SMARTSAtom atom2 = new ImplicitHCountAtom(2);
        query1.addAtom(atom1);
        query1.addAtom(atom2);
        query1.addBond(new OrderQueryBond(atom1, atom2, 1));
        assertFalse(UniversalIsomorphismTester.isSubgraph(container, query1));
    }

    public void testMatchInherited() {
        try {
            SymbolQueryAtom c1 = new SymbolQueryAtom(new org.openscience.cdk.Atom("C"));
            SymbolAndChargeQueryAtom c2 = new SymbolAndChargeQueryAtom(new org.openscience.cdk.Atom("C"));
            IAtomContainer c = MoleculeFactory.makeAlkane(2);
            QueryAtomContainer query1 = new QueryAtomContainer();
            query1.addAtom(c1);
            query1.addAtom(c2);
            query1.addBond(new OrderQueryBond(c1, c2, CDKConstants.BONDORDER_SINGLE));
            assertTrue(UniversalIsomorphismTester.isSubgraph(c, query1));
            QueryAtomContainer query = new QueryAtomContainer();
            query.addAtom(c1);
            query.addAtom(c2);
            query.addBond(new AnyOrderQueryBond(c1, c2, CDKConstants.BONDORDER_SINGLE));
            assertTrue(UniversalIsomorphismTester.isSubgraph(c, query));
        } catch (CDKException exception) {
            fail(exception.getMessage());
        }
    }
}
