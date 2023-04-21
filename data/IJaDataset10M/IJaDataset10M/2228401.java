package org.openscience.cdk.tools.diff;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.tools.diff.tree.BondOrderDifference;
import org.openscience.cdk.tools.diff.tree.ChemObjectDifference;
import org.openscience.cdk.tools.diff.tree.IDifference;
import org.openscience.cdk.tools.diff.tree.IDifferenceList;
import org.openscience.cdk.tools.diff.tree.IntegerDifference;

/**
 * Compares two {@link IBond} classes.
 * 
 * @author     egonw
 * @cdk.module diff
 */
@TestClass("org.openscience.cdk.tools.diff.BondDiffTest")
public class BondDiff {

    /**
     * Overwrite the default public constructor because this class is not
     * supposed to be instantiated.
     */
    private BondDiff() {
    }

    @TestMethod("testMatchAgainstItself,testDiff")
    public static String diff(IChemObject first, IChemObject second) {
        IDifference diff = difference(first, second);
        if (diff == null) {
            return "";
        } else {
            return diff.toString();
        }
    }

    @TestMethod("testDifference")
    public static IDifference difference(IChemObject first, IChemObject second) {
        if (!(first instanceof IBond && second instanceof IBond)) {
            return null;
        }
        IBond firstB = (IBond) first;
        IBond secondB = (IBond) second;
        IDifferenceList totalDiff = new ChemObjectDifference("BondDiff");
        totalDiff.addChild(BondOrderDifference.construct("order", firstB.getOrder(), secondB.getOrder()));
        totalDiff.addChild(IntegerDifference.construct("atomCount", firstB.getAtomCount(), secondB.getAtomCount()));
        if (firstB.getAtomCount() == secondB.getAtomCount()) {
            for (int i = 0; i < firstB.getAtomCount(); i++) {
                totalDiff.addChild(AtomDiff.difference(firstB.getAtom(i), secondB.getAtom(i)));
            }
        }
        totalDiff.addChild(ElectronContainerDiff.difference(first, second));
        if (totalDiff.childCount() > 0) {
            return totalDiff;
        } else {
            return null;
        }
    }
}
