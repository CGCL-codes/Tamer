package esra.conformation;

import esra.core.AtomSpecifier;
import esra.core.Particles;
import esra.math.BLA;
import esra.math.Geometry;

/**
 * <h4>DSSP_BP: Determine Secondary Structure of Beta-Peptides.</h4>
 * <p>
 * rather hackish implementation, according to the author.
 * so proceed with care.
 * </p>
 * 
 * @author  Daniel Trzesniak
 */
public class Dssp_BP {

    Particles atoms;

    /**
     * backbone atom indices
     */
    protected static final int C = 0;

    protected static final int CA = 1;

    protected static final int H = 2;

    protected static final int N = 3;

    protected static final int O = 4;

    protected static final int CB = 5;

    /**
     * hydrogen bond indices
     */
    protected static final int donor = 0;

    protected static final int acceptor = 1;

    /**
     * secondary structure codes
     */
    public static final int _10_helix = 0;

    public static final int _14_helix = 1;

    public static final int _18_helix = 2;

    public static final int _22_helix = 3;

    public static final int _8_helix = 4;

    public static final int _12_helix = 5;

    public static final int _16_helix = 6;

    public static final int _20_helix = 7;

    public static final int turnT = 8;

    public static final int unstructeredT = 9;

    /**
     * the backbone atom numbers
     */
    int[][] bbAN;

    int[] Turn;

    int[] Helix;

    public Dssp_BP(Particles atoms) {
        this.atoms = atoms;
        bbAN = findBackBoneAtoms(this.atoms);
    }

    /**
     * this is the main dssp routine.
     * 
     * @param backboneAtomNumbers the matrix of atom numbers of the C, H, N, O atoms
     * @param backbonePositions   the corresponding positions
     * @param residueNumbers      the residue number of each atom (in the system, this is a bit confusing)
     * @return the secondary structure of each residue
     */
    public static int[] calc_str(final int[][] backboneAtomNumbers, final double[][][] backbonePositions, final int[] residueNumbers, final int numRes) {
        final boolean[][] hbondMatrix = findHbonds(backboneAtomNumbers, backbonePositions, residueNumbers, numRes);
        int[] turn_pos_1Res = findTurns(hbondMatrix, 1);
        int[] turn_pos_2Res = findTurns(hbondMatrix, 2);
        int[] turn_pos_3Res = findTurns(hbondMatrix, 3);
        int[] turn_pos_4Res = findTurns(hbondMatrix, 4);
        int[] turn_neg_1Res = findTurns(hbondMatrix, -1);
        int[] turn_neg_2Res = findTurns(hbondMatrix, -2);
        int[] turn_neg_3Res = findTurns(hbondMatrix, -3);
        int[] turn_neg_4Res = findTurns(hbondMatrix, -4);
        int[] turnRes = new int[numRes];
        for (int ii = 0; ii < numRes; ii++) {
            if (turn_pos_1Res[ii] == turnT || turn_pos_2Res[ii] == turnT || turn_pos_3Res[ii] == turnT || turn_pos_4Res[ii] == turnT || turn_neg_1Res[ii] == turnT || turn_neg_2Res[ii] == turnT || turn_neg_3Res[ii] == turnT || turn_neg_4Res[ii] == turnT) turnRes[ii] = turnT; else turnRes[ii] = unstructeredT;
        }
        int[] helix_pos_1Res = findHelices(turn_pos_1Res, 1, _10_helix);
        int[] helix_pos_2Res = findHelices(turn_pos_2Res, 2, _14_helix);
        int[] helix_pos_3Res = findHelices(turn_pos_3Res, 3, _18_helix);
        int[] helix_pos_4Res = findHelices(turn_pos_4Res, 4, _22_helix);
        int[] helix_neg_1Res = findHelices(turn_neg_1Res, -1, _8_helix);
        int[] helix_neg_2Res = findHelices(turn_neg_2Res, -2, _12_helix);
        int[] helix_neg_3Res = findHelices(turn_neg_3Res, -3, _16_helix);
        int[] helix_neg_4Res = findHelices(turn_neg_4Res, -4, _20_helix);
        final int[] dssp = assignUniqueSecondaryStructure(helix_pos_1Res, helix_pos_2Res, helix_pos_3Res, helix_pos_4Res, helix_neg_1Res, helix_neg_2Res, helix_neg_3Res, helix_neg_4Res, turnRes);
        return dssp;
    }

    /**
     * calc_str, the non-static version
     * 
     * @return the secondary structure of each residue
     */
    public int[] calc_str() {
        final double[][][] bbAP = getBackboneCoords(atoms.pos, bbAN);
        return calc_str(bbAN, bbAP, atoms.rnum, atoms.numResidues);
    }

    /**
     * <h4>Find indices of the protein backbone atoms.</h4> 
     * <p>
     * Backbone atoms are defined as having atom names &isin; {C, CA, CB, N, O, H}. 
     * Only one atom type per residue is allowed. There are, however,
     * amino acids where one of the atoms may be missing (i.e. proline does not
     * have an "H"). In this case, a -1 is inserted for the atom number. 
     * </p>
     * @param atoms 	a particles instance
     * @return		the backbone atom numbers. return[C] contains the atom number of the C-atoms etc.
     */
    public static int[][] findBackBoneAtoms(final Particles atoms) {
        final int[][] an = new int[6][atoms.numResidues];
        final String[] atomNames = { "C", "CA", "H", "N", "O", "CB" };
        for (int resNum = 0; resNum < atoms.numResidues; resNum++) {
            for (int aa = C; aa <= CB; aa++) {
                final String as = ":" + (resNum + 1) + ":" + atomNames[aa];
                int[] na = AtomSpecifier.getAtomNumbers(as, atoms);
                if (na.length > 1) throw new RuntimeException("More than one atom of type " + atomNames[aa] + " in residue " + resNum + ". What gives?"); else if (na.length == 0) an[aa][resNum] = -1; else an[aa][resNum] = na[0];
            }
        }
        return an;
    }

    /**
     * <h4>get the coords of the backbone atoms.</h4>
     * 
     * @param pos   	the positions of all atoms
     * @param bban  	the backbone atom numbers. bban[C] contains the indices of all C-atoms, etc.
     * @return		the positions of the backbone atoms. return[C] contains the positions of all C-atoms etc.
     */
    public static double[][][] getBackboneCoords(final double[][] pos, final int[][] bban) {
        final int numAtomTypes = bban.length;
        final int numRes = bban[0].length;
        double[][][] bbap = new double[numAtomTypes][numRes][0];
        for (int ii = C; ii <= O; ii++) {
            for (int resNum = 0; resNum < numRes; resNum++) {
                final int aa = bban[ii][resNum];
                if (aa >= 0) bbap[ii][resNum] = pos[aa]; else bbap[ii][resNum] = null;
            }
        }
        return bbap;
    }

    /**
     * check whether we fulfill conditions of a normal HB
     * 
     * @param ar
     * @param dr
     * @param bbap
     */
    public static boolean isHB(final int ar, final int dr, final double[][][] bbap) {
        final double[] rN = bbap[N][dr];
        final double[] rH = bbap[H][dr];
        final double[] rO = bbap[O][ar];
        final double[] rC = bbap[C][ar];
        if (rN == null || rH == null || rO == null || rC == null) return false;
        final double rHO = BLA.distance(rH, rO);
        final double aNHO = Geometry.angle(rN, rH, rO);
        if (rHO < 0.25 && aNHO > 135.0) return true; else return false;
    }

    /**
     * fill a part of vector stuff with value.
     * 
     * @param stuff
     * @param start
     * @param end
     * @param value
     */
    static void fill(final int[] stuff, final int start, final int end, final int value) {
        for (int ii = start; ii <= end; ii++) stuff[ii] = value;
    }

    /**
     * looking for hbonds
     * 
     * @param bbaNum the backbone atom numbers
     * @param bbaPos the backbone atom positions
     * @param rnum the residue numbers
     * @return a list of [donors, acceptors]
     */
    public static boolean[][] findHbonds(final int[][] bbaNum, final double[][][] bbaPos, final int[] rnum, final int numRes) {
        boolean[][] hbondMatrix = new boolean[numRes][numRes];
        for (int i = 0; i < bbaPos[O].length; ++i) {
            for (int j = 0; j < bbaPos[H].length; ++j) {
                hbondMatrix[i][j] = isHB(i, j, bbaPos);
            }
        }
        return hbondMatrix;
    }

    public static int[] findTurns(final boolean[][] hbondMatrix, final int offset) {
        final int numResidues = hbondMatrix.length;
        int[] turns = BLA.same(numResidues, unstructeredT);
        if (offset > 0) for (int ii = 0; ii < numResidues - offset; ii++) {
            if (hbondMatrix[ii][ii + offset]) fill(turns, ii, ii + offset, turnT);
        } else if (offset < 0) for (int ii = numResidues - 1; ii > -offset - 1; ii--) {
            if (hbondMatrix[ii][ii + offset]) fill(turns, ii, ii + offset, turnT);
        }
        return turns;
    }

    /**
     * <h4>
     * Turns may be parts of helices.
     * </h4>
     * <p>
     * </p>
     * @param turns	the turns
     * @return	the helices
     */
    public static int[] findHelices(final int[] turns, final int offset, final int helixType) {
        final int numResidues = turns.length;
        final int[] helices = BLA.same(numResidues, unstructeredT);
        if (offset > 0) for (int ii = 1; ii < numResidues - offset; ii++) {
            if (turns[ii] == turnT && turns[ii - 1] == turnT && turns[ii + offset] == turnT) {
                fill(helices, ii, ii + offset, helixType);
            }
        } else if (offset < 0) for (int ii = numResidues - 2; ii > -offset - 1; ii--) {
            if (turns[ii] == turnT && turns[ii + 1] == turnT && turns[ii + offset] == turnT) {
                fill(helices, ii, ii + offset, helixType);
            }
        }
        return helices;
    }

    /**
     * <h4>every residue may have just one secondary structure type.</h4>
     * 
     * <p>
     * This is enforced by first assigning each residue with the corresponding
     * highest-priority secondary structure type, and then "scrubbing" the resulting
     * assignment, i.e.
     * <ul>
     * <li>helices shorter than the respective minimal lengths are re-typed as the corresponding
     * turns.</li>
     * </ul> 
     * </p>
     * 
     * @param helix_pos_1Res
     * @param helix_pos_2Res
     * @param helix_pos_3Res
     * @param helix_pos_4Res
     * @param helix_neg_1Res
     * @param helix_neg_2Res
     * @param helix_neg_3Res
     * @param helix_neg_4Res
     * @param turnRes
     * @return an array containing the secondary structure code of each residue
     */
    public static int[] assignUniqueSecondaryStructure(final int[] helix_pos_1Res, final int[] helix_pos_2Res, final int[] helix_pos_3Res, final int[] helix_pos_4Res, final int[] helix_neg_1Res, final int[] helix_neg_2Res, final int[] helix_neg_3Res, final int[] helix_neg_4Res, final int[] turnRes) {
        final int numRes = turnRes.length;
        if (helix_pos_1Res.length != numRes || helix_pos_2Res.length != numRes || helix_pos_3Res.length != numRes || helix_pos_4Res.length != numRes || helix_neg_1Res.length != numRes || helix_neg_2Res.length != numRes || helix_neg_3Res.length != numRes || helix_neg_4Res.length != numRes || turnRes.length != numRes) throw new RuntimeException("All secondary structure arrays must have the same length.");
        final int[][] stuff = { helix_pos_1Res, helix_pos_2Res, helix_pos_3Res, helix_pos_4Res, helix_neg_1Res, helix_neg_2Res, helix_neg_3Res, helix_neg_4Res, turnRes };
        final int[] dssp = BLA.same(numRes, unstructeredT);
        for (int type = 0; type < stuff.length; type++) {
            for (int res = 0; res < numRes; res++) {
                if (dssp[res] > stuff[type][res]) {
                    dssp[res] = stuff[type][res];
                }
            }
        }
        return dssp;
    }
}
