package it.southdown.avana.typing;

public class TypingConfig {

    private double minFragmentLength;

    private int minClusterSize;

    private double minCumulativeVariantFreq;

    private double minSitesFraction;

    private double minSequenceScore;

    private String evaluator;

    private double minSplitMi;

    private double peakThreshold;

    private int minPeaks;

    public TypingConfig(double minFragmentLength, int minClusterSize, double minCumulativeVariantFreq, double minSitesFraction, double minSequenceScore, String evaluator, double minSplitMi, double peakThreshold, int minPeaks) {
        this.minFragmentLength = minFragmentLength;
        this.minClusterSize = minClusterSize;
        this.minCumulativeVariantFreq = minCumulativeVariantFreq;
        this.minSitesFraction = minSitesFraction;
        this.minSequenceScore = minSequenceScore;
        this.evaluator = evaluator;
        this.minSplitMi = minSplitMi;
        this.peakThreshold = peakThreshold;
        this.minPeaks = minPeaks;
    }

    public double getMinFragmentLength() {
        return minFragmentLength;
    }

    public int getMinClusterSize() {
        return minClusterSize;
    }

    public void setMinClusterSize(int minClusterSize) {
        this.minClusterSize = minClusterSize;
    }

    public double getMinCumulativeVariantFreq() {
        return minCumulativeVariantFreq;
    }

    public void setMinCumulativeVariantFreq(double minComulativeVariantFreq) {
        this.minCumulativeVariantFreq = minComulativeVariantFreq;
    }

    public double getMinSitesFraction() {
        return minSitesFraction;
    }

    public void setMinSitesFraction(double minSitesFraction) {
        this.minSitesFraction = minSitesFraction;
    }

    public double getMinSequenceScore() {
        return minSequenceScore;
    }

    public void setMinSequenceScore(double minSequenceScore) {
        this.minSequenceScore = minSequenceScore;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public double getMinSplitMi() {
        return minSplitMi;
    }

    public void setMinSplitMi(double minSplitMi) {
        this.minSplitMi = minSplitMi;
    }

    public double getPeakThreshold() {
        return peakThreshold;
    }

    public void setPeakThreshold(double peakThreshold) {
        this.peakThreshold = peakThreshold;
    }

    public int getMinPeaks() {
        return minPeaks;
    }

    public void setMinPeaks(int minPeaks) {
        this.minPeaks = minPeaks;
    }

    public String toString() {
        return ("TypingConfig" + ": minFragmentLength = " + minFragmentLength + ", minClusterSize = " + minClusterSize + ", minCumulativeVariantFreq = " + minCumulativeVariantFreq + ", minSequenceScore = " + minSequenceScore + ", evaluator = " + evaluator + ", minSplitMi = " + minSplitMi + ", peakThreshold = " + peakThreshold + ", minPeaks = " + minPeaks);
    }
}
