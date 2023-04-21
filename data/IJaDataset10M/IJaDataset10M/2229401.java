package vv.cms.lcd.calibrate.measured.util;

import java.io.*;
import java.util.*;
import shu.cms.*;
import shu.cms.colorformat.logo.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.lcd.*;
import shu.cms.util.*;
import shu.math.*;
import shu.util.log.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public abstract class MeasuredUtils {

    /**
   * �NpatchList�x�s��filename
   * @param filename String
   * @param patchList List
   * @return LogoFile
   */
    public static final LogoFile saveToLogoFile(String filename, List<Patch> patchList) {
        try {
            LogoFile smoothLogo = LogoFile.getDefaultInstance(patchList, null);
            smoothLogo.save(filename);
            return smoothLogo;
        } catch (IOException ex) {
            Logger.log.error("", ex);
        }
        return null;
    }

    private MeasuredUtils() {
    }

    /**
   * �O�_�Ĥ@��XYZ�̱���targetXYZ (�Ĥ@��XYZ�P�ɤ]�����O���I�?������XYZ)
   * @param targetXYZ CIEXYZ
   * @param aroundXYZ CIEXYZ[]
   * @return boolean
   */
    public static final boolean isFirstNearestXYZInuvPrime(CIEXYZ targetXYZ, CIEXYZ[] aroundXYZ) {
        int size = aroundXYZ.length;
        double[] dists = new double[size];
        for (int x = 0; x < size; x++) {
            double[] delta = getDeltauvPrime(targetXYZ, aroundXYZ[x]);
            dists[x] = Math.sqrt(Maths.sqr(delta[0]) + Maths.sqr(delta[1]));
        }
        return Maths.minIndex(dists) == 0;
    }

    public static final boolean isFirstNearestXYZInDeltaE00(CIEXYZ targetXYZ, CIEXYZ[] aroundXYZ, CIEXYZ white, boolean weighting211) {
        int size = aroundXYZ.length;
        double[] dists = new double[size];
        for (int x = 0; x < size; x++) {
            double[] delta = null;
            if (weighting211) {
                delta = getDeltaE00Weighting211(targetXYZ, aroundXYZ[x], white);
            } else {
                delta = getDeltaE00(targetXYZ, aroundXYZ[x], white);
            }
            dists[x] = delta[0];
        }
        return Maths.minIndex(dists) == 0;
    }

    public static final boolean isFirstNearestXYZInDeltaEuv(CIEXYZ targetXYZ, CIEXYZ[] aroundXYZ, CIEXYZ white) {
        int size = aroundXYZ.length;
        double[] dists = new double[size];
        for (int x = 0; x < size; x++) {
            double[] delta = null;
            delta = getDeltaEuv(targetXYZ, aroundXYZ[x], white);
            dists[x] = delta[0];
        }
        return Maths.minIndex(dists) == 0;
    }

    public static final boolean isFirstNearestXYZInDeltaE(CIEXYZ targetXYZ, CIEXYZ[] aroundXYZ, CIEXYZ white) {
        int size = aroundXYZ.length;
        double[] dists = new double[size];
        for (int x = 0; x < size; x++) {
            double[] delta = null;
            delta = getDeltaE(targetXYZ, aroundXYZ[x], white);
            dists[x] = delta[0];
        }
        return Maths.minIndex(dists) == 0;
    }

    public static final double[] getDeltaE00(CIEXYZ center, CIEXYZ XYZ, CIEXYZ white) {
        DeltaE de = new DeltaE(center, XYZ, white, true);
        return new double[] { de.getCIE2000DeltaE() };
    }

    private static final double[] getDeltaE(CIEXYZ center, CIEXYZ XYZ, CIEXYZ white) {
        DeltaE de = new DeltaE(center, XYZ, white, true);
        return new double[] { de.getCIEDeltaE() };
    }

    private static final double[] getDeltaEuv(CIEXYZ center, CIEXYZ XYZ, CIEXYZ white) {
        double de = DeltaE.CIEDeltaEuv(center, XYZ, white, true);
        return new double[] { de };
    }

    private static final double[] getDeltaE00Weighting211(CIEXYZ center, CIEXYZ XYZ, CIEXYZ white) {
        DeltaE de = new DeltaE(center, XYZ, white, true);
        return new double[] { de.getCIE2000DeltaE(2, 1, 1) };
    }

    public static final double[] getDeltauvPrime(CIEXYZ center, CIEXYZ XYZ) {
        CIExyY centerxyY = new CIExyY(center);
        CIExyY xyY = new CIExyY(XYZ);
        return xyY.getDeltauvPrime(centerxyY);
    }

    /**
   * testRGB�O�_���bsquareRGBArray��t�W
   * @param squareRGBArray RGB[]
   * @param testRGB RGB
   * @param xAxis Channel
   * @param yAxis Channel
   * @return boolean
   */
    public static final boolean isOnEdge(RGB[] squareRGBArray, RGB testRGB, RGBBase.Channel xAxis, RGBBase.Channel yAxis) {
        int size = squareRGBArray.length;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (int x = 0; x < size; x++) {
            RGB rgb = squareRGBArray[x];
            double xValue = rgb.getValue(xAxis);
            double yValue = rgb.getValue(yAxis);
            maxX = Math.max(maxX, xValue);
            maxY = Math.max(maxY, yValue);
            minX = Math.min(minX, yValue);
            minY = Math.min(minY, yValue);
        }
        return (testRGB.getValue(xAxis) == maxX || testRGB.getValue(xAxis) == minX || testRGB.getValue(yAxis) == maxY || testRGB.getValue(yAxis) == maxY);
    }

    /**
   * �q�ե����W�D�ӶǦ^ramp Target, �p�G���ݭnramp Target, �h�Ǧ^FiveColor��Target
   * @param channel Channel
   * @param ramp256Measure boolean �O�_�n�q��ramp�ɨ�
   * @param withWhite boolean �O�_�n�a��(�p�Gchannel����, �h���ѼƵL��)
   * @return Number
   */
    public static final LCDTargetBase.Number getMeasureNumber(RGBBase.Channel channel, boolean ramp256Measure, boolean withWhite) {
        if (ramp256Measure) {
            switch(channel) {
                case R:
                    return withWhite ? LCDTargetBase.Number.Ramp256R_W : LCDTargetBase.Number.Ramp256R;
                case G:
                    return withWhite ? LCDTargetBase.Number.Ramp256G_W : LCDTargetBase.Number.Ramp256G;
                case B:
                    return withWhite ? LCDTargetBase.Number.Ramp256B_W : LCDTargetBase.Number.Ramp256B;
                case W:
                    return LCDTargetBase.Number.Ramp256W;
                default:
                    return null;
            }
        } else {
            return LCDTargetBase.Number.BlackAndWhite;
        }
    }

    /**
   * �NrgbArray�L�o�X�ȶ�Channel����
   * @param rgbArray RGB[]
   * @param measureChannel Channel
   * @return RGB[]
   */
    public static final RGB[] getMeasureRGBArray(final RGB[] rgbArray, RGBBase.Channel measureChannel) {
        int size = rgbArray.length;
        RGB[] measureRGBArray = RGBArray.deepClone(rgbArray);
        for (int x = 0; x < size; x++) {
            RGB rgb = measureRGBArray[x];
            if (measureChannel != RGBBase.Channel.W) {
                rgb.reserveValue(measureChannel);
            }
            measureRGBArray[x] = rgb;
        }
        return measureRGBArray;
    }

    public static final void inverse(boolean[] boolArray) {
        int size = boolArray.length;
        for (int x = 0; x < size; x++) {
            boolArray[x] = !boolArray[x];
        }
    }
}
