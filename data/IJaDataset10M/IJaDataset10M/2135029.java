package test.esra.core;

import java.util.Random;
import esra.core.Pairlist;
import esra.core.Pairlist.GridToParticleMap;
import esra.core.pbc.PeriodicBoundary;
import junit.framework.TestCase;

public class PairlistTest extends TestCase {

    public final void testDoubleLoopPairlist() {
        Pairlist pl = new Pairlist(2, 1);
        final PeriodicBoundary pbc = PeriodicBoundary.getPbc("v");
        final double[] box = { 0.0, 0.0, 0.0 };
        final double[][] pos = { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        for (int ii = 0; ii < 10; ii++) {
            pos[1][2] += 0.1;
            pl = Pairlist.doubleLoopPairlist(pos, box, 1.0, pl, pbc);
            assertEquals(1, pl.numPairs[0]);
            assertEquals(0, pl.numPairs[1]);
            assertEquals(1, pl.pairs[0][0]);
        }
        for (int ii = 0; ii < 9; ii++) {
            pos[1][2] += 0.1;
            pl = Pairlist.doubleLoopPairlist(pos, box, 1.0, pl, pbc);
            assertEquals(0, pl.numPairs[0]);
            assertEquals(0, pl.numPairs[1]);
            assertEquals(1, pl.pairs[0][0]);
        }
    }

    public final void testDisjointDoubleLoopPairlist() {
        Pairlist pl = new Pairlist(2, 2);
        final PeriodicBoundary pbc = PeriodicBoundary.getPbc("v");
        final double[] box = { 0.0, 0.0, 0.0 };
        final double[][] posA = { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        final double[][] posB = { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        for (int ii = 0; ii < 10; ii++) {
            posB[1][2] += 0.1;
            pl = Pairlist.disjointDoubleLoopPairlist(posA, posB, box, 1.0, pl, pbc);
            assertEquals(2, pl.numPairs[0]);
            assertEquals(2, pl.numPairs[1]);
            assertEquals(0, pl.pairs[0][0]);
            assertEquals(0, pl.pairs[1][0]);
            assertEquals(1, pl.pairs[0][1]);
            assertEquals(1, pl.pairs[1][1]);
        }
        for (int ii = 0; ii < 9; ii++) {
            posB[1][2] += 0.1;
            pl = Pairlist.disjointDoubleLoopPairlist(posA, posB, box, 1.0, pl, pbc);
            assertEquals(1, pl.numPairs[0]);
            assertEquals(1, pl.numPairs[1]);
            assertEquals(0, pl.pairs[0][0]);
            assertEquals(0, pl.pairs[1][0]);
            assertEquals(1, pl.pairs[0][1]);
            assertEquals(1, pl.pairs[1][1]);
        }
    }

    public final void testCubicGridPairlist() {
        final PeriodicBoundary pbc = PeriodicBoundary.getPbc("r");
        Random rr = new Random();
        final int numAtoms = 1000;
        final double[] box = { 10.0, 10.0, 10.0 };
        final double[][] pos = new double[numAtoms][3];
        for (int ii = 0; ii < numAtoms; ii++) {
            for (int jj = 0; jj < 3; jj++) {
                pos[ii][jj] = box[0] * rr.nextDouble();
            }
        }
        final double cutoff = 1.0;
        Pairlist pla = Pairlist.doubleLoopPairlist(pos, box, cutoff, new Pairlist(numAtoms, numAtoms / 2), pbc);
        Pairlist plb = Pairlist.cubicGridPairlist(pos, box, cutoff, new Pairlist(numAtoms, numAtoms / 2), pbc, new Pairlist.GridToParticleMap(box[0], pos.length, cutoff));
        for (int ii = 0; ii < numAtoms; ii++) {
            assertEquals(pla.numPairs[ii], plb.numPairs[ii]);
            for (int jj = 0; jj < pla.numPairs[ii]; jj++) {
                assertEquals(pla.pairs[ii][jj], plb.pairs[ii][jj]);
            }
        }
    }

    public final void testDisjointCubicGridPairlist() {
        final PeriodicBoundary pbc = PeriodicBoundary.getPbc("r");
        Random rr = new Random();
        final double[] box = { 10.0, 10.0, 10.0 };
        final int numAtoms = 1000;
        final double[][] posA = new double[numAtoms][3];
        final double[][] posB = new double[numAtoms][3];
        for (int ii = 0; ii < numAtoms; ii++) {
            for (int jj = 0; jj < 3; jj++) {
                posA[ii][jj] = box[0] * rr.nextDouble();
                posB[ii][jj] = box[0] * rr.nextDouble();
            }
        }
        final double cutoff = 1.0;
        final Pairlist pla = Pairlist.disjointDoubleLoopPairlist(posA, posB, box, cutoff, new Pairlist(numAtoms, numAtoms / 2), pbc);
        final GridToParticleMap gpm = new GridToParticleMap(box[0], posB.length, cutoff);
        final Pairlist plb = Pairlist.disjointCubicGridPairlist(posA, posB, box, cutoff, new Pairlist(numAtoms, numAtoms / 2), pbc, gpm);
        for (int ii = 0; ii < numAtoms; ii++) {
            assertEquals(pla.numPairs[ii], plb.numPairs[ii]);
            for (int jj = 0; jj < pla.numPairs[ii]; jj++) {
                assertEquals(pla.pairs[ii][jj], plb.pairs[ii][jj]);
            }
        }
    }
}
