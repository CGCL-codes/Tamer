package preprocessing.automatic.GUI;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 26-Apr-2010
 * Time: 16:08:59
 * To change this template use File | Settings | File Templates.
 */
public class JetColorMap {

    public static Color[] getJetColorMap(int numColor) {
        int nCol = ((Double) Math.ceil(numColor / 4)).intValue();
        double[] u = new double[nCol * 3];
        for (int i = 0; i < nCol; i++) {
            u[i] = (double) (i + 1) / (double) nCol;
        }
        for (int i = nCol; i < 2 * nCol; i++) {
            u[i] = 1.0;
        }
        for (int i = 2 * nCol; i < 3 * nCol; i++) {
            u[i] = (double) (3 * nCol - (i + 1)) / (double) nCol;
        }
        int[] g = new int[3 * nCol];
        int[] r = new int[3 * nCol];
        int[] b = new int[3 * nCol];
        int tmp = ((Double) Math.ceil(nCol / 2)).intValue();
        for (int i = 0; i < 3 * nCol; i++) {
            g[i] = tmp + i + 1;
            r[i] = g[i] + nCol;
            b[i] = g[i] - nCol;
        }
        double[][] colComponents = new double[3][numColor];
        Color[] colors = new Color[numColor];
        for (int i = 0; i < 3 * nCol; i++) {
            if (r[i] < (numColor)) {
                colComponents[0][r[i]] = u[i];
            }
        }
        for (int i = 0; i < 3 * nCol; i++) {
            if (g[i] < numColor) {
                colComponents[1][g[i]] = u[i];
            }
        }
        for (int i = 0; i < 3 * nCol; i++) {
            if (b[i] >= 0) {
                colComponents[2][b[i]] = u[i];
            }
        }
        for (int i = 0; i < numColor; i++) {
            colors[i] = new Color((float) colComponents[0][i], (float) colComponents[1][i], (float) colComponents[2][i]);
        }
        return colors;
    }

    public static void main(String[] args) {
        Color[] cols = getJetColorMap(63);
        for (int i = 0; i < cols.length; i++) {
            System.out.printf("%d %d %d\n", cols[i].getRed(), cols[i].getGreen(), cols[i].getBlue());
        }
        System.out.printf("%d\n", cols.length);
    }
}
