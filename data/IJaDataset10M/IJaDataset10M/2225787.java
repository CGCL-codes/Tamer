package esra.energy.nonbonded;

import esra.core.Pairlist;
import esra.core.Pairlist.GridToParticleMap;
import esra.core.pbc.PeriodicBoundary;
import esra.energy.InteractionFunction;
import esra.math.BLA;

/**
 * @author vincent, winger
 *
 */
public class LennardJones extends InteractionFunction {

    /**
	 * 
	 * Lennard-Jones interaction energy.
	 * 
	 * note: assumes that c6 and c12 of two interacting atoms
	 * is the product of the values for the single atoms.
	 * needs arrays of c6 and c12 values for all atoms.
	 * 
	 * 
	 *
	 * @param pos pos[ii] is the position of particle ii
	 * @param c12 c12[ii] is the corresponding c12-parameter
	 * @param c6  c6[ii] is the corresponding c6-parameter
	 * @param pbc the periodic boundary condition
	 * @param box the box edge lengths
	 * @return the Lennard-Jones interaction energy
	 */
    public static double potentialEnergy(final double[][] pos, final double[] c12, final double[] c6, final PeriodicBoundary pbc, final double[] box, final Pairlist pl) {
        final int numAtoms = pl.nParticles;
        double energy = 0.0;
        for (int ii = 0; ii < numAtoms; ++ii) {
            if (0.0 == c6[ii] && 0.0 == c12[ii]) continue;
            for (int kk = ii + 1; kk < pl.numPairs[ii]; ++kk) {
                final int jj = pl.pairs[ii][kk];
                if (0.0 == c6[jj] && 0.0 == c12[jj]) continue;
                final double dis2 = BLA.norm2(pbc.sco(pos[ii], pos[jj], box));
                final double dis6 = dis2 * dis2 * dis2;
                final double c12ij = c12[ii] * c12[jj];
                final double c6ij = c6[ii] * c6[jj];
                energy += (c12ij / dis6 - c6ij) / dis6;
            }
        }
        return energy;
    }

    /**
	 * Implements Lennard-Jones forces as described in the GROMOS96 manual
	 * Eq. (2.5.6.4). 
	 * 
	 * note: assumes that c6 and c12 of two interacting atoms
	 * is the product of the values for the single atoms
	 * needs arrays of c6 and c12 values for all atoms!
	 * 
	 * @param pos
	 * @param c12
	 * @param c6
	 * @param pbc
	 * @param box
	 * @param pl
	 * @param forces
	 * @return the forces
	 */
    public static double[][] forces(final double[][] pos, final double[] c12, final double[] c6, final PeriodicBoundary pbc, final double[] box, final Pairlist pl, final double[][] forces) {
        final int numAtoms = pl.nParticles;
        double forceValue = 0.0;
        final double[] tmp = new double[3];
        for (int ii = 0; ii < numAtoms; ++ii) {
            if (0.0 == c6[ii] && 0.0 == c12[ii]) continue;
            final int numPairs = pl.numPairs[ii];
            final int[] pairs = pl.pairs[ii];
            for (int kk = 0; kk < numPairs; ++kk) {
                final int jj = pairs[kk];
                if (0.0 == c6[jj] && 0.0 == c12[jj]) continue;
                final double[] sco = pbc.sco(pos[ii], pos[jj], box, tmp);
                final double dis2 = BLA.norm2(sco);
                final double dis4 = dis2 * dis2;
                final double dis6 = dis4 * dis2;
                final double c12ij = c12[ii] * c12[jj];
                final double c6ij = c6[ii] * c6[jj];
                forceValue = (2 * c12ij / dis6 - c6ij) * 6 / (dis4 * dis4);
                for (int dd = 0; dd < 3; dd++) {
                    final double fd = forceValue * sco[dd];
                    forces[ii][dd] -= fd;
                    forces[jj][dd] += fd;
                }
            }
        }
        return forces;
    }

    private double[] _c12;

    private double[] _c6;

    private double _cutoff;

    private PeriodicBoundary _pbc;

    public GridToParticleMap _gpm;

    private Pairlist _pl;

    /**
	 * @param c12
	 * @param c6
	 * @param pbc
	 * @param cutoff
	 */
    public LennardJones(final double[] c12, final double[] c6, final PeriodicBoundary pbc, final double cutoff, final Pairlist pl, final Pairlist.GridToParticleMap gpm) {
        _c12 = c12;
        _c6 = c6;
        _pbc = pbc;
        _cutoff = cutoff;
        _pl = pl;
    }

    /**
	 * @see esra.energy.InteractionFunction#potentialEnergy(double[][], double[])
	 */
    public double potentialEnergy(final double[][] pos, final double box[]) {
        _pl = Pairlist.cubicGridPairlist(pos, box, _cutoff, _pl, _pbc, _gpm);
        return potentialEnergy(pos, _c12, _c6, _pbc, box, _pl);
    }

    /**
	 * @see esra.energy.InteractionFunction#forces(double[][], double[])
	 */
    public double[][] forces(final double[][] pos, final double box[]) {
        _pl = Pairlist.cubicGridPairlist(pos, box, _cutoff, _pl, _pbc, _gpm);
        final double[][] forces = new double[pos.length][3];
        return forces(pos, _c12, _c6, _pbc, box, _pl, forces);
    }
}
