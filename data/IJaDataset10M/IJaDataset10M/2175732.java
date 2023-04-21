package shu.cms.lcd.calibrate.modeled;

import java.awt.*;
import shu.cms.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.hvs.*;
import shu.cms.lcd.calibrate.parameter.*;
import shu.cms.plot.*;

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
public class CalibratorPlotter {

    private LCDModelCalibrator c;

    public CalibratorPlotter(LCDModelCalibrator lcdClibrator) {
        this.c = lcdClibrator;
    }

    public void plot() {
        plotRGBDeltaE(c.rbDeltaEArray);
        plotuvPrime(c.targetxyYCurve, c.calibrateRGBArray);
        plotuvPrimeY(c.targetxyYCurve, c.calibrateRGBArray);
        plotGamma(c.calibrateRGBArray, c.cp.gamma == ColorProofParameter.Gamma.Custom ? c.cp.customGamma : -1);
    }

    private void plotGamma(RGB[] caliRGBArray, double targetGamma) {
        Plot2D plot = Plot2D.getInstance("Gamma");
        int size = caliRGBArray.length;
        RGB whiteRGB = (RGB) caliRGBArray[size - 1].clone();
        CIEXYZ wMaxXYZ = c.model.getXYZ(whiteRGB, false);
        double wMaxY = wMaxXYZ.Y;
        whiteRGB.reserveValue(RGBBase.Channel.G);
        CIEXYZ gMaxXYZ = c.model.getXYZ(whiteRGB, false);
        CIEXYZ flare = c.model.flare.getFlare();
        double flareY = flare.Y;
        for (int x = 0; x < size; x++) {
            RGB rgb = caliRGBArray[x];
            RGB w = (RGB) rgb.clone();
            double wY = c.model.getXYZ(w, false).Y;
            w.reserveValue(RGBBase.Channel.G);
            double gY = c.model.getXYZ(w, false).Y;
            double wGamma = (wY - flareY) / (wMaxXYZ.Y - flareY);
            double gGamma = (gY - flareY) / (gMaxXYZ.Y - flareY);
            plot.addCacheScatterLinePlot("W Y", Color.black, x, wGamma);
            plot.addCacheScatterLinePlot("G Y", Color.green, x, gGamma);
            if (targetGamma != -1) {
                double normal = ((double) x) / (size - 1);
                normal = Math.pow(normal, targetGamma);
                double idealY = (wMaxY - flareY) * normal + flareY;
                double wYJNDI = GSDF.DICOM.getJNDIndex(wY);
                double iYJNDI = GSDF.DICOM.getJNDIndex(idealY);
                double deltaJNDI = wYJNDI - iYJNDI;
                plot.addCacheScatterLinePlot("W dJNDI", Color.magenta, x, deltaJNDI);
            }
        }
        plot.drawCachePlot();
        plot.setFixedBounds(0, 0, 255);
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "JND step");
        plot.addLegend();
        plot.setVisible();
    }

    private Plot2D plotRGBDeltaE(DeltaE[] rgbDeltaEArray) {
        Plot2D plot = Plot2D.getInstance("RGBDeltaE");
        int size = rgbDeltaEArray.length;
        for (int x = 0; x < size; x++) {
            DeltaE de = rgbDeltaEArray[x];
            plot.addCacheScatterLinePlot("dE", Color.red, x + 1, de.getCIE2000DeltaE());
            plot.addCacheScatterLinePlot("dab", Color.green, x + 1, de.getCIE2000Deltaab());
        }
        plot.drawCachePlot();
        plot.setFixedBounds(0, 0, 255);
        plot.setAxeLabel(0, "code");
        plot.setAxeLabel(1, "deltaE");
        plot.addLegend();
        plot.setVisible();
        return plot;
    }

    private void plotuvPrime(CIExyY[] targetxyYCurve, RGB[] caliRGBArray) {
        Plot2D plot = Plot2D.getInstance("uv' diagram");
        Plot2D cctplot = Plot2D.getInstance("CCT");
        int size = targetxyYCurve.length;
        for (int x = 0; x < size; x++) {
            CIExyY xyY = targetxyYCurve[x];
            double[] idealuvp = xyY.getuvPrimeValues();
            plot.addCacheScatterLinePlot("ideal", Color.red, idealuvp[0], idealuvp[1]);
            RGB rgb = caliRGBArray[x];
            c.model.changeMaxValue(rgb);
            CIEXYZ XYZ = c.model.getXYZ(rgb, false);
            double[] uvp = XYZ.getuvPrimeValues();
            plot.addCacheScatterLinePlot("actual", Color.green, uvp[0], uvp[1]);
            RGB nativeRGB = new RGB(RGB.ColorSpace.unknowRGB, new int[] { x, x, x });
            c.model.changeMaxValue(nativeRGB);
            CIEXYZ nativeXYZ2 = c.model.getXYZ(nativeRGB, false);
            double[] nativeuvp = nativeXYZ2.getuvPrimeValues();
            plot.addCacheScatterLinePlot("native", Color.blue, nativeuvp[0], nativeuvp[1]);
            CIEXYZ simulateXYZ = c.model.getXYZ(rgb, false);
            cctplot.addCacheScatterLinePlot("target CCT", Color.red, x, xyY.getCCT());
            cctplot.addCacheScatterLinePlot("simulate CCT", Color.green, x, simulateXYZ.getCCT());
        }
        plot.setAxeLabel(0, "u'");
        plot.setAxeLabel(1, "v'");
        plot.drawCachePlot();
        plot.addLegend();
        plot.setVisible();
        cctplot.setAxeLabel(0, "code");
        cctplot.setAxeLabel(1, "CCT");
        cctplot.drawCachePlot();
        cctplot.addLegend();
        cctplot.setFixedBounds(0, 0, 255);
        cctplot.setVisible();
        double[][] duvpArray = new LCDModelCalibrateReporter(c).getEstimateDeltauvPrime();
        int duvpSize = duvpArray.length;
        Plot2D dplot = Plot2D.getInstance("delta uv'");
        for (int x = 0; x < duvpSize; x++) {
            double[] duvp = duvpArray[x];
            dplot.addCacheScatterLinePlot("acutal-du'", Color.red, x, duvp[0]);
            dplot.addCacheScatterLinePlot("acutal-dv'", Color.green, x, duvp[1]);
        }
        dplot.setAxeLabel(0, "code");
        dplot.setAxeLabel(1, "delta");
        dplot.drawCachePlot();
        dplot.setFixedBounds(0, 0, 255);
        dplot.addLegend();
        dplot.setVisible();
    }

    private void plotuvPrimeY(CIExyY[] targetxyYCurve, RGB[] caliRGBArray) {
        Plot3D plot = Plot3D.getInstance("u'v'Y");
        int size = targetxyYCurve.length;
        double[][] idealData = new double[size][];
        double[][] modelData = new double[size][];
        for (int x = 0; x < size; x++) {
            CIExyY xyY = targetxyYCurve[x];
            double[] idealuvpY = xyY.getuvPrimeYValues();
            idealData[x] = idealuvpY;
            RGB rgb = caliRGBArray[x];
            c.model.changeMaxValue(rgb);
            CIEXYZ XYZ = c.model.getXYZ(rgb, false);
            CIExyY modelxyY = new CIExyY(XYZ);
            double[] uvpY = modelxyY.getuvPrimeYValues();
            modelData[x] = uvpY;
        }
        plot.addLinePlot("ideal", Color.blue, idealData);
        plot.addLinePlot("model", Color.red, modelData);
        plot.setAxeLabel(0, "u'");
        plot.setAxeLabel(1, "v'");
        plot.setAxeLabel(2, "Y");
        plot.setVisible();
    }
}
