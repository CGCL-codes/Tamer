package net.sf.mzmine.peaklistmethods;

import net.sf.mzmine.alignmentresultmethods.*;
import net.sf.mzmine.alignmentresultvisualizers.*;
import net.sf.mzmine.datastructures.*;
import net.sf.mzmine.distributionframework.*;
import net.sf.mzmine.miscellaneous.*;
import net.sf.mzmine.peaklistmethods.*;
import net.sf.mzmine.rawdatamethods.*;
import net.sf.mzmine.rawdatavisualizers.*;
import net.sf.mzmine.userinterface.*;
import java.util.*;
import java.text.NumberFormat;

/**
 * This class implements a peak picker based on searching for local maximums in each spectra
 */
public class CentroidPicker implements PeakPicker {

    private final String[] fieldNames = { "M/Z bin size (Da)", "Chromatographic threshold level (%)", "Noise level (absolute value)", "Minimum peak height (absolute value)", "Minimum peak duration (seconds)", "Tolerance for m/z variation (Da)", "Tolerance for intensity variation (%)" };

    /**
	 * Method asks parameter values from user
	 */
    public CentroidPickerParameters askParameters(MainWindow mainWin, CentroidPickerParameters currentValues) {
        CentroidPickerParameters myParameters;
        if (currentValues == null) {
            myParameters = new CentroidPickerParameters();
        } else {
            myParameters = currentValues;
        }
        double[] paramValues = new double[7];
        paramValues[0] = myParameters.binSize;
        paramValues[1] = myParameters.chromatographicThresholdLevel;
        paramValues[2] = myParameters.noiseLevel;
        paramValues[3] = myParameters.minimumPeakHeight;
        paramValues[4] = myParameters.minimumPeakDuration;
        paramValues[5] = myParameters.mzTolerance;
        paramValues[6] = myParameters.intTolerance;
        NumberFormat[] numberFormats = new NumberFormat[7];
        numberFormats[0] = NumberFormat.getNumberInstance();
        numberFormats[0].setMinimumFractionDigits(2);
        numberFormats[1] = NumberFormat.getPercentInstance();
        numberFormats[2] = NumberFormat.getNumberInstance();
        numberFormats[2].setMinimumFractionDigits(0);
        numberFormats[3] = NumberFormat.getNumberInstance();
        numberFormats[3].setMinimumFractionDigits(0);
        numberFormats[4] = NumberFormat.getNumberInstance();
        numberFormats[4].setMinimumFractionDigits(1);
        numberFormats[5] = NumberFormat.getNumberInstance();
        numberFormats[5].setMinimumFractionDigits(3);
        numberFormats[6] = NumberFormat.getPercentInstance();
        ParameterSetupDialog psd = new ParameterSetupDialog(mainWin, "Please check the parameter values", fieldNames, paramValues, numberFormats);
        psd.showModal(mainWin.getDesktop());
        if (psd.getExitCode() == -1) {
            return null;
        }
        double d;
        d = psd.getFieldValue(0);
        if (d <= 0) {
            mainWin.displayErrorMessage("Incorrect bin size!");
            return null;
        }
        myParameters.binSize = d;
        d = psd.getFieldValue(1);
        if ((d < 0) || (d > 1)) {
            mainWin.displayErrorMessage("Incorrect chromatographic threshold level!");
            return null;
        }
        myParameters.chromatographicThresholdLevel = d;
        d = psd.getFieldValue(2);
        if (d < 0) {
            mainWin.displayErrorMessage("Incorrect noise level!");
            return null;
        }
        myParameters.noiseLevel = d;
        d = psd.getFieldValue(3);
        if (d <= 0) {
            mainWin.displayErrorMessage("Incorrect minimum peak height!");
            return null;
        }
        myParameters.minimumPeakHeight = d;
        d = psd.getFieldValue(4);
        if (d <= 0) {
            mainWin.displayErrorMessage("Incorrect minimum peak duration!");
            return null;
        }
        myParameters.minimumPeakDuration = d;
        d = psd.getFieldValue(5);
        if (d < 0) {
            mainWin.displayErrorMessage("Incorrect m/z tolerance value!");
            return null;
        }
        myParameters.mzTolerance = d;
        d = psd.getFieldValue(6);
        if (d < 0) {
            mainWin.displayErrorMessage("Incorrect intensity tolerance value!");
            return null;
        }
        myParameters.intTolerance = d;
        return myParameters;
    }

    public PeakList findPeaks(NodeServer nodeServer, RawDataAtNode rawData, PeakPickerParameters _parameters) {
        CentroidPickerParameters parameters = (CentroidPickerParameters) _parameters;
        double startMZ = rawData.getMinMZValue();
        double endMZ = rawData.getMaxMZValue();
        int maxScanNum = rawData.getNumberOfScans();
        int numOfBins = (int) (java.lang.Math.ceil((endMZ - startMZ) / parameters.binSize));
        double[] tmpInts;
        double[][] binInts = new double[numOfBins][maxScanNum];
        double[] chromatographicThresholds = new double[numOfBins];
        Scan s;
        double[] masses;
        double[] intensities;
        Vector<OneDimPeak> oneDimPeaks;
        int ind;
        int bin;
        PeakList peakList = new PeakList();
        Vector<PeakConstruction> underConstructionPeaks = new Vector<PeakConstruction>();
        double mass;
        double intensity;
        int minPeakLengthInScans = rawData.getScanNumberByTime(parameters.minimumPeakDuration);
        if (parameters.chromatographicThresholdLevel > 0) {
            rawData.initializeScanBrowser(0, maxScanNum - 1);
            for (int scani = 0; scani < maxScanNum; scani++) {
                s = rawData.getNextScan();
                nodeServer.updateJobCompletionRate((double) (scani) / (double) (maxScanNum * 2 - 2));
                tmpInts = s.getBinnedIntensities(startMZ, endMZ, numOfBins, true);
                for (int bini = 0; bini < numOfBins; bini++) {
                    binInts[bini][scani] = tmpInts[bini];
                }
            }
            rawData.finalizeScanBrowser();
            for (int bini = 0; bini < numOfBins; bini++) {
                chromatographicThresholds[bini] = MyMath.calcQuantile(binInts[bini], parameters.chromatographicThresholdLevel);
            }
        } else {
            for (int bini = 0; bini < numOfBins; bini++) {
                chromatographicThresholds[bini] = 0;
            }
        }
        binInts = null;
        tmpInts = null;
        System.gc();
        rawData.initializeScanBrowser(0, rawData.getNumberOfScans() - 1);
        for (int i = 0; i < maxScanNum; i++) {
            nodeServer.updateJobCompletionRate((double) (i + maxScanNum) / (double) (maxScanNum * 2 - 2));
            s = rawData.getNextScan();
            masses = s.getMZValues();
            intensities = s.getIntensityValues();
            oneDimPeaks = new Vector<OneDimPeak>();
            for (int j = 0; j < intensities.length; j++) {
                if (intensities[j] >= parameters.noiseLevel) {
                    bin = (int) java.lang.Math.floor((masses[j] - startMZ) / parameters.binSize);
                    if (bin < 0) {
                        bin = 0;
                    }
                    if (bin >= numOfBins) {
                        bin = numOfBins - 1;
                    }
                    if (intensities[j] >= chromatographicThresholds[bin]) {
                        oneDimPeaks.add(new OneDimPeak(i, j, masses[j], intensities[j]));
                    }
                }
            }
            TreeSet<MatchScore> scores = new TreeSet<MatchScore>();
            double maxScore = 1000;
            for (PeakConstruction ucPeak : underConstructionPeaks) {
                for (OneDimPeak oneDimPeak : oneDimPeaks) {
                    MatchScore score = calcScore(ucPeak, oneDimPeak, maxScore, parameters);
                    if (score.getScore() < Double.MAX_VALUE) {
                        scores.add(score);
                    }
                }
            }
            Iterator<MatchScore> scoreIterator = scores.iterator();
            while (scoreIterator.hasNext()) {
                MatchScore score = scoreIterator.next();
                if (score.getScore() >= Double.MAX_VALUE) {
                    break;
                }
                if (score.getOneDimPeak().isConnected()) {
                    continue;
                }
                if (score.getUnderConstructionPeak().isGrowing()) {
                    continue;
                }
                PeakConstruction ucPeak = score.getUnderConstructionPeak();
                OneDimPeak oneDimPeak = score.getOneDimPeak();
                ucPeak.addScan(score.getOneDimPeak().scanNum, score.getOneDimPeak().datapointIndex, score.getOneDimPeak().mz, score.getOneDimPeak().intensity);
                ucPeak.setGrowingStatus(true);
                oneDimPeak.setConnected();
            }
            int ucInd = 0;
            for (PeakConstruction ucPeak : underConstructionPeaks) {
                if (!ucPeak.isGrowing()) {
                    ucPeak.finalizePeak();
                    int[] startStopInd = cropRTShape(ucPeak.getCentroidIntensities(), parameters);
                    if (startStopInd[0] <= startStopInd[1]) {
                        ucPeak.finalizePeak(startStopInd[0], startStopInd[1]);
                        double ucLength = ucPeak.getLengthInSecs();
                        double ucHeight = ucPeak.getMaxIntensity();
                        if ((ucLength >= parameters.minimumPeakDuration) && (ucHeight >= parameters.minimumPeakHeight)) {
                            Peak finalPeak = new Peak(ucPeak.getCentroidMZMedian(), ucPeak.getMaxIntensityTime(), ucPeak.getMaxIntensity(), ucPeak.getSumOfIntensities(), ucPeak.getCentroidMZs(), ucPeak.getScanNums(), ucPeak.getCentroidIntensities());
                            peakList.addPeak(finalPeak);
                        }
                    }
                    underConstructionPeaks.set(ucInd, null);
                }
                ucInd++;
            }
            for (ucInd = 0; ucInd < underConstructionPeaks.size(); ucInd++) {
                PeakConstruction ucPeak = underConstructionPeaks.get(ucInd);
                if (ucPeak == null) {
                    underConstructionPeaks.remove(ucInd);
                    ucInd--;
                } else {
                    ucPeak.setGrowingStatus(false);
                }
            }
            for (OneDimPeak oneDimPeak : oneDimPeaks) {
                if (!oneDimPeak.isConnected()) {
                    PeakConstruction ucPeak = new PeakConstruction(rawData, minPeakLengthInScans, minPeakLengthInScans);
                    ucPeak.addScan(i, oneDimPeak.datapointIndex, oneDimPeak.mz, oneDimPeak.intensity);
                    underConstructionPeaks.add(ucPeak);
                }
            }
        }
        for (PeakConstruction ucPeak : underConstructionPeaks) {
            ucPeak.finalizePeak();
            int[] startStopInd = cropRTShape(ucPeak.getCentroidIntensities(), parameters);
            if (startStopInd[0] <= startStopInd[1]) {
                ucPeak.finalizePeak(startStopInd[0], startStopInd[1]);
                double ucLength = ucPeak.getLengthInSecs();
                double ucHeight = ucPeak.getMaxIntensity();
                if ((ucLength >= parameters.minimumPeakDuration) && (ucHeight >= parameters.minimumPeakHeight)) {
                    Peak finalPeak = new Peak(ucPeak.getCentroidMZMedian(), ucPeak.getMaxIntensityTime(), ucPeak.getMaxIntensity(), ucPeak.getSumOfIntensities(), ucPeak.getCentroidMZs(), ucPeak.getScanNums(), ucPeak.getCentroidIntensities());
                    peakList.addPeak(finalPeak);
                }
            }
        }
        rawData.finalizeScanBrowser();
        return peakList;
    }

    /**
	 * This functions calculates the score for goodness of match between uc-peak and 1d-peak
	 */
    private MatchScore calcScore(PeakConstruction ucPeak, OneDimPeak oneDimPeak, double maxScore, CentroidPickerParameters parameters) {
        double ucMZ = ucPeak.getCentroidMZMedian();
        double[] ucPrevInts = ucPeak.getCentroidIntensities();
        int ucUsedSize = ucPeak.getUsedSize();
        if (java.lang.Math.abs(ucMZ - oneDimPeak.mz) > parameters.mzTolerance) {
            return new MatchScore(ucPeak, oneDimPeak, Double.MAX_VALUE);
        } else {
            double scoreMZComponent = java.lang.Math.abs(ucMZ - oneDimPeak.mz) * maxScore;
            double scoreRTComponent = calcScoreForRTShape(ucPrevInts, ucUsedSize, oneDimPeak.intensity, parameters.intTolerance, maxScore);
            double totalScore = java.lang.Math.sqrt(scoreMZComponent * scoreMZComponent + scoreRTComponent * scoreRTComponent);
            return new MatchScore(ucPeak, oneDimPeak, totalScore);
        }
    }

    /**
	 * this function crops the shape of the final peak in RT direction
	 * cropping removes continuous regions that stay within intensity
	 * tolerance from beginning and end of the peak
	 *
	 * @return	array of two indices, left and right one.
	 */
    private int[] cropRTShape(double[] intensities, CentroidPickerParameters parameters) {
        int res[] = new int[2];
        res[0] = 0;
        res[1] = intensities.length - 1;
        return res;
    }

    /**
	 * This function check for the shape of the peak in RT direction, and
	 * determines if it is possible to add given m/z peak at the end of the peak.
	 *
	 */
    private double calcScoreForRTShape(double prevInts[], int usedSize, double nextInt, double intTolerance, double maxScore) {
        if (usedSize == 0) {
            return 0;
        }
        if (usedSize == 1) {
            if ((nextInt - prevInts[0]) >= 0) {
                return 0;
            }
            double bottomMargin = prevInts[0] * (1 - intTolerance);
            if (nextInt <= bottomMargin) {
                return Double.MAX_VALUE;
            }
            return 0;
        }
        int derSign = 1;
        for (int ind = 2; ind < usedSize; ind++) {
            if (derSign == 1) {
                double bottomMargin = prevInts[ind - 1] * (1 - intTolerance);
                if (prevInts[ind] <= bottomMargin) {
                    derSign = -1;
                    continue;
                }
            }
            if (derSign == -1) {
                double topMargin = prevInts[ind - 1] * (1 + intTolerance);
                if (prevInts[ind] >= topMargin) {
                    return Double.MAX_VALUE;
                }
            }
        }
        if (derSign == 1) {
            return 0;
        }
        if (derSign == -1) {
            double topMargin = prevInts[usedSize - 1] * (1 + intTolerance);
            if (nextInt >= topMargin) {
                return Double.MAX_VALUE;
            }
            if (nextInt < prevInts[usedSize - 1]) {
                return 0;
            }
            return 0;
        }
        return Double.MAX_VALUE;
    }

    private class OneDimPeak {

        public int scanNum;

        public double mz;

        public double intensity;

        public int datapointIndex;

        private boolean connected;

        public OneDimPeak(int _scanNum, int _datapointIndex, double _mz, double _intensity) {
            scanNum = _scanNum;
            datapointIndex = _datapointIndex;
            mz = _mz;
            intensity = _intensity;
            connected = false;
        }

        public void setConnected() {
            connected = true;
        }

        public boolean isConnected() {
            return connected;
        }
    }

    private class MatchScore implements Comparable<MatchScore> {

        private double score;

        private PeakConstruction ucPeak;

        private OneDimPeak oneDimPeak;

        public MatchScore(PeakConstruction uc, OneDimPeak od, double s) {
            ucPeak = uc;
            oneDimPeak = od;
            score = s;
        }

        public int compareTo(MatchScore m) {
            int retsig = (int) java.lang.Math.signum(score - m.getScore());
            if (retsig == 0) {
                retsig = -1;
            }
            return retsig;
        }

        public void setScore(double s) {
            score = s;
        }

        public double getScore() {
            return score;
        }

        public PeakConstruction getUnderConstructionPeak() {
            return ucPeak;
        }

        public OneDimPeak getOneDimPeak() {
            return oneDimPeak;
        }
    }
}
