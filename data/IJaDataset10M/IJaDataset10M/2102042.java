package net.sf.mzmine.modules.peaklistmethods.gapfilling.peakfinder;

import java.util.List;
import java.util.Vector;
import net.sf.mzmine.data.DataPoint;
import net.sf.mzmine.data.PeakListRow;
import net.sf.mzmine.data.PeakStatus;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.data.Scan;
import net.sf.mzmine.data.impl.SimpleChromatographicPeak;
import net.sf.mzmine.data.impl.SimpleDataPoint;
import net.sf.mzmine.util.Range;
import net.sf.mzmine.util.ScanUtils;

class Gap {

    private PeakListRow peakListRow;

    private RawDataFile rawDataFile;

    private Range mzRange, rtRange;

    private double intTolerance;

    private List<GapDataPoint> currentPeakDataPoints;

    private List<GapDataPoint> bestPeakDataPoints;

    private double bestPeakHeight;

    /**
	 * Constructor: Initializes an empty gap
	 * 
	 * @param mz
	 *            M/Z coordinate of this empty gap
	 * @param rt
	 *            RT coordinate of this empty gap
	 */
    Gap(PeakListRow peakListRow, RawDataFile rawDataFile, Range mzRange, Range rtRange, double intTolerance) {
        this.peakListRow = peakListRow;
        this.rawDataFile = rawDataFile;
        this.intTolerance = intTolerance;
        this.mzRange = mzRange;
        this.rtRange = rtRange;
    }

    void offerNextScan(Scan scan) {
        double scanRT = scan.getRetentionTime();
        if (scanRT < rtRange.getMin()) return;
        if ((scanRT > rtRange.getMax()) && (currentPeakDataPoints == null)) return;
        DataPoint basePeak = ScanUtils.findBasePeak(scan, mzRange);
        GapDataPoint currentDataPoint;
        if (basePeak != null) {
            currentDataPoint = new GapDataPoint(scan.getScanNumber(), basePeak.getMZ(), scanRT, basePeak.getIntensity());
        } else {
            currentDataPoint = new GapDataPoint(scan.getScanNumber(), mzRange.getAverage(), scanRT, 0);
        }
        if (currentPeakDataPoints == null) {
            currentPeakDataPoints = new Vector<GapDataPoint>();
            currentPeakDataPoints.add(currentDataPoint);
            return;
        }
        if (checkRTShape(currentDataPoint)) {
            currentPeakDataPoints.add(currentDataPoint);
        } else {
            if (currentPeakDataPoints != null) {
                checkCurrentPeak();
                currentPeakDataPoints = null;
            }
        }
    }

    public void noMoreOffers() {
        if (currentPeakDataPoints != null) {
            checkCurrentPeak();
            currentPeakDataPoints = null;
        }
        if (bestPeakDataPoints != null) {
            double area = 0, height = 0, mz = 0, rt = 0;
            int scanNumbers[] = new int[bestPeakDataPoints.size()];
            DataPoint finalDataPoint[] = new DataPoint[bestPeakDataPoints.size()];
            Range finalRTRange = null, finalMZRange = null, finalIntensityRange = null;
            int representativeScan = 0;
            for (int i = 0; i < bestPeakDataPoints.size(); i++) {
                GapDataPoint dp = bestPeakDataPoints.get(i);
                if (i == 0) {
                    finalRTRange = new Range(dp.getRT());
                    finalMZRange = new Range(dp.getMZ());
                    finalIntensityRange = new Range(dp.getIntensity());
                } else {
                    assert finalRTRange != null && finalMZRange != null && finalIntensityRange != null;
                    finalRTRange.extendRange(dp.getRT());
                    finalMZRange.extendRange(dp.getMZ());
                    finalIntensityRange.extendRange(dp.getIntensity());
                }
                scanNumbers[i] = bestPeakDataPoints.get(i).getScanNumber();
                finalDataPoint[i] = new SimpleDataPoint(dp.getMZ(), dp.getIntensity());
                mz += bestPeakDataPoints.get(i).getMZ();
                if (bestPeakDataPoints.get(i).getIntensity() > height) {
                    height = bestPeakDataPoints.get(i).getIntensity();
                    rt = bestPeakDataPoints.get(i).getRT();
                    representativeScan = bestPeakDataPoints.get(i).getScanNumber();
                }
                if (i == bestPeakDataPoints.size() - 1) break;
                double rtDifference = bestPeakDataPoints.get(i + 1).getRT() - bestPeakDataPoints.get(i).getRT();
                double intensityStart = bestPeakDataPoints.get(i).getIntensity();
                double intensityEnd = bestPeakDataPoints.get(i + 1).getIntensity();
                area += (rtDifference * (intensityStart + intensityEnd) / 2);
            }
            mz /= bestPeakDataPoints.size();
            int fragmentScan = ScanUtils.findBestFragmentScan(rawDataFile, finalRTRange, finalMZRange);
            SimpleChromatographicPeak newPeak = new SimpleChromatographicPeak(rawDataFile, mz, rt, height, area, scanNumbers, finalDataPoint, PeakStatus.ESTIMATED, representativeScan, fragmentScan, finalRTRange, finalMZRange, finalIntensityRange);
            peakListRow.addPeak(rawDataFile, newPeak);
        }
    }

    /**
	 * This function check for the shape of the peak in RT direction, and
	 * determines if it is possible to add given m/z peak at the end of the
	 * peak.
	 */
    private boolean checkRTShape(GapDataPoint dp) {
        if (dp.getRT() < rtRange.getMin()) {
            double prevInt = currentPeakDataPoints.get(currentPeakDataPoints.size() - 1).getIntensity();
            if (dp.getIntensity() > (prevInt * (1 - intTolerance))) {
                return true;
            }
        }
        if (rtRange.contains(dp.getRT())) {
            return true;
        }
        if (dp.getRT() > rtRange.getMax()) {
            double prevInt = currentPeakDataPoints.get(currentPeakDataPoints.size() - 1).getIntensity();
            if (dp.getIntensity() < (prevInt * (1 + intTolerance))) {
                return true;
            }
        }
        return false;
    }

    private void checkCurrentPeak() {
        int highestMaximumInd = -1;
        double currentMaxHeight = 0f;
        for (int i = 1; i < currentPeakDataPoints.size() - 1; i++) {
            if (rtRange.contains(currentPeakDataPoints.get(i).getRT())) {
                if ((currentPeakDataPoints.get(i).getIntensity() >= currentPeakDataPoints.get(i + 1).getIntensity()) && (currentPeakDataPoints.get(i).getIntensity() >= currentPeakDataPoints.get(i - 1).getIntensity())) {
                    if (currentPeakDataPoints.get(i).getIntensity() > currentMaxHeight) {
                        currentMaxHeight = currentPeakDataPoints.get(i).getIntensity();
                        highestMaximumInd = i;
                    }
                }
            }
        }
        if (highestMaximumInd == -1) return;
        int startInd = highestMaximumInd;
        double currentInt = currentPeakDataPoints.get(startInd).getIntensity();
        while (startInd > 0) {
            double nextInt = currentPeakDataPoints.get(startInd - 1).getIntensity();
            if (currentInt < (nextInt * (1 - intTolerance))) break;
            startInd--;
            currentInt = nextInt;
        }
        int stopInd = highestMaximumInd;
        currentInt = currentPeakDataPoints.get(stopInd).getIntensity();
        while (stopInd < (currentPeakDataPoints.size() - 1)) {
            double nextInt = currentPeakDataPoints.get(stopInd + 1).getIntensity();
            if (nextInt > (currentInt * (1 + intTolerance))) break;
            stopInd++;
            currentInt = nextInt;
        }
        if ((bestPeakDataPoints == null) || (bestPeakHeight < currentMaxHeight)) {
            bestPeakDataPoints = currentPeakDataPoints.subList(startInd, stopInd);
        }
    }
}
