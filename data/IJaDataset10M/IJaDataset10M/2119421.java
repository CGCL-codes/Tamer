package org.systemsbiology.qualscore.features;

import org.isb.mzxml.Scan;

public class OldSignalNoise {

    public static String[] getNames() {
        return new String[] { "Isotope signal", "Isotope noise", "Isotope S/N ratio" };
    }

    public static double[] findIsotopes(Scan s, boolean singlyCharged) {
        float[][] peakList = s.getPeakList();
        double score1 = 0;
        double score2 = 0;
        double score3 = 0;
        double cutoff = s.getTotIonCurrent() / (s.getPeaksCount() + 1);
        for (int i = 0; i < peakList.length; i++) {
            if (peakList[i][1] > cutoff) {
                score1 += 1;
            } else {
                score2 += 1;
            }
        }
        score3 = Math.sqrt(Math.sqrt(score1 / (score2 + 1))) * 10;
        score1 = Math.sqrt(Math.sqrt(score1)) * 10;
        score2 = Math.sqrt(Math.sqrt(score2)) * 10;
        return new double[] { score1, score2, score3 };
    }
}
