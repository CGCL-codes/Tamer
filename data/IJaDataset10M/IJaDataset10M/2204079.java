package genomeUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import nutils.ArrayUtils;
import nutils.CompareUtils;
import nutils.IOUtils;
import nutils.NumberUtils;

public class DecimalCounter {

    public static class LodCounter {

        private static final int SpaceMultiplier = 10;

        private int mDecimalMultiplier;

        private ArrayList<LodCounterCountPackage> mLodList;

        private LodCounterCountPackage mDummyPackage;

        public LodCounter(int numDecimalsAllowed) {
            numDecimalsAllowed = Math.max(0, numDecimalsAllowed);
            mDecimalMultiplier = (int) Math.round(Math.pow(10, numDecimalsAllowed));
            mLodList = new ArrayList<LodCounterCountPackage>(mDecimalMultiplier * SpaceMultiplier);
            mDummyPackage = new LodCounterCountPackage();
        }

        public void submitLodScore(double lodScore) {
            int lodAdjusted = getAdjustedDouble(lodScore);
            mDummyPackage.mLodValue = lodAdjusted;
            int result = Collections.binarySearch(mLodList, mDummyPackage);
            if (result >= 0) {
                LodCounterCountPackage lccp = mLodList.get(result);
                lccp.mCount++;
            } else {
                LodCounterCountPackage lccp = new LodCounterCountPackage();
                lccp.mLodValue = lodAdjusted;
                lccp.mCount = 1;
                int insertionPoint = -1 * (result + 1);
                mLodList.add(insertionPoint, lccp);
            }
        }

        public LodListWithCounts getLodWithCounts() {
            LodListWithCounts llwc = new LodListWithCounts(mLodList.size());
            Iterator<LodCounterCountPackage> iter = mLodList.iterator();
            for (int i = 0; i < mLodList.size(); i++) {
                LodCounterCountPackage lccp = iter.next();
                llwc.mLodScores[i] = getDeadjustedDouble(lccp.mLodValue);
                llwc.mCounts[i] = lccp.mCount;
            }
            llwc.mCumulativeCountsForward = getCumulativeCounts(llwc.mCounts, true);
            llwc.mCumulativeCountsBackward = getCumulativeCounts(llwc.mCounts, false);
            return llwc;
        }

        private int getAdjustedDouble(double value) {
            return (int) Math.round(value * mDecimalMultiplier);
        }

        private double getDeadjustedDouble(int value) {
            return (value / ((double) mDecimalMultiplier));
        }

        public static int[] getCumulativeCounts(int[] theArray, boolean goForward) {
            int[] rV = theArray.clone();
            if (goForward) {
                for (int i = 1; i < rV.length; i++) {
                    rV[i] += rV[i - 1];
                }
            } else {
                for (int i = rV.length - 2; i >= 0; i--) {
                    rV[i] += rV[i + 1];
                }
            }
            return rV;
        }

        public static void Test() {
            LodCounter lodCounter = new LodCounter(2);
            lodCounter.submitLodScore(7.135);
            lodCounter.submitLodScore(7.1399);
            lodCounter.submitLodScore(5.1);
            lodCounter.submitLodScore(.91);
            lodCounter.submitLodScore(.913);
            lodCounter.submitLodScore(15.3);
            lodCounter.submitLodScore(0.02);
            lodCounter.submitLodScore(0.017);
            lodCounter.submitLodScore(0.005);
            lodCounter.submitLodScore(0.004);
            lodCounter.submitLodScore(0.010006);
            LodListWithCounts llwc = lodCounter.getLodWithCounts();
            for (int i = 0; i < llwc.mLodScores.length; i++) {
                System.out.println("" + llwc.mLodScores[i] + "\t" + llwc.mCounts[i] + "\t" + llwc.mCumulativeCountsForward[i] + "\t" + llwc.mCumulativeCountsBackward[i]);
            }
        }
    }

    public static class LodListWithCounts {

        public double[] mLodScores;

        public int[] mCounts;

        public int[] mCumulativeCountsForward;

        public int[] mCumulativeCountsBackward;

        public LodListWithCounts(int arraySize) {
            mLodScores = new double[arraySize];
            mCounts = new int[arraySize];
        }

        public static LodListWithCounts getLodListsWithCountsFromFile(String inFilename, double minLodOfInterest, double maxLodOfInterest) {
            ArrayList<String> allLines = IOUtils.readAllLinesFromFile(inFilename);
            LinkedList<Double> lodScoresList = new LinkedList<Double>();
            LinkedList<Integer> countsList = new LinkedList<Integer>();
            for (int i = 0; i < allLines.size(); i++) {
                String[] components = allLines.get(i).split("\\s");
                double lodScore = Double.parseDouble(components[0]);
                if ((lodScore >= minLodOfInterest) && (lodScore <= maxLodOfInterest)) {
                    int count = Integer.parseInt(components[1]);
                    lodScoresList.add(new Double(lodScore));
                    countsList.add(new Integer(count));
                }
            }
            LodListWithCounts llwc = new LodListWithCounts(lodScoresList.size());
            llwc.mLodScores = ArrayUtils.getPrimitiveArray(lodScoresList);
            llwc.mCounts = ArrayUtils.getPrimitiveArray(countsList);
            llwc.mCumulativeCountsForward = LodCounter.getCumulativeCounts(llwc.mCounts, true);
            llwc.mCumulativeCountsBackward = LodCounter.getCumulativeCounts(llwc.mCounts, false);
            return llwc;
        }
    }

    private static class LodCounterCountPackage implements Comparable<LodCounterCountPackage> {

        int mLodValue = 0;

        int mCount = 0;

        public int compareTo(LodCounterCountPackage rhs) {
            return CompareUtils.compareInt(rhs.mLodValue, mLodValue);
        }
    }

    public static class PValueCounter {

        private static final int GranularityDefault = 10;

        int[] pValueCounts;

        int mGranularity;

        public PValueCounter() {
            constructorHelper(GranularityDefault);
        }

        public PValueCounter(int granularityMagnitude) {
            int granularity = (int) Math.round(Math.pow(10, granularityMagnitude));
            constructorHelper(granularity);
        }

        private void constructorHelper(int granularity) {
            mGranularity = granularity;
            pValueCounts = new int[mGranularity + 1];
            Arrays.fill(pValueCounts, 0);
        }

        /** Returns the counts of the p values. */
        public int[] getPValueCounts() {
            return pValueCounts;
        }

        /** Returns the number of elements. */
        public int getNumBins() {
            return pValueCounts.length;
        }

        public int submitPValue(float p, boolean shouldRound) {
            int index = shouldRound ? Math.round(p * mGranularity) : (int) (p * mGranularity);
            return (++(pValueCounts[index]));
        }

        /** Returns the cumulative counts of the p values over the buckets. */
        public int[] getPValueCountsCumulative() {
            int[] cumCounts = (int[]) pValueCounts.clone();
            for (int i = 1; i < cumCounts.length; i++) {
                cumCounts[i] += cumCounts[i - 1];
            }
            return cumCounts;
        }

        /** Returns the cumulative log counts of the p values over the buckets. */
        public double[] getPValueCountsCumulativeLog(boolean forward) {
            int[] results = forward ? getPValueCountsCumulative() : getPValueCountsCumulativeReverse();
            double[] rV = new double[results.length];
            for (int i = 0; i < results.length; i++) {
                rV[i] = NumberUtils.MathLog10Safe(results[i]);
            }
            return rV;
        }

        /** Returns the cumulative counts of hte p values over the buckets in the reverse direction. */
        public int[] getPValueCountsCumulativeReverse() {
            int[] cumCounts = (int[]) pValueCounts.clone();
            for (int i = cumCounts.length - 2; i >= 0; i--) {
                cumCounts[i] += cumCounts[i + 1];
            }
            return cumCounts;
        }

        /** Returns the proportion of each count (over the cumulative count). */
        public float[] getPValueCountsCumulativeProportion(boolean calcInLog) {
            int[] cumCounts = getPValueCountsCumulative();
            float totalCount = (float) cumCounts[cumCounts.length - 1];
            float[] countProp = new float[cumCounts.length];
            for (int i = 0; i < countProp.length; i++) {
                countProp[i] = cumCounts[i] / totalCount;
                countProp[i] = (float) (calcInLog ? NumberUtils.MathLog10Safe(countProp[i]) : countProp[i]);
            }
            return countProp;
        }

        public static void Test() {
            PValueCounter pvc = new PValueCounter(2);
            for (int i = 0; i <= 100; i++) {
                pvc.submitPValue((float) i / (float) 100.0, true);
                pvc.submitPValue((float) i / (float) 100.0, true);
            }
            int[] counts = pvc.getPValueCounts();
            int[] cumCounts = pvc.getPValueCountsCumulative();
            for (int i = 0; i < counts.length; i++) {
                System.out.println(counts[i] + "\t" + cumCounts[i]);
            }
        }
    }

    public static class PValueCounterPackage implements Comparable<PValueCounterPackage> {

        public double mPValue;

        public double mCounts;

        public double mCountsCumulative;

        public double mProportions;

        public double mProportionsCumulative;

        public PValueCounterPackage(double pValue) {
            mPValue = pValue;
            mCounts = mCountsCumulative = mProportions = mProportionsCumulative = 0;
        }

        public int compareTo(PValueCounterPackage rhs) {
            return Double.compare(mPValue, rhs.mPValue);
        }
    }

    public static class PValueCounterSparse {

        Hashtable<Double, PValueCounterPackage> mCounterTable;

        public PValueCounterSparse() {
            mCounterTable = new Hashtable<Double, PValueCounterPackage>();
        }

        public void clear() {
            mCounterTable.clear();
        }

        /** Rounds the p-value to the 2nd non-zero digit and increments the count. */
        public int submitPValue(double p, int nthNonZeroDigit) {
            nthNonZeroDigit = Math.max(1, nthNonZeroDigit);
            int exponent = ((int) (-1 * NumberUtils.MathLog10Safe(p))) + nthNonZeroDigit;
            long multiplier = Math.round(Math.pow(10, exponent));
            long pShifted = Math.round(p * multiplier);
            double pNew = (double) pShifted / (double) multiplier;
            PValueCounterPackage pvp = mCounterTable.get(pNew);
            if (pvp == null) {
                pvp = new PValueCounterPackage(pNew);
                mCounterTable.put(pNew, pvp);
            }
            pvp.mCounts++;
            return (int) Math.round(pvp.mCounts);
        }

        /** Returns the list of p-values, counts, cumulative counts, and proportions. */
        public ArrayList<PValueCounterPackage> getCountsAndProportions(boolean reverseList) {
            ArrayList<PValueCounterPackage> pvpList = new ArrayList<PValueCounterPackage>(mCounterTable.size());
            for (Enumeration<PValueCounterPackage> e = mCounterTable.elements(); e.hasMoreElements(); ) {
                PValueCounterPackage pvp = e.nextElement();
                pvpList.add(pvp);
            }
            Collections.sort(pvpList);
            if (reverseList) {
                Collections.reverse(pvpList);
            }
            if (pvpList.size() > 0) {
                pvpList.get(0).mCountsCumulative = pvpList.get(0).mCounts;
                for (int i = 1; i < pvpList.size(); i++) {
                    PValueCounterPackage pvp = pvpList.get(i);
                    pvp.mCountsCumulative = pvpList.get(i - 1).mCountsCumulative + pvp.mCounts;
                }
                double totalCount = pvpList.get(pvpList.size() - 1).mCountsCumulative;
                pvpList.get(0).mProportions = pvpList.get(0).mCounts / totalCount;
                pvpList.get(0).mProportionsCumulative = pvpList.get(0).mProportions;
                for (int i = 1; i < pvpList.size(); i++) {
                    PValueCounterPackage pvp = pvpList.get(i);
                    pvp.mProportions = pvp.mCounts / totalCount;
                    pvp.mProportionsCumulative = pvpList.get(i - 1).mProportionsCumulative + pvp.mProportions;
                }
            }
            return pvpList;
        }

        /** Given a filename, this reads the values of a column in that filename into a pvc. If
		 *  an existing p-value counter object is provided, it is used.  Else if null is passed
		 *  in for this, a new one is created. */
        public static PValueCounterSparse readColumnValuesIntoPVC(PValueCounterSparse existingPVC, String inFilename, int columnOfInterest, boolean hasHeader, int numSignificantDigits) {
            DecimalCounter.PValueCounterSparse pvc = (existingPVC == null) ? new DecimalCounter.PValueCounterSparse() : existingPVC;
            BufferedReader in = IOUtils.getBufferedReader(inFilename);
            int lineCounter = -1;
            String line;
            while ((line = IOUtils.getNextLineInBufferedReader(in)) != null) {
                lineCounter++;
                if (hasHeader && (lineCounter == 0)) continue;
                String components[] = line.split("\\s");
                double pvalue = Double.parseDouble(components[columnOfInterest]);
                pvc.submitPValue(pvalue, numSignificantDigits);
            }
            IOUtils.closeBufferedReader(in);
            return pvc;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
