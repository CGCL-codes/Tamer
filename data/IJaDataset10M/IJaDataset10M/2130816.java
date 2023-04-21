package shu.cms.camdc.test;

import java.io.*;
import java.util.*;
import java.awt.image.*;
import shu.cms.*;
import shu.cms.camdc.model.*;
import shu.cms.camdc.model.DCUtils;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.dc.*;
import shu.cms.dc.ideal.*;
import shu.cms.hvs.cam.*;
import shu.cms.image.*;
import shu.cms.plot.*;
import shu.math.array.*;
import shu.math.lut.*;
import shu.cms.devicemodel.dc.DCPolynomialRegressionModel;
import shu.cms.util.GammaCorrector;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class WhiteBalanceTester {

    private IdealDigitalCamera camera;

    private DCPolynomialRegressionModel dcmodel;

    private GammaCorrector[] gammaCorrectors;

    public WhiteBalanceTester(IdealDigitalCamera camera, DCPolynomialRegressionModel dcmodel) {
        this.camera = camera;
        this.dcmodel = dcmodel;
    }

    private double getGCorrectFactor(Illuminant illuminant) {
        List<Spectra> targetRefelectSpactra = DCTargetBase.Instance.getTargetReflectSpectra(DCTargetBase.Chart.CC24);
        Spectra whiteSpectra = (Spectra) targetRefelectSpactra.get(3).clone();
        List<Spectra> whiteSpectraList = new ArrayList<Spectra>(1);
        whiteSpectraList.add(whiteSpectra);
        whiteSpectraList = Spectra.produceSpectraPowerList(whiteSpectraList, illuminant);
        Spectra whitePowerSpetra = whiteSpectraList.get(0);
        double[] rgbValues = whitePowerSpetra.getRGBValues(camera);
        double gfactor = 0.885583 / rgbValues[1];
        return gfactor;
    }

    private final ChromaticAdaptation getChromaticAdaptation(Illuminant illuminant) {
        CIEXYZ from = illuminant.getNormalizeXYZ();
        ChromaticAdaptation ca = ChromaticAdaptation.getInstanceAdaptToD65(from, CAMConst.CATType.CAT02);
        return ca;
    }

    private void gammaCorrect(double[] rgbValues) {
        rgbValues[0] = gammaCorrectors[0].correct(rgbValues[0]);
        rgbValues[1] = gammaCorrectors[1].correct(rgbValues[1]);
        rgbValues[2] = gammaCorrectors[2].correct(rgbValues[2]);
    }

    public final BufferedImage whiteBalance(BufferedImage image, Illuminant illuminant) {
        double gfactor = getGCorrectFactor(illuminant);
        camera.setGreenCorrectFactor(gfactor);
        double[][] rgb2XYZMatrix = DCUtils.getRGB2XYZMatrix(illuminant, camera);
        ChromaticAdaptation ca = getChromaticAdaptation(illuminant);
        double[][] illuminant2D65Matrix = ca.getAdaptationMatrixToDestination();
        double[][] rgb2D65XYZMatrix = DoubleArray.times(illuminant2D65Matrix, rgb2XYZMatrix);
        BufferedImage result = ImageUtils.cloneBufferedImage(image);
        int width = result.getWidth();
        int height = result.getHeight();
        WritableRaster raster = result.getRaster();
        double[] rgbValues = new double[3];
        rgbValues = new double[] { 253, 226, 107 };
        rgbValues = DoubleArray.times(rgbValues, 1 / 255.);
        gammaCorrect(rgbValues);
        double[] D65XYZValues_ = DoubleArray.times(rgb2D65XYZMatrix, rgbValues);
        double[] sRGBValues_ = RGB.fromXYZValues(D65XYZValues_, RGB.ColorSpace.sRGB);
        DoubleArray.timesAndNoReturn(sRGBValues_, 255);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.getPixel(x, y, rgbValues);
                DoubleArray.timesAndNoReturn(rgbValues, 1. / 255);
                gammaCorrect(rgbValues);
                double[] D65XYZValues = DoubleArray.times(rgb2D65XYZMatrix, rgbValues);
                double[] sRGBValues = RGB.fromXYZValues(D65XYZValues, RGB.ColorSpace.sRGB);
                DoubleArray.timesAndNoReturn(sRGBValues, 255);
                RGB.rationalize(sRGBValues, RGB.MaxValue.Double255);
                raster.setPixel(x, y, sRGBValues);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String dayICCFilename = "Measurement Files/camera/htc legend/test3/��.icc";
        String AICCFilename = "Measurement Files/camera/htc legend/test3/�뵷�O.icc";
        DCTarget ADCTarget = DCTarget.Instance.get(LightSource.CIE.A, 1, DCTarget.Chart.MiniCC24, DCTarget.FileType.ICC, AICCFilename);
        CameraSensorEstimator estimator = new CameraSensorEstimator();
        IdealDigitalCamera camera = estimator.estimate(dayICCFilename, LightSource.CIE.F8);
        DCPolynomialRegressionModel dcmodel = estimator.getDCModel();
        GammaCorrector[] gammaCorrectors = estimator.getGammaCorrectors();
        Plot2D sensorPlot = Plot2D.getInstance();
        DCUtils.plotSensor(sensorPlot, camera);
        sensorPlot.setVisible();
        WhiteBalanceTester wbt = new WhiteBalanceTester(camera, dcmodel);
        wbt.gammaCorrectors = gammaCorrectors;
        BufferedImage image = null;
        try {
            image = ImageUtils.loadImage("Image/WhiteBalance/HTC Legend/�뵷�O.jpg");
            Spectra spectra = CorrelatedColorTemperature.getSpectraOfBlackbodyRadiator(3000);
            Illuminant illuminant = Illuminant.A;
            BufferedImage wbImage = wbt.whiteBalance(image, illuminant);
            ImageUtils.storeJPEGImage("wb.jpg", wbImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
