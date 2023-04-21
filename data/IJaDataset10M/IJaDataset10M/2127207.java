package edu.utah.seq.data;

import java.io.*;

/**Chromosome specific container for window level results from EnrichedRegionMaker.*/
public class SmoothingWindowInfo implements Serializable {

    private SmoothingWindow[] sm;

    private Info info;

    public static final long serialVersionUID = 2;

    public SmoothingWindowInfo(SmoothingWindow[] sm, Info info) {
        this.sm = sm;
        this.info = info;
    }

    /**Returns min and max of the scores for a given scoreIndex.*/
    public static float[] minMax(SmoothingWindowInfo[] swi, int scoreIndex) {
        float min = swi[0].getSm()[0].getScores()[scoreIndex];
        float max = min;
        for (int c = 0; c < swi.length; c++) {
            SmoothingWindow[] windows = swi[c].getSm();
            for (int i = 0; i < windows.length; i++) {
                float score = windows[i].getScores()[scoreIndex];
                if (score > max) max = score; else if (score < min) min = score;
            }
        }
        return new float[] { min, max };
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public SmoothingWindow[] getSm() {
        return sm;
    }

    public void setSm(SmoothingWindow[] sm) {
        this.sm = sm;
    }
}
