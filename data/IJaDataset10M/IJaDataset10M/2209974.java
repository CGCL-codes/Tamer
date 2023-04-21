package com.webcamtracker.ms2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * optimizar
 * initialize target model solo al principio del tracking
 * cachear epanechnikov profile
 */
public class TrackingFilter {

    public static final int NUMBEROFBINS = 16;

    public static final int MAX_ITERATIONS = 20;

    Point initialBoxCOG;

    public Point currentBoxCOG;

    ColorHistogramIndex target;

    ColorHistogramIndex candidate;

    ColorHistogramIndex colorHistogram;

    private BufferedImage targetModel;

    private void setTrackingCenter(int x, int y) {
        initialBoxCOG = new Point(x, y);
        currentBoxCOG = new Point(x, y);
    }

    public static void captureRGBHistogram(BufferedImage data, ColorHistogramIndex index, Point cog, int width, int height) {
        int r, g, b;
        int rBinIndex = 0;
        int gBinIndex = 0;
        int bBinIndex = 0;
        assert width % 2 == 0;
        assert height % 2 == 0;
        int halfHeight = height / 2;
        int halfWidth = width / 2;
        ModelParameters modelParameters = ModelParametersFactoryImpl.getInstance().createModelParameters(halfHeight, halfWidth);
        index.clearRGBHistrogram();
        index.initializeBinIndexes();
        for (int j = (cog.x - halfWidth), l = 0; j < (cog.x + halfWidth); j++, l++) {
            for (int i = (cog.y - halfHeight), m = 0; i < (cog.y + halfHeight); i++, m++) {
                if ((i >= 0) && (i < data.getHeight()) && (j >= 0) && (j < data.getWidth())) {
                    int rgb = data.getRGB(j, i);
                    r = Utils.red(rgb);
                    g = Utils.green(rgb);
                    b = Utils.blue(rgb);
                    rBinIndex = (int) ((r * (index.numberOfBins - 1.0f)) / 255.0f);
                    gBinIndex = (int) ((g * (index.numberOfBins - 1.0f)) / 255.0f);
                    bBinIndex = (int) ((b * (index.numberOfBins - 1.0f)) / 255.0f);
                    index.redBinIndex[i][j] = rBinIndex;
                    index.greenBinIndex[i][j] = gBinIndex;
                    index.blueBinIndex[i][j] = bBinIndex;
                    index.rgbHistogram[rBinIndex][gBinIndex][bBinIndex] += modelParameters.profileParams[m][l];
                }
            }
        }
        float check = 0.0f;
        for (int i = 0; i < index.numberOfBins; i++) {
            for (int j = 0; j < index.numberOfBins; j++) {
                for (int k = 0; k < index.numberOfBins; k++) {
                    index.rgbHistogram[i][j][k] = index.rgbHistogram[i][j][k] / modelParameters.profileParamsSum;
                    check += index.rgbHistogram[i][j][k];
                }
            }
        }
    }

    private void captureModelRGBHistogram(BufferedImage targetModel) {
        target = new ColorHistogramIndex(targetModel.getWidth(), targetModel.getHeight(), NUMBEROFBINS);
        captureRGBHistogram(targetModel, target, new Point(targetModel.getWidth() / 2, targetModel.getHeight() / 2), targetModel.getWidth(), targetModel.getHeight());
    }

    private void captureCandidateRGBHistogram(BufferedImage data, ColorHistogramIndex candidate, Point cog, int width, int height) {
        captureRGBHistogram(data, candidate, cog, width, height);
    }

    public void setTargetModel(BufferedImage targetModel) {
        this.targetModel = targetModel;
        captureModelRGBHistogram(targetModel);
    }

    public void meanShiftTracking(BufferedImage targetModel, BufferedImage data, Point trackingCenter) {
        setTargetModel(targetModel);
        meanShiftTracking(data, trackingCenter);
    }

    public void meanShiftTracking(BufferedImage data, Point trackingCenter) {
        setTrackingCenter(trackingCenter.x, trackingCenter.y);
        Point tempCandidatePosition, tmpCand;
        float sumXWeights, sumYWeights, sumWeights;
        int i, j, k, p, q, uR, uG, uB = 0;
        float prevBhattacharyya = 0.0f;
        float bhattacharyya = 0.0f;
        int iterations = 0;
        boolean shift = true;
        while (shift) {
            int candidateHeight = targetModel.getHeight();
            int candidateWidth = targetModel.getWidth();
            int candidateHalfHeight = candidateHeight / 2;
            int candidateHalfWidth = candidateWidth / 2;
            ModelParameters candidateParameters = ModelParametersFactoryImpl.getInstance().createModelParameters(candidateHalfHeight, candidateHalfWidth);
            candidate = new ColorHistogramIndex(data.getWidth(), data.getHeight(), NUMBEROFBINS);
            candidate.initializeBinIndexes();
            candidate.clearRGBHistrogram();
            candidate.initializeWeights();
            captureCandidateRGBHistogram(data, candidate, currentBoxCOG, targetModel.getWidth(), targetModel.getHeight());
            for (i = 0; i < candidate.numberOfBins; i++) {
                for (j = 0; j < candidate.numberOfBins; j++) {
                    for (k = 0; k < candidate.numberOfBins; k++) {
                        prevBhattacharyya += Math.sqrt(target.rgbHistogram[i][j][k] * candidate.rgbHistogram[i][j][k]);
                    }
                }
            }
            for (i = (currentBoxCOG.y - candidateHalfHeight), p = 0; i < (currentBoxCOG.y + candidateHalfHeight); i++, p++) {
                for (j = (currentBoxCOG.x - candidateHalfWidth), q = 0; j < (currentBoxCOG.x + candidateHalfWidth); j++, q++) {
                    if ((0 <= i) && (i < data.getHeight()) && (j >= 0) && (j < data.getWidth())) {
                        if (candidateParameters.nonZeroMask[p][q]) {
                            uR = candidate.redBinIndex[i][j];
                            uG = candidate.greenBinIndex[i][j];
                            uB = candidate.blueBinIndex[i][j];
                            if (candidate.rgbHistogram[uR][uG][uB] > 0.0f) {
                                candidate.weights[i][j] = (float) Math.sqrt(target.rgbHistogram[uR][uG][uB] / candidate.rgbHistogram[uR][uG][uB]);
                            }
                        }
                    }
                }
            }
            sumXWeights = 0.0f;
            sumYWeights = 0.0f;
            sumWeights = 0.0f;
            for (i = (currentBoxCOG.y - candidateHalfHeight), p = 0; i < (currentBoxCOG.y + candidateHalfHeight); i++, p++) {
                for (j = (currentBoxCOG.x - candidateHalfWidth), q = 0; j < (currentBoxCOG.x + candidateHalfWidth); j++, q++) {
                    if ((0 <= i) && (i < data.getHeight()) && (j >= 0) && (j < data.getWidth())) {
                        if (candidateParameters.nonZeroMask[p][q]) {
                            sumYWeights += i * candidate.weights[i][j];
                            sumXWeights += j * candidate.weights[i][j];
                            sumWeights += candidate.weights[i][j];
                        }
                    }
                }
            }
            tempCandidatePosition = new Point();
            tempCandidatePosition.x = (int) (sumXWeights / sumWeights);
            tempCandidatePosition.y = (int) (sumYWeights / sumWeights);
            boolean cont = true;
            while (cont) {
                colorHistogram = new ColorHistogramIndex(data.getWidth(), data.getHeight(), NUMBEROFBINS);
                colorHistogram.initializeBinIndexes();
                colorHistogram.clearRGBHistrogram();
                colorHistogram.initializeWeights();
                captureCandidateRGBHistogram(data, colorHistogram, tempCandidatePosition, targetModel.getWidth(), targetModel.getHeight());
                bhattacharyya = 0.0f;
                for (i = 0; i < candidate.numberOfBins; i++) {
                    for (j = 0; j < candidate.numberOfBins; j++) {
                        for (k = 0; k < candidate.numberOfBins; k++) {
                            bhattacharyya += Math.sqrt(target.rgbHistogram[i][j][k] * colorHistogram.rgbHistogram[i][j][k]);
                        }
                    }
                }
                tmpCand = new Point();
                if (bhattacharyya < prevBhattacharyya) {
                    tmpCand.x = tempCandidatePosition.x;
                    tmpCand.y = tempCandidatePosition.y;
                    prevBhattacharyya = bhattacharyya;
                    tempCandidatePosition.x = (int) (0.5f * (currentBoxCOG.x + tmpCand.x));
                    tempCandidatePosition.y = (int) (0.5f * (currentBoxCOG.y + tmpCand.y));
                } else {
                    cont = false;
                }
            }
            if (shift = iterations++ < MAX_ITERATIONS && !currentBoxCOG.equals(tempCandidatePosition)) {
                currentBoxCOG.x = tempCandidatePosition.x;
                currentBoxCOG.y = tempCandidatePosition.y;
            }
        }
    }
}
