package game.data.projection;

import common.RND;

/**
 * User: honza
 * Date: 13.2.2007
 * Time: 20:55:05
 */
class ArtificialClusterGenerator {

    public static double[][] generateClusters(int[] osize, double oinMin, double oinMax, double obtwMin, double obtwMax) {
        int n = 0;
        for (int anOsize : osize) {
            n += anOsize;
        }
        double[][] D = new double[n][n];
        int cnt = osize[0];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (cnt == 0) {
                cnt = osize[++k];
            }
            for (int j = i + 1; j < n; j++) {
                if (j - i < cnt) {
                    D[i][j] = D[j][i] = RND.getDouble(oinMin, oinMax);
                } else {
                    D[i][j] = D[j][i] = RND.getDouble(obtwMin, obtwMax);
                }
            }
            cnt--;
        }
        return D;
    }

    public static int[] generateClasses(int[] osize) {
        int n = 0;
        for (int anOsize : osize) {
            n += anOsize;
        }
        int[] cls = new int[n];
        int c = 0;
        for (int i = 0; i < osize.length; i++) {
            for (int j = 0; j < osize[i]; j++) {
                cls[c++] = i;
            }
        }
        return cls;
    }

    public static double[][] generateClustersTriangular(int[] osize, double oinMin, double oinMax, double obtwMin, double obtwMax) {
        int n = 0;
        for (int anOsize : osize) {
            n += anOsize;
        }
        int total = n - 1;
        double[][] D = new double[total][];
        int rows = osize[0];
        int cnt = rows;
        int k = 0;
        for (int i = 0; i < total; i++) {
            if (cnt == 0) {
                rows = osize[++k];
                cnt = rows;
            }
            D[i] = new double[total - i];
            for (int j = 0; j < total - i; j++) {
                if (j < cnt - 1) {
                    D[i][j] = RND.getDouble(oinMin, oinMax);
                } else {
                    D[i][j] = RND.getDouble(obtwMin, obtwMax);
                }
            }
            cnt--;
        }
        return D;
    }
}
