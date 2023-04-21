package br.ufsc.inf.guiga.media.codec.video.h264.vcl.mode.decision;

import br.ufsc.inf.guiga.media.codec.video.h264.vcl.algorithm.Transform;

/**
 * SA(T)D, the Sum of Absolute Differences of the Transformed residual data.
 * 
 * @author Guilherme Ferreira <guiga@inf.ufsc.br>
 */
public class SATD implements DistortionMetric {

    private Transform transform;

    public SATD(Transform transform) {
        this.transform = transform;
    }

    /**
     * <p>
     * <b>Implements:</b>
     * 
     * <pre>
     * int distortion_hadamard(imgpel **img_org, imgpel pred_img[16][16])
     * </pre>
     */
    public int getDistortion16x16(int[][] orig, int[][] pred) {
        int satd = 0;
        int ii, jj, i, j;
        int[][][][] M0 = new int[4][4][4][4];
        int[][] M4 = new int[4][4];
        int[][] M7 = new int[4][4];
        for (j = 0; j < 16; j++) {
            for (i = 0; i < 16; i++) {
                M0[j >> 2][i >> 2][j & 0x03][i & 0x03] = orig[j][i] - pred[j][i];
            }
        }
        for (jj = 0; jj < 4; jj++) {
            for (ii = 0; ii < 4; ii++) {
                M7 = M0[jj][ii];
                transform.hadamard4x4(M7, M7);
                for (j = 0; j < 4; j++) {
                    for (i = 0; i < 4; i++) {
                        if ((i + j) != 0) satd += Math.abs(M7[j][i]);
                    }
                }
            }
        }
        for (j = 0; j < 4; j++) {
            for (i = 0; i < 4; i++) M4[j][i] = (M0[j][i][0][0] >> 1);
        }
        transform.hadamard4x4(M4, M4);
        for (j = 0; j < 4; j++) {
            for (i = 0; i < 4; i++) {
                satd += Math.abs(M4[j][i]);
            }
        }
        return satd;
    }

    /**
     * Calculate 4x4 Hadamard-Transformed SAD
     * <p>
     * <b>Implements:</b>
     * 
     * <pre>
     * int HadamardSAD4x4 (int* diff)
     * </pre>
     */
    public int getDistortion4x4(int[][] orig, int[][] pred, int pos_y, int pos_x) {
        int satd = 0;
        int[][] diff = new int[4][4];
        int[] d = new int[16];
        int[] m = new int[16];
        for (int j = 0; j < 4; j++) {
            int jj = pos_y + j;
            for (int i = 0; i < 4; i++) {
                int ii = pos_x + i;
                diff[j][i] = orig[jj][ii] - pred[jj][ii];
            }
        }
        m[0] = diff[0][0] + diff[3][0];
        m[1] = diff[0][1] + diff[3][1];
        m[2] = diff[0][2] + diff[3][2];
        m[3] = diff[0][3] + diff[3][3];
        m[4] = diff[1][0] + diff[2][0];
        m[5] = diff[1][1] + diff[2][1];
        m[6] = diff[1][2] + diff[2][2];
        m[7] = diff[1][3] + diff[2][3];
        m[8] = diff[1][0] - diff[2][0];
        m[9] = diff[1][1] - diff[2][1];
        m[10] = diff[1][2] - diff[2][2];
        m[11] = diff[1][3] - diff[2][3];
        m[12] = diff[0][0] - diff[3][0];
        m[13] = diff[0][1] - diff[3][1];
        m[14] = diff[0][2] - diff[3][2];
        m[15] = diff[0][3] - diff[3][3];
        d[0] = m[0] + m[4];
        d[1] = m[1] + m[5];
        d[2] = m[2] + m[6];
        d[3] = m[3] + m[7];
        d[4] = m[8] + m[12];
        d[5] = m[9] + m[13];
        d[6] = m[10] + m[14];
        d[7] = m[11] + m[15];
        d[8] = m[0] - m[4];
        d[9] = m[1] - m[5];
        d[10] = m[2] - m[6];
        d[11] = m[3] - m[7];
        d[12] = m[12] - m[8];
        d[13] = m[13] - m[9];
        d[14] = m[14] - m[10];
        d[15] = m[15] - m[11];
        m[0] = d[0] + d[3];
        m[1] = d[1] + d[2];
        m[2] = d[1] - d[2];
        m[3] = d[0] - d[3];
        m[4] = d[4] + d[7];
        m[5] = d[5] + d[6];
        m[6] = d[5] - d[6];
        m[7] = d[4] - d[7];
        m[8] = d[8] + d[11];
        m[9] = d[9] + d[10];
        m[10] = d[9] - d[10];
        m[11] = d[8] - d[11];
        m[12] = d[12] + d[15];
        m[13] = d[13] + d[14];
        m[14] = d[13] - d[14];
        m[15] = d[12] - d[15];
        d[0] = m[0] + m[1];
        d[1] = m[0] - m[1];
        d[2] = m[2] + m[3];
        d[3] = m[3] - m[2];
        d[4] = m[4] + m[5];
        d[5] = m[4] - m[5];
        d[6] = m[6] + m[7];
        d[7] = m[7] - m[6];
        d[8] = m[8] + m[9];
        d[9] = m[8] - m[9];
        d[10] = m[10] + m[11];
        d[11] = m[11] - m[10];
        d[12] = m[12] + m[13];
        d[13] = m[12] - m[13];
        d[14] = m[14] + m[15];
        d[15] = m[15] - m[14];
        for (int k = 0; k < 16; ++k) {
            satd += Math.abs(d[k]);
        }
        return ((satd + 1) >> 1);
    }
}
