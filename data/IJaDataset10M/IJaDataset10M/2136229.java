package test.esra.forcefield;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import junit.framework.TestCase;
import esra.forcefield.Bonds;
import esra.io.g96.in.*;

public class BondsTest extends TestCase {

    final double TOLERANCE = 1E-8;

    public void testBonds() {
    }

    public void testCopies() {
    }

    public void testSubset() throws IOException {
        final InTopoG96.Topology topo = InTopoG96.readTopology("testData/test.top");
        final int numParticles = topo.particles.number;
        final Bonds bonds = topo.system.bond.join(topo.system.bondh, 0);
        final int testNumAtoms = numParticles / 2;
        ArrayList<Integer> testAtomsList = new ArrayList<Integer>();
        Random rand = new Random();
        for (int ii = 0; ii < testNumAtoms; ii++) {
            Integer jj;
            while (testAtomsList.contains(jj = new Integer(rand.nextInt(numParticles)))) {
            }
            testAtomsList.add(jj);
        }
        int[] testAtomsArray = new int[testNumAtoms];
        for (int ii = 0; ii < testNumAtoms; ii++) testAtomsArray[ii] = ((Integer) testAtomsList.get(ii)).intValue();
        final Bonds newBonds = bonds.subset(testAtomsArray, numParticles);
        ArrayList expectedBonds = new ArrayList();
        for (int bb = 0; bb < bonds.number; bb++) {
            Integer II = new Integer(bonds.I[bb]);
            Integer JJ = new Integer(bonds.J[bb]);
            if (testAtomsList.contains(II) && testAtomsList.contains(JJ)) {
                expectedBonds.add(new Integer(bb));
            }
        }
        assertEquals(expectedBonds.size(), newBonds.number);
        for (int nb = 0; nb < expectedBonds.size(); nb++) {
            final int ob = ((Integer) expectedBonds.get(nb)).intValue();
            final int io = bonds.I[ob];
            final int in = newBonds.I[nb];
            assertEquals(io, testAtomsArray[in]);
            final int jo = bonds.J[ob];
            final int jn = newBonds.J[nb];
            assertEquals(jo, testAtomsArray[jn]);
            assertEquals(bonds.Kb[ob], newBonds.Kb[nb], TOLERANCE);
            assertEquals(bonds.b0[ob], newBonds.b0[nb], TOLERANCE);
            assertEquals(true, bonds.bt[ob].equals(newBonds.bt[nb]));
        }
    }

    public void testJoin() {
    }
}
