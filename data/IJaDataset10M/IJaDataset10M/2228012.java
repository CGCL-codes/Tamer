package ontologizer.statistics;

import java.util.Arrays;
import java.util.HashMap;

public class WestfallYoungStepDownCachedOld extends AbstractTestCorrection implements IResampling {

    /** Specifies the number of resampling steps */
    private int numberOfResamplingSteps = 1000;

    private HashMap<Integer, PValue[][]> sampledPValuesPerSize = new HashMap<Integer, PValue[][]>();

    public String getDescription() {
        return null;
    }

    public String getName() {
        return "Westfall-Young-Step-Down-Cached-Old";
    }

    /**
	 * 
	 * @author Sebastian Bauer
	 *
	 * Class models a double value entry and its index of
	 * a source array.
	 *
	 */
    private class Entry implements Comparable<Entry> {

        public double value;

        public int index;

        public int compareTo(Entry o) {
            if (value < o.value) return -1;
            if (value == o.value) return 0;
            return 1;
        }
    }

    ;

    public PValue[] adjustPValues(IPValueCalculation pvalueCalc) {
        int i;
        PValue[] rawP = pvalueCalc.calculateRawPValues();
        double[] q = new double[rawP.length];
        int[] count = new int[rawP.length];
        int m = rawP.length;
        int r[] = new int[m];
        Entry[] sortedRawPValues = new Entry[m];
        for (i = 0; i < m; i++) {
            sortedRawPValues[i] = new Entry();
            sortedRawPValues[i].value = rawP[i].p;
            sortedRawPValues[i].index = i;
        }
        Arrays.sort(sortedRawPValues);
        for (i = 0; i < m; i++) r[i] = sortedRawPValues[i].index;
        int studySetSize = pvalueCalc.currentStudySetSize();
        PValue[][] randomSampledPValues = new PValue[numberOfResamplingSteps][m];
        if (sampledPValuesPerSize.containsKey(studySetSize)) {
            System.out.println("Using available samples for study set size " + studySetSize);
            randomSampledPValues = sampledPValuesPerSize.get(studySetSize);
        } else {
            System.out.println("Sampling for study set size " + studySetSize + "\nThis may take a while...");
            for (int b = 0; b < numberOfResamplingSteps; b++) {
                randomSampledPValues[b] = pvalueCalc.calculateRandomPValues();
                System.out.print("created " + b + " samples out of " + numberOfResamplingSteps + "\r");
            }
            sampledPValuesPerSize.put(studySetSize, randomSampledPValues);
        }
        for (int b = 0; b < numberOfResamplingSteps; b++) {
            PValue[] randomRawP = randomSampledPValues[b];
            q[m - 1] = randomRawP[r[m - 1]].p;
            for (i = m - 2; i >= 0; i--) q[i] = Math.min(q[i + 1], randomRawP[r[i]].p);
            for (i = 0; i < m; i++) {
                if (q[i] <= rawP[r[i]].p) count[i]++;
            }
        }
        int c = count[0];
        for (i = 1; i < m; i++) c = count[i] = Math.max(1, Math.max(c, count[i]));
        for (i = 0; i < m; i++) {
            rawP[r[i]].p_adjusted = ((double) count[i]) / numberOfResamplingSteps;
        }
        return rawP;
    }

    public void setNumberOfResamplingSteps(int n) {
        if (n != numberOfResamplingSteps) {
            numberOfResamplingSteps = n;
            sampledPValuesPerSize = new HashMap<Integer, PValue[][]>();
        }
    }

    public int getNumberOfResamplingSteps() {
        return numberOfResamplingSteps;
    }

    public void resetCache() {
        sampledPValuesPerSize = new HashMap<Integer, PValue[][]>();
    }

    public int getSizeTolerance() {
        return 0;
    }

    public void setSizeTolerance(int t) {
    }
}
