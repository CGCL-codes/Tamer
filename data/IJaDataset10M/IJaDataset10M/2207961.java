package shu.cms.lcd.experiment;

import java.awt.*;
import java.io.*;
import java.util.List;
import jxl.read.biff.*;
import jxl.write.*;
import shu.cms.*;
import shu.cms.colorformat.file.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.devicemodel.lcd.*;
import shu.cms.hvs.*;
import shu.cms.hvs.gradient.*;
import shu.cms.lcd.*;
import shu.cms.plot.*;
import shu.cms.util.*;
import shu.math.lut.*;
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
public class DICOMCalibrator {

    private LCDTarget lcdTarget;

    private MultiMatrixModel model;

    private GradientModel gm;

    public DICOMCalibrator(LCDTarget lcdTarget) {
        this.lcdTarget = lcdTarget;
        this.model = new MultiMatrixModel(lcdTarget);
        model.produceFactor();
        gm = new GSDFGradientModel(model);
        gm.setHKStrategy(strategy);
    }

    public Plot2D plotContrastCurve() {
        Plot2D plot = Plot2D.getInstance("Contrast curve");
        int size = contrastCruve.length;
        for (int x = 0; x < size; x++) {
            double contrast = contrastCruve[x];
            plot.addCacheScatterLinePlot("", x, contrast);
        }
        plot.drawCachePlot();
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "1/contrast");
        plot.setFixedBounds(0, 0, 255);
        plot.setVisible();
        return plot;
    }

    public Plot2D plotCalibratedDeltaJNDIndex() {
        Plot2D plot = Plot2D.getInstance("Calibrated delta JNDIndex");
        int size = calibratedTargetJNDIArray.length;
        for (int x = 0; x < size; x++) {
            double JNDI = calibratedTargetJNDIArray[x];
            RGB rgb = calibratedRGBArray[x];
            double actualJNDI = code2JNDILut.getValue(rgb.getValue(ch));
            double delta = JNDI - actualJNDI;
            plot.addCacheScatterLinePlot("", x, delta);
        }
        plot.drawCachePlot();
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "delta JND Index");
        plot.setFixedBounds(0, 0, 255);
        plot.setVisible();
        return plot;
    }

    public Plot2D plotCalibratedJNDIndex() {
        Plot2D plot = Plot2D.getInstance("Calibrated JNDIndex");
        int size = calibratedTargetJNDIArray.length;
        for (int x = 0; x < size; x++) {
            double JNDI = calibratedTargetJNDIArray[x];
            plot.addCacheScatterLinePlot("target", Color.red, x, JNDI);
            if (x <= maxCode) {
                double orgJNDI = code2JNDILut.getValue(x);
                plot.addCacheScatterLinePlot("original", Color.green, x, orgJNDI);
            }
        }
        plot.drawCachePlot();
        plot.addLegend();
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "JND Index");
        plot.setFixedBounds(0, 0, 255);
        plot.setVisible();
        return plot;
    }

    public Plot2D plotDeltaJNDIndex(RGBBase.Channel ch, RGB.MaxValue maxValue, boolean plotJNDI) {
        Plot2D plot = Plot2D.getInstance("JND Index");
        double step = maxValue.getStepIn255();
        RGB rgb = new RGB(RGB.ColorSpace.unknowRGB, maxValue);
        model.changeMaxValue(rgb);
        double preJNDI = -1;
        for (double c = 0; c <= 254; c += step) {
            rgb.setValue(ch, c, RGB.MaxValue.Double255);
            CIEXYZ XYZ = model.getXYZ(rgb, false);
            double JNDI = GSDF.DICOM.getJNDIndex(XYZ.Y);
            if (preJNDI == -1) {
                preJNDI = JNDI;
                continue;
            }
            double deltaJNDI = JNDI - preJNDI;
            if (plotJNDI) {
                plot.addCacheScatterLinePlot("deltaJNDI*1k", c, deltaJNDI * 1000);
                plot.addCacheScatterLinePlot("JNDI", Color.red, c, JNDI);
            } else {
                plot.addCacheScatterLinePlot("deltaJNDI", c, deltaJNDI);
            }
            preJNDI = JNDI;
        }
        plot.addLegend();
        plot.drawCachePlot();
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "JND Index");
        plot.setFixedBounds(0, 0, 255);
        plot.setVisible();
        return plot;
    }

    public static final int getOccurStep(int occurTimes) {
        int occurStep = (int) (255. / (occurTimes + 1));
        return occurStep;
    }

    public void storeExperimentTargetAsExcel(String filename) {
        int size = experiementTarget.length;
        RGB[] rgbArray = new RGB[size + 1];
        System.arraycopy(experiementTarget, 0, rgbArray, 0, size);
        rgbArray[size] = new RGB();
        try {
            ExcelFile xls = new ExcelFile(filename, true);
            RGBArray.write2VVExcelCell(xls, rgbArray);
            int jndiSize = experimentTargetJNDIArray.length;
            for (int x = 0; x < jndiSize; x++) {
                double jndi = experimentTargetJNDIArray[x];
                xls.setCell(4, x, jndi);
            }
            xls.close();
        } catch (WriteException ex) {
            Logger.log.error("", ex);
        } catch (IOException ex) {
            Logger.log.error("", ex);
        } catch (BiffException ex) {
            Logger.log.error("", ex);
        }
    }

    private RGB[] experiementTarget;

    private int firstAdjustIndex;

    /**
     * �Ĥ@�ӳQ�վ�쪺code index��
     * @return int
     */
    public int getFirstAdjustIndex() {
        return firstAdjustIndex;
    }

    public double getExperimentDeltaJNDI() {
        return experimentDeltaJNDI;
    }

    private double experimentDeltaJNDI;

    /**
     *
     * @param ch Channel
     * @param maxValue MaxValue
     * @param maxCode double
     * @param occurjndi double �վ㪺jndi�B
     * @param deltajndi double �վ㪺delta jndi
     * @return RGB[] ��delta jndi�Ӥp�ɭP�S��k��즳�t����code��, �^��null
     */
    public RGB[] produceExperimentTarget(RGBBase.Channel ch, RGB.MaxValue maxValue, double maxCode, double occurjndi, double deltajndi) {
        return produceExperimentTarget(ch, maxValue, maxCode, occurjndi, deltajndi, -1);
    }

    public RGB[] produceExperimentTarget(RGBBase.Channel ch, RGB.MaxValue maxValue, double maxCode, double occurjndi, double deltajndi, double predeltajndi) {
        RGB[] target = calibrate(ch, maxValue, maxCode);
        int size = calibratedActualJNDIArray.length;
        experimentTargetJNDIArray = new double[size];
        System.arraycopy(calibratedActualJNDIArray, 0, experimentTargetJNDIArray, 0, size);
        boolean adjusted = false;
        for (int x = 0; x < size; x++) {
            double jndi = experimentTargetJNDIArray[x];
            if (jndi >= occurjndi) {
                double newJNDI = jndi + deltajndi;
                newJNDI = code2JNDILut.correctValueInRange(newJNDI);
                double code = code2JNDILut.getKey(newJNDI);
                RGB rgb = target[x];
                rgb.setValue(ch, code, RGB.MaxValue.Double255);
                rgb.changeMaxValue(RGB.MaxValue.Int12Bit);
                rgb.changeMaxValue(RGB.MaxValue.Double255);
                double newcode = rgb.getValue(ch);
                double actualJNDI = code2JNDILut.getValue(newcode);
                experimentTargetJNDIArray[x] = actualJNDI;
                if (!adjusted) {
                    if (predeltajndi != -1) {
                        double oldcode = code2JNDILut.getKey(jndi + predeltajndi);
                        RGB oldrgb = new RGB();
                        oldrgb.setValue(ch, oldcode, RGB.MaxValue.Double255);
                        oldrgb.changeMaxValue(RGB.MaxValue.Int12Bit);
                        oldrgb.changeMaxValue(RGB.MaxValue.Double255);
                        oldcode = oldrgb.getValue(ch);
                        if (oldcode == newcode) {
                            return null;
                        }
                    }
                    firstAdjustIndex = x;
                    experimentDeltaJNDI = actualJNDI - jndi;
                }
                adjusted = true;
                if (!following) {
                    break;
                }
            }
        }
        experiementTarget = target;
        return target;
    }

    public static void main(String[] args) {
        example1(args);
    }

    public static void example1(String[] args) {
    }

    protected Patch getBlackPatch(List<Patch> nonBlack) {
        Patch black = null;
        if (lcdTarget.getNumber() == LCDTargetBase.Number.Ramp4096) {
            Patch first = nonBlack.get(0);
            int index = lcdTarget.getPatchList().indexOf(first);
            black = lcdTarget.getPatch(index - 1);
        } else {
            black = lcdTarget.getBlackPatch();
        }
        return black;
    }

    public double getJNDI(double code) {
        return code2JNDILut.getValue(code);
    }

    public double getCode(double jndi) {
        return code2JNDILut.getKey(jndi);
    }

    public double getLuminance(double jndi) {
        double code = getCode(jndi);
        Patch p = lcdTarget.getPatch(ch, code);
        double Y = p.getXYZ().Y;
        return code2JNDILut.getKey(Y);
    }

    private double[] calibratedTargetJNDIArray;

    private double[] calibratedActualJNDIArray;

    private double[] experimentTargetJNDIArray;

    private Interpolation1DLUT code2JNDILut;

    private RGB[] calibratedRGBArray;

    private RGBBase.Channel ch;

    private double maxCode;

    private double[] contrastCruve;

    private GradientModel.HKStrategy strategy = GradientModel.HKStrategy.None;

    /**
     * ���s��code��jndi�O�_����վ�B��jndi?
     */
    private static boolean following = true;

    /**
     * ����DICOM GSDF�W�d�U��code
     * @param ch Channel
     * @param maxValue MaxValue
     * @param maxCode double
     * @return RGB[]
     */
    public RGB[] calibrate(RGBBase.Channel ch, RGB.MaxValue maxValue, double maxCode) {
        this.ch = ch;
        this.maxCode = maxCode;
        List<Patch> patchList = lcdTarget.filter.oneValueChannel(ch);
        double measureMaxCode = (lcdTarget.getNumber() == LCDTargetBase.Number.Ramp4096) ? 255.75 : 255;
        double step = lcdTarget.getStep();
        int size = patchList.size();
        int maxCodeSize = size - (int) ((measureMaxCode - maxCode) / step);
        Patch blackPatch = getBlackPatch(patchList);
        CIEXYZ blackXYZ = blackPatch.getXYZ();
        Patch maxCodePatch = patchList.get(maxCodeSize - 1);
        CIEXYZ maxXYZ = maxCodePatch.getXYZ();
        double blackJNDI = gm.getJNDIndex(blackXYZ);
        double maxJNDI = gm.getJNDIndex(maxXYZ);
        double jndiStep = (maxJNDI - blackJNDI) / 255.;
        code2JNDILut = produceJNDILUT(ch, maxValue, maxCode);
        RGB[] rgbArray = new RGB[256];
        calibratedTargetJNDIArray = new double[256];
        calibratedActualJNDIArray = new double[256];
        contrastCruve = new double[255];
        double preY = -1;
        for (int x = 0; x <= 255; x++) {
            double targetJNDI = blackJNDI + jndiStep * x;
            double code = code2JNDILut.getKey(targetJNDI);
            calibratedTargetJNDIArray[x] = targetJNDI;
            RGB rgb = new RGB(RGB.ColorSpace.unknowRGB, RGB.MaxValue.Double255);
            rgb.setValue(RGBBase.Channel.W, code, RGB.MaxValue.Double255);
            rgb.changeMaxValue(RGB.MaxValue.Int12Bit);
            rgb.changeMaxValue(RGB.MaxValue.Double255);
            code = rgb.getValue(RGBBase.Channel.W);
            double actualJNDI = code2JNDILut.getValue(code);
            calibratedActualJNDIArray[x] = actualJNDI;
            rgbArray[x] = rgb;
            double Y = gm.getLuminance(targetJNDI);
            if (preY != -1) {
                double contrast = (Y - preY) / (Y + preY);
                contrastCruve[x - 1] = 1. / contrast;
            }
            preY = Y;
        }
        rgbArray[255] = new RGB(RGB.ColorSpace.unknowRGB, RGB.MaxValue.Double255);
        rgbArray[255].setValue(RGBBase.Channel.W, maxCode, RGB.MaxValue.Double255);
        calibratedRGBArray = rgbArray;
        return rgbArray;
    }

    public void produceJNDILUT(double maxcode) {
        code2JNDILut = produceJNDILUT(RGBBase.Channel.W, RGB.MaxValue.Int12Bit, maxcode);
    }

    protected Interpolation1DLUT produceJNDILUT(RGBBase.Channel ch, RGB.MaxValue maxValue, double maxCode) {
        double step = maxValue.getStepIn255();
        RGB rgb = new RGB();
        model.changeMaxValue(rgb);
        int size = (int) (maxCode / step);
        double[] input = new double[size + 1];
        double[] output = new double[size + 1];
        int index = 0;
        for (double c = 0; c <= maxCode; c += step) {
            rgb.setValue(ch, c, RGB.MaxValue.Double255);
            CIEXYZ XYZ = null;
            if (ch == RGBBase.Channel.W) {
                XYZ = model.getNeutralXYZ(c, false);
            } else {
                XYZ = model.getXYZ(rgb, false);
            }
            input[index] = c;
            double jndi = gm.getJNDIndex(XYZ);
            output[index] = jndi;
            index++;
        }
        Interpolation1DLUT lut = new Interpolation1DLUT(input, output, Interpolation1DLUT.Algo.QUADRATIC_POLYNOMIAL);
        return lut;
    }
}
