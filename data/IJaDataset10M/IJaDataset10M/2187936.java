package shu.cms.devicemodel.lcd.xtalk;

import shu.cms.colorspace.depend.*;
import shu.math.array.*;
import shu.math.lut.*;
import shu.util.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 * xtalk��������, �ΨӰO��xtalk���v�T
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class XTalkEliminator {

    private static enum Mode {

        ThreeLut, OneLut
    }

    private Mode mode;

    private Interpolation2DLUT rLut;

    private Interpolation2DLUT gLut;

    private Interpolation2DLUT bLut;

    TouchedLut touchedLut;

    /**
     *
     * @param adjacentValues double[] �Ҧ��F����
     * @param selfValues double[] �Ҧ��ۨ���
     * @param rCorrectionValues double[][] r�ץ���
     * @param gCorrectionValues double[][] g�ץ���
     * @param bCorrectionValues double[][] b�ץ���
     */
    protected XTalkEliminator(double[] adjacentValues, double[] selfValues, double[][] rCorrectionValues, double[][] gCorrectionValues, double[][] bCorrectionValues) {
        this(adjacentValues, selfValues, rCorrectionValues, gCorrectionValues, bCorrectionValues, positiveFixLUT);
    }

    protected XTalkEliminator(double[] adjacentValues, double[] selfValues, double[][] rCorrectionValues, double[][] gCorrectionValues, double[][] bCorrectionValues, boolean fix) {
        this(adjacentValues, selfValues, rCorrectionValues, adjacentValues, selfValues, gCorrectionValues, adjacentValues, selfValues, bCorrectionValues, fix);
    }

    double[][][] correctLut;

    /**
     * �ϥ�RGB��Ӫ?�Φ�
     * @param rAdjacentValues double[]
     * @param rSelfValues double[]
     * @param rCorrectionValues double[][]
     * @param gAdjacentValues double[]
     * @param gSelfValues double[]
     * @param gCorrectionValues double[][]
     * @param bAdjacentValues double[]
     * @param bSelfValues double[]
     * @param bCorrectionValues double[][]
     * @param fix boolean
     */
    protected XTalkEliminator(double[] rAdjacentValues, double[] rSelfValues, double[][] rCorrectionValues, double[] gAdjacentValues, double[] gSelfValues, double[][] gCorrectionValues, double[] bAdjacentValues, double[] bSelfValues, double[][] bCorrectionValues, boolean fix) {
        this.mode = Mode.ThreeLut;
        this.correctLut = new double[][][] { rCorrectionValues, gCorrectionValues, bCorrectionValues };
        rLut = new Interpolation2DLUT(rAdjacentValues, rSelfValues, rCorrectionValues, interpAlgo);
        gLut = new Interpolation2DLUT(gAdjacentValues, gSelfValues, gCorrectionValues, interpAlgo);
        bLut = new Interpolation2DLUT(bAdjacentValues, bSelfValues, bCorrectionValues, interpAlgo);
        fixLUT(rCorrectionValues, fix, false);
        fixLUT(gCorrectionValues, fix, false);
        fixLUT(bCorrectionValues, fix, false);
        touchedLut = new TouchedLut(rAdjacentValues, rSelfValues, gAdjacentValues, gSelfValues, bAdjacentValues, bSelfValues);
    }

    static class TouchedLut {

        private int[][] rLut;

        private int[][] gLut;

        private int[][] bLut;

        private double[][] rValues;

        private double[][] gValues;

        private double[][] bValues;

        private TouchedLut(double[] rAdjacentValues, double[] rSelfValues, double[] gAdjacentValues, double[] gSelfValues, double[] bAdjacentValues, double[] bSelfValues) {
            int rWidth = rAdjacentValues.length;
            int rHeight = rSelfValues.length;
            int gWidth = gAdjacentValues.length;
            int gHeight = gSelfValues.length;
            int bWidth = bAdjacentValues.length;
            int bHeight = bSelfValues.length;
            rLut = new int[rWidth][rHeight];
            gLut = new int[gWidth][gHeight];
            bLut = new int[bWidth][bHeight];
            rValues = new double[][] { rAdjacentValues, rSelfValues };
            gValues = new double[][] { gAdjacentValues, gSelfValues };
            bValues = new double[][] { bAdjacentValues, bSelfValues };
        }

        double[][][] getTouchedLUT() {
            double[][][] touchedLut = new double[][][] { IntArray.toDoubleArray(rLut), IntArray.toDoubleArray(gLut), IntArray.toDoubleArray(bLut) };
            return touchedLut;
        }

        private double[][] getValues(RGBBase.Channel selfChannel) {
            switch(selfChannel) {
                case R:
                    return rValues;
                case G:
                    return gValues;
                case B:
                    return bValues;
                default:
                    return null;
            }
        }

        private int[][] getLut(RGBBase.Channel selfChannel) {
            switch(selfChannel) {
                case R:
                    return rLut;
                case G:
                    return gLut;
                case B:
                    return bLut;
                default:
                    return null;
            }
        }

        private void touched(RGBBase.Channel selfChannel, double selfValue, double adjacentValue) {
            double[][] values = getValues(selfChannel);
            int xIndex = Searcher.leftBinarySearch(values[0], adjacentValue);
            int yIndex = Searcher.leftBinarySearch(values[1], selfValue);
            int[][] lut = getLut(selfChannel);
            if (xIndex == -1 || yIndex == -1) {
                System.out.println("");
            }
            lut[xIndex][yIndex]++;
            lut[xIndex + 1][yIndex]++;
            lut[xIndex][yIndex + 1]++;
            lut[xIndex + 1][yIndex + 1]++;
        }
    }

    /**
     * �Ȩϥγ�@��Ӫ?�Φ�
     * @param adjacentValues double[]
     * @param selfValues double[]
     * @param correctionValues double[][]
     * @param fix boolean
     */
    protected XTalkEliminator(double[] adjacentValues, double[] selfValues, double[][] correctionValues, boolean fix) {
        this.mode = Mode.OneLut;
        this.correctLut = new double[][][] { correctionValues };
        rLut = new Interpolation2DLUT(adjacentValues, selfValues, correctionValues, interpAlgo);
        fixLUT(correctionValues, fix, false);
    }

    /**
     * �N��Ӫ�ץ���������
     */
    protected static final boolean positiveFixLUT = true;

    /**
     * �ץ����X�z���ץ���
     * @param values double[][]
     * @param positiveFix boolean �ץ���u�d�U����
     * @param negativeFix boolean �ץ���u�d�U�t��
     */
    protected static final void fixLUT(double[][] values, boolean positiveFix, boolean negativeFix) {
        int height = values.length;
        int width = values[0].length;
        if (!positiveFix && !negativeFix) {
            return;
        }
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (positiveFix) {
                    values[x][y] = values[x][y] < 0 ? 0 : values[x][y];
                }
                if (negativeFix) {
                    values[x][y] = values[x][y] > 0 ? 0 : values[x][y];
                }
            }
        }
    }

    /**
     * linear���G��t���G�̨�
     */
    protected static final Interpolation2DLUT.Algo interpAlgo = Interpolation2DLUT.Algo.BILINEAR;

    public double getCorrectionValue(RGBBase.Channel selfChannel, double selfValue, double adjacentValue) {
        if (this.mode != Mode.ThreeLut) {
            throw new IllegalStateException("this.mode != Mode.ThreeLut");
        }
        if (selfValue == 0 || adjacentValue == 0) {
            return 0;
        }
        touchedLut.touched(selfChannel, selfValue, adjacentValue);
        switch(selfChannel) {
            case R:
                return rLut.getValue(adjacentValue, selfValue);
            case G:
                return gLut.getValue(adjacentValue, selfValue);
            case B:
                return bLut.getValue(adjacentValue, selfValue);
            default:
                throw new IllegalArgumentException("selfChannel != R/G/B");
        }
    }

    public double getCorrectionValue(double selfValue, double adjacentValue) {
        if (this.mode != Mode.OneLut) {
            throw new IllegalStateException("this.mode != Mode.OneLut");
        }
        if (selfValue == 0 || adjacentValue == 0) {
            return 0;
        }
        return rLut.getValue(adjacentValue, selfValue);
    }
}
