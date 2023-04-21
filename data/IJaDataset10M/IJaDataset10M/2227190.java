package net.sf.mzmine.modules.peaklistmethods.isotopes.isotopepatternscore;

import java.util.ArrayList;
import java.util.Arrays;
import net.sf.mzmine.data.DataPoint;
import net.sf.mzmine.data.IsotopePattern;
import net.sf.mzmine.data.impl.SimpleDataPoint;
import net.sf.mzmine.modules.peaklistmethods.isotopes.isotopeprediction.IsotopePatternCalculator;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.parameters.parametertypes.MZTolerance;
import net.sf.mzmine.util.DataPointSorter;
import net.sf.mzmine.util.Range;
import net.sf.mzmine.util.SortingDirection;
import net.sf.mzmine.util.SortingProperty;

public class IsotopePatternScoreCalculator {

    public static boolean checkMatch(IsotopePattern ip1, IsotopePattern ip2, ParameterSet parameters) {
        double score = getSimilarityScore(ip1, ip2, parameters);
        double minimumScore = parameters.getParameter(IsotopePatternScoreParameters.isotopePatternScoreThreshold).getValue();
        return score >= minimumScore;
    }

    /**
     * Returns a calculated similarity score of two isotope patterns in the
     * range of 0 (not similar at all) to 1 (100% same).
     */
    public static double getSimilarityScore(IsotopePattern ip1, IsotopePattern ip2, ParameterSet parameters) {
        assert ip1 != null;
        assert ip2 != null;
        MZTolerance mzTolerance = parameters.getParameter(IsotopePatternScoreParameters.mzTolerance).getValue();
        assert mzTolerance != null;
        final double patternIntensity = Math.max(ip1.getHighestIsotope().getIntensity(), ip2.getHighestIsotope().getIntensity());
        final double noiseIntensity = parameters.getParameter(IsotopePatternScoreParameters.isotopeNoiseLevel).getValue();
        IsotopePattern nip1 = IsotopePatternCalculator.normalizeIsotopePattern(ip1);
        IsotopePattern nip2 = IsotopePatternCalculator.normalizeIsotopePattern(ip2);
        ArrayList<DataPoint> mergedDataPoints = new ArrayList<DataPoint>();
        for (DataPoint dp : nip1.getDataPoints()) {
            if (dp.getIntensity() * patternIntensity < noiseIntensity) continue;
            mergedDataPoints.add(dp);
        }
        for (DataPoint dp : nip2.getDataPoints()) {
            if (dp.getIntensity() * patternIntensity < noiseIntensity) continue;
            DataPoint negativeDP = new SimpleDataPoint(dp.getMZ(), dp.getIntensity() * -1);
            mergedDataPoints.add(negativeDP);
        }
        DataPoint mergedDPArray[] = mergedDataPoints.toArray(new DataPoint[0]);
        Arrays.sort(mergedDPArray, new DataPointSorter(SortingProperty.MZ, SortingDirection.Ascending));
        for (int i = 0; i < mergedDPArray.length - 1; i++) {
            Range toleranceRange = mzTolerance.getToleranceRange(mergedDPArray[i].getMZ());
            if (!toleranceRange.contains(mergedDPArray[i + 1].getMZ())) continue;
            double summedIntensity = mergedDPArray[i].getIntensity() + mergedDPArray[i + 1].getIntensity();
            double newMZ = mergedDPArray[i + 1].getMZ();
            mergedDPArray[i + 1] = new SimpleDataPoint(newMZ, summedIntensity);
            mergedDPArray[i] = null;
        }
        double result = 1;
        for (DataPoint dp : mergedDPArray) {
            if (dp == null) continue;
            double remainingIntensity = Math.abs(dp.getIntensity());
            if (remainingIntensity > 1) remainingIntensity = 1;
            result *= 1 - remainingIntensity;
        }
        return result;
    }
}
