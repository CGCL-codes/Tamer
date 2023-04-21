package net.sf.cdktools.util;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.tools.HydrogenAdder;

/**
 * A collection of static utility methods for working with the CDK.
 * 
 * @author Richard Apodaca
 */
public class CDKKit {

    private static HydrogenAdder hAdder = new HydrogenAdder();

    /**
   * This class should not be instantiated. Use its static methods instead.
   */
    private CDKKit() {
    }

    /**
   * Returns <code>true</code> if <code>input</code> matches <code>target</code> in the
   * exact structure sense.
   * 
   * @param input the input AtomContainer
   * @param target the target AtomContainer
   * @return <code>true</code> if <code>input</code> matches <code>target</code> in the
   * exact structure sense, or <code>false</code> otherwise
   * @throws RuntimeException if UniversalIsomorphismTester throws a CDKException
   */
    public static boolean exactStructureMatch(AtomContainer input, AtomContainer target) {
        boolean result = false;
        try {
            result = UniversalIsomorphismTester.isIsomorph(input, target);
        } catch (CDKException e) {
            throw new RuntimeException("There was a problem with isomorphism checking.", e);
        }
        return result;
    }

    /**
   * Applies implicit hydrogen atoms to <code>ac</code> using <code>HydrogenAdder</code>.
   * 
   * @param ac the AtomContainer for which to add implicity hydrogen atoms
   */
    public static void addImplicitHydrogens(AtomContainer ac) {
        try {
            hAdder.addImplicitHydrogensToSatisfyValency(ac);
        } catch (CDKException e) {
            throw new RuntimeException("There was a problem with adding implicit hydrogen atoms.", e);
        }
    }

    /**
   * Delivers an overly-simplistic printout of the properties of <code>ac</code> to 
   * </code>System.out</code>.
   * 
   * @param ac the AtomContainer to print out
   */
    public static void printAtomContainer(AtomContainer ac) {
        System.out.println("Molecule:");
        for (int i = 0; i < ac.getAtomCount(); i++) {
            Atom atom = ac.getAtomAt(i);
            System.out.println("Atom[" + i + "]: " + atom.getSymbol() + " " + atom.getHydrogenCount() + "H " + atom.getFormalCharge() + " FC");
        }
        for (int i = 0; i < ac.getBondCount(); i++) {
            Bond bond = ac.getBondAt(i);
            int source = ac.getAtomNumber(bond.getAtomAt(0));
            int target = ac.getAtomNumber(bond.getAtomAt(1));
            System.out.println("Bond[" + i + "]: " + source + "-" + target + " " + bond.getOrder());
        }
    }
}
