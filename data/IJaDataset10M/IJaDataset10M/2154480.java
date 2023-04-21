package seneca.structgen;

import java.io.*;
import compchem.*;
import java.util.Vector;
import org.apache.log4j.*;

public class SingleRandomStructureGenerator extends StructureGenerator implements java.io.Serializable, Cloneable {

    Atom[] latestResult;

    int[][] maxBondOrders;

    public SingleRandomStructureGenerator() {
        super("SingleRandomGent");
    }

    public void reset() {
    }

    public void execute() {
    }

    public Object getStatus() {
        return null;
    }

    /** Starts the structure generation process. Initializes any essential array 
	    and calls recursive bond generator routine */
    public Atom[] generate() {
        boolean structureFound = false;
        boolean bondFormed;
        int partner, next, max, order, usc, twoCounter;
        nrOfAtoms = atomSet.length;
        for (int f = 0; f < atomSet.length; f++) {
            atomSet[f].reset();
        }
        do {
            iteration++;
            structureFound = false;
            do {
                bondFormed = false;
                for (int f = 0; f < atomSet.length; f++) {
                    yield();
                    if (!atomSet[f].isSaturated()) {
                        partner = getAnotherUnsaturatedNode(f);
                        if (partner != -1) {
                            max = Math.min(atomSet[f].getCurrentMaxBondOrder(), atomSet[partner].getCurrentMaxBondOrder());
                            order = Math.max(1, (int) (Math.random() * max));
                            atomSet[f].formBond(atomSet[partner], order);
                            atomSet[partner].formBond(atomSet[f], order);
                            bondFormed = true;
                        }
                    }
                }
            } while (bondFormed);
            usc = getUnsaturatedCount();
            attic.atomSet = atomSet;
            bondMatrix = DataStructureTools.createConnectionTable(attic.atomSet);
            attic.apsp = spf.computeFloydAPSP(bondMatrix);
            if (usc == 0 && structureConnected(bondMatrix)) {
                return atomSet;
            }
            for (int f = 0; f < atomSet.length; f++) atomSet[f].reset();
        } while (!structureFound);
        return null;
    }

    /** Recursive method that saturates a given node and then calls itself with
	    the next unsaturated node as the argument */
    int getAnotherUnsaturatedNode(int thisOne) {
        int next = (int) (Math.random() * atomSet.length);
        for (int f = next; f < atomSet.length; f++) {
            if (!atomSet[f].isSaturated() && f != thisOne && !atomSet[f].isBondedTo(atomSet[thisOne])) return f;
        }
        for (int f = 0; f < next; f++) {
            if (!atomSet[f].isSaturated() && f != thisOne && !atomSet[f].isBondedTo(atomSet[thisOne])) return f;
        }
        return -1;
    }

    private int getUnsaturatedCount() {
        int count = 0;
        for (int f = 0; f < atomSet.length; f++) {
            if (!atomSet[f].isSaturated()) count++;
        }
        return count;
    }

    private boolean nodeStatusOk(int thisNode) {
        if (atomSet[thisNode].maxBondOrderSum < atomSet[thisNode].HCount + sumOfBonds[thisNode]) return false;
        return true;
    }

    private boolean nodeStatusPerfect(int thisNode) {
        if (atomSet[thisNode].maxBondOrderSum == atomSet[thisNode].HCount + sumOfBonds[thisNode]) return true;
        return false;
    }

    private boolean endOfMatrix(int row, int col) {
        if ((col < nrOfAtoms - 1) || (row < nrOfAtoms - 2)) {
            return false;
        }
        return true;
    }

    private void sortNodes() {
        boolean sorted;
        Atom node;
        for (int f = 0; f < atomSet.length; f++) {
            atomSet[f].number = f;
        }
        do {
            sorted = true;
            for (int f = 0; f < atomSet.length - 1; f++) {
                if (atomSet[f].HCount > atomSet[f + 1].HCount) {
                    node = atomSet[f + 1];
                    atomSet[f + 1] = atomSet[f];
                    atomSet[f] = node;
                    sorted = false;
                }
            }
        } while (!sorted);
        for (int f = 0; f < atomSet.length; f++) {
            atomSet[f].number = f;
        }
    }

    public void setMolecule(Molecule molecule) {
    }

    public long getSpeed() {
        return -1;
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("MyObject can't clone");
        }
        return o;
    }
}
