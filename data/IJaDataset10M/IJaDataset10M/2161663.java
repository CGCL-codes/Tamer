package com.elitost.utils;

/**
 * IFP-Group <br>
 * Infrastructure Project <br>
 * <p>
 * GeometryUtils:
 * <p>
 * Created on 21 janv. 2004
 * 
 * @author rahon
 */
public final class GeometryUtils {

    private GeometryUtils() {
    }

    /**
    * Compute z values at node from z values at cell center 2D function
    * 
    * @param zc ni*nj z values at cell center
    * @param ni number of cell in X direction
    * @param nj number of cell in Y direction
    * @return z at node (ni+1)*(nj+1) values
    */
    public static double[][] compute2DNodesFromCells(double[][] zc, int ni, int nj) {
        double[][] zn = new double[nj + 1][ni + 1];
        int o2;
        int o3;
        int o4;
        zn[0][0] = zc[0][0];
        zn[0][ni] = zc[0][ni - 1];
        zn[nj][0] = zc[nj - 1][0];
        zn[nj][ni] = zc[nj - 1][ni - 1];
        for (int i = 1; i < ni; i++) {
            zn[0][i] = 0.5f * (zc[0][i - 1] + zc[0][i]);
            zn[nj][i] = 0.5f * (zc[nj - 1][i - 1] + zc[nj - 1][i]);
        }
        for (int j = 1; j < nj; j++) {
            zn[j][0] = 0.5f * (zc[j - 1][0] + zc[j][0]);
            zn[j][ni] = 0.5f * (zc[j - 1][ni - 1] + zc[j][ni - 1]);
        }
        for (int j = 1; j < nj; j++) {
            for (int i = 1; i < ni; i++) {
                zn[j][i] = 0.25f * (zc[j - 1][i - 1] + zc[j][i - 1] + zc[j - 1][i] + zc[j][i]);
            }
        }
        return zn;
    }

    public static double[][][] compute3DCellsCenterFromTop(double[][] zt, double[] dz, int ni, int nj, int nk) {
        double[][][] zc = new double[nk][nj][ni];
        for (int j = 0; j < nj; j++) {
            for (int i = 0; i < ni; i++) {
                zc[0][j][i] = zt[j][i] - (0.5f * dz[0]);
            }
        }
        for (int k = 1; k < nk; k++) {
            for (int j = 0; j < nj; j++) {
                for (int i = 0; i < ni; i++) {
                    zc[k][j][i] = zc[k - 1][j][i] - 0.5f * (dz[k] + dz[k - 1]);
                }
            }
        }
        return zc;
    }

    /**
    * Compute 3D mesh cells center from top (2D values) and thickness of each
    * cell
    * 
    * @param zt z values of top of grid
    * @param dz thickness of all cells
    * @param ni number of cells in X direction
    * @param nj number of cells in Y direction
    * @param nk number of layers
    * @return Z coordinate of each cell
    */
    public static double[][][] compute3DCellsCenterFromTop(double[][] zt, double[][][] dz, int ni, int nj, int nk) {
        double[][][] zc = new double[nk][nj][ni];
        for (int j = 0; j < nj; j++) {
            for (int i = 0; i < ni; i++) {
                zc[0][j][i] = zt[j][i] - (0.5f * dz[0][j][i]);
            }
        }
        for (int k = 1; k < nk; k++) {
            for (int j = 0; j < nj; j++) {
                for (int i = 0; i < ni; i++) {
                    zc[k][j][i] = zc[k - 1][j][i] - 0.5f * (dz[k][j][i] + dz[k - 1][j][i]);
                }
            }
        }
        return zc;
    }

    /**
    * Compute z values at node from z values at cell center 3D function
    * 
    * @param zc ni*nj*nk z values at cell center
    * @param ni number of cell in X direction
    * @param nj number of cell in Y direction
    * @param nk number of cell in Z direction
    * @return z at node (ni+1)*(nj+1)*(nk+1) values
    * @return
    */
    public static double[][][] compute3DNodesFromCells(double[][][] zc, int ni, int nj, int nk) {
        double[][][] zn = new double[nk + 1][nj + 1][ni + 1];
        zn[0][0][0] = zc[0][0][0];
        zn[0][0][ni] = zc[0][0][ni - 1];
        zn[0][nj][0] = zc[0][nj - 1][0];
        zn[0][nj][ni] = zc[0][nj - 1][ni - 1];
        zn[nk][0][0] = zc[nk][0][0];
        zn[nk][0][ni] = zc[nk][0][ni - 1];
        zn[nk][nj][0] = zc[nk][nj - 1][0];
        zn[nk][nj][ni] = zc[nk][nj - 1][ni - 1];
        for (int i = 1; i < ni; i++) {
            zn[0][0][i] = 0.5f * (zc[0][0][i] + zc[0][0][i - 1]);
            zn[0][nj][i] = 0.5f * (zc[0][nj - 1][i] + zc[0][nj - 1][i - 1]);
            zn[nk][0][i] = 0.5f * (zc[nk - 1][0][i] + zc[nk - 1][0][i - 1]);
            zn[nk][nj][i] = 0.5f * (zc[nk - 1][nj - 1][i] + zc[nk - 1][nj - 1][i - 1]);
        }
        for (int j = 1; j < nj; j++) {
            zn[0][j][0] = 0.5f * (zc[0][j][0] + zc[0][j - 1][0]);
            zn[0][j][ni] = 0.5f * (zc[0][j][ni - 1] + zc[0][j - 1][ni - 1]);
            zn[nk][j][0] = 0.5f * (zc[nk - 1][j][0] + zc[nk - 1][j - 1][0]);
            zn[nk][j][ni] = 0.5f * (zc[nk - 1][j][ni - 1] + zc[nk - 1][j - 1][ni - 1]);
        }
        for (int k = 1; k < nk; k++) {
            zn[k][0][0] = 0.5f * (zc[k][k][0] + zc[k - 1][0][0]);
            zn[k][0][ni] = 0.5f * (zc[k][0][ni - 1] + zc[k - 1][0][ni - 1]);
            zn[k][nj][0] = 0.5f * (zc[k][nj - 1][0] + zc[k - 1][nj - 1][0]);
            zn[k][nj][ni] = 0.5f * (zc[k][nj - 1][ni - 1] + zc[k - 1][nj - 1][ni - 1]);
        }
        for (int j = 1; j < nj; j++) {
            for (int i = 1; i < ni; i++) {
                zn[0][j][i] = 0.25f * (zc[0][j - 1][i - 1] + zc[0][j][i - 1] + zc[0][j - 1][i] + zc[0][j][i]);
                zn[nk][j][i] = 0.25f * (zc[nk - 1][j - 1][i - 1] + zc[nk - 1][j][i - 1] + zc[nk - 1][j - 1][i] + zc[nk - 1][j][i]);
            }
        }
        for (int k = 1; k < nk; k++) {
            for (int i = 1; i < ni; i++) {
                zn[k][0][i] = 0.25f * (zc[k - 1][0][i - 1] + zc[k][0][i - 1] + zc[k - 1][0][i] + zc[k][0][i]);
                zn[k][nj][i] = 0.25f * (zc[k - 1][nj - 1][i - 1] + zc[k][nj - 1][i - 1] + zc[k - 1][nj - 1][i] + zc[k][nj - 1][i]);
            }
        }
        for (int k = 1; k < nj; k++) {
            for (int j = 1; j < nj; j++) {
                zn[k][j][0] = 0.25f * (zc[k - 1][j - 1][0] + zc[k][j - 1][0] + zc[k - 1][j][0] + zc[k][j][0]);
                zn[k][j][ni] = 0.25f * (zc[k - 1][j - 1][ni - 1] + zc[k][j - 1][ni - 1] + zc[k - 1][j][ni - 1] + zc[k][j][ni - 1]);
            }
        }
        for (int k = 1; k < nk; k++) {
            for (int j = 1; j < nj; j++) {
                for (int i = 1; i < ni; i++) {
                    zn[k][j][i] = 0.125f * (zc[k][j][i] + zc[k][j][i - 1] + zc[k][j - 1][i] + zc[k][j - 1][i - 1] + zc[k - 1][j][i] + zc[k - 1][j][i - 1] + zc[k - 1][j - 1][i] + zc[k - 1][j - 1][i - 1]);
                }
            }
        }
        return zn;
    }

    /**
    * Compute structured indices (i,j,k) for element idx in a structured volume
    * with dimensions (ni,nj,nk) global index value is idx = (k * nj * ni + j *
    * ni + i)
    * 
    * @param idx globalIndex
    * @param ni structured volume dimensions
    * @param nj
    * @param nk
    * @param ijk int[3] to be filled with (i,j,k) indices
    */
    public static void getIndex(int idx, int ni, int nj, int nk, int[] ijk) {
        if ((idx >= 0) && (idx < (ni * nj * nk))) {
            ijk[2] = idx / (nj * ni);
            idx = idx - ijk[2] * (nj * ni);
            ijk[1] = idx / ni;
            ijk[0] = idx - ijk[1] * ni;
            if ((ijk[0] < 0) || (ijk[0] >= ni) || (ijk[1] < 0) || (ijk[1] >= nj) || (ijk[2] < 0) || (ijk[2] >= nk)) {
                throw new IllegalArgumentException("Inconsistent result");
            }
        } else {
            throw new IllegalArgumentException(idx + " is not a valid index");
        }
    }

    /**
    * Compute global index for element (i,j,k) in a structured volume with
    * dimensions (ni,nj,nk) global index value is (k * nj * ni + j * ni + i)
    * 
    * @param i
    * @param j
    * @param k
    * @param ni
    * @param nj
    * @param nk
    * @return global index
    */
    public static int getNth(int i, int j, int k, int ni, int nj, int nk) {
        int idx;
        if (((i >= 0) && (i < ni)) && ((j >= 0) && (j < nj)) && ((k >= 0) && (k < nk))) {
            idx = (k * nj * ni + j * ni + i);
            if ((idx >= 0) && (idx < (ni * nj * nk))) {
                return idx;
            }
        }
        throw new IllegalArgumentException("(" + i + "," + j + "," + k + ") not in mesh");
    }

    /**
    * @param tab
    * @return
    */
    public static double[] convert1D(double[] tab) {
        double tabOut[] = new double[tab.length];
        int ite = 0;
        for (int i = tab.length - 1; i >= 0; i--) {
            tabOut[ite] = tab[i];
            ite++;
        }
        return tabOut;
    }

    /**
    * @param tab
    * @return
    */
    public static double[][] convert2D(double[][] tab) {
        if (tab != null) {
            double tabOut[][] = new double[tab.length][];
            int ite = 0;
            for (int i = tab.length - 1; i >= 0; i--) {
                tabOut[ite] = tab[i];
                ite++;
            }
            return tabOut;
        }
        return null;
    }

    /**
    * @param tab
    * @return
    */
    public static double[][] convertZtop(double[][] tab) {
        if (tab != null) {
            double tabOut[][] = new double[tab.length][];
            int ite = 0;
            for (int i = tab.length - 1; i >= 0; i--) {
                tabOut[ite] = tab[i];
                for (int j = 0; j < tabOut[ite].length; j++) {
                    tabOut[ite][j] = tabOut[ite][j] * (-1);
                }
                ite++;
            }
            return tabOut;
        }
        return null;
    }

    /**
    * @param tab
    * @return
    */
    public static double[][][] convert3D(double[][][] tab) {
        double tabOut[][][] = new double[tab.length][][];
        int ite = 0;
        for (int k = tab.length - 1; k >= 0; k--) {
            tabOut[ite] = tab[k];
            ite++;
        }
        double tabAnd[][][] = new double[tab.length][tab[0].length][];
        int iter;
        for (int k = 0; k < tabOut.length; k++) {
            iter = 0;
            for (int j = tabOut[k].length - 1; j >= 0; j--) {
                tabAnd[k][iter] = tabOut[k][j];
                iter++;
            }
        }
        return tabAnd;
    }

    /**
    * @param tab
    * @return
    */
    public static double[] convertYs(double[] tab) {
        double tabOut[] = new double[tab.length];
        int ite = 1;
        for (int i = tab.length - 1; i > 0; i--) {
            double tmp = tab[i] - tab[i - 1];
            tabOut[ite] = tabOut[ite - 1] + tmp;
            ite++;
        }
        return tabOut;
    }
}
