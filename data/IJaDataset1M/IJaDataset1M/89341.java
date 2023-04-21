package edu.utah.seq.data;

import java.io.*;

public class EnrichedRegion implements Serializable {

    private String chromosome;

    private int start;

    private int stop;

    private int numberOfWindows;

    private SmoothingWindow bestWindow;

    private String sequence;

    private Point[] treatmentPoints;

    private Point[] controlPoints;

    private SmoothingWindow[] subWindows;

    private SmoothingWindow bestSubWindow;

    private float binomialPValue;

    private float log2Ratio;

    private int numberUniqueTreatmentObservations;

    private int numberUniqueControlObservations;

    private int[] counts;

    public static final long serialVersionUID = 3;

    public EnrichedRegion(SmoothingWindow window, String chromosome) {
        chromosome = this.chromosome = chromosome;
        start = window.getStart();
        stop = window.getStop();
        bestWindow = window;
    }

    public EnrichedRegion(SmoothingWindow window) {
        start = window.getStart();
        stop = window.getStop();
    }

    public EnrichedRegion() {
    }

    ;

    public String getBed5(int scoreIndex) {
        float score = bestWindow.getScores()[scoreIndex];
        return chromosome + "\t" + start + "\t" + stop + "\t.\t" + score + "\t.";
    }

    public SmoothingWindow getBestWindow() {
        return bestWindow;
    }

    public void setBestWindow(SmoothingWindow bestWindow) {
        this.bestWindow = bestWindow;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getNumberOfWindows() {
        return numberOfWindows;
    }

    public void setNumberOfWindows(int numberOfWindows) {
        this.numberOfWindows = numberOfWindows;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public Point[] getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(Point[] controlPoints) {
        this.controlPoints = controlPoints;
    }

    public Point[] getTreatmentPoints() {
        return treatmentPoints;
    }

    public void setTreatmentPoints(Point[] treatmentPoints) {
        this.treatmentPoints = treatmentPoints;
    }

    public SmoothingWindow[] getSubWindows() {
        return subWindows;
    }

    public void setSubWindows(SmoothingWindow[] subWindows) {
        this.subWindows = subWindows;
    }

    public SmoothingWindow getBestSubWindow() {
        return bestSubWindow;
    }

    public void setBestSubWindow(SmoothingWindow bestSubWindow) {
        this.bestSubWindow = bestSubWindow;
    }

    public float getBinomialPValue() {
        return binomialPValue;
    }

    public void setBinomialPValue(float binomialPValue) {
        this.binomialPValue = binomialPValue;
    }

    public float getLog2Ratio() {
        return log2Ratio;
    }

    public void setLog2Ratio(float log2Ratio) {
        this.log2Ratio = log2Ratio;
    }

    public int getNumberUniqueTreatmentObservations() {
        return numberUniqueTreatmentObservations;
    }

    public void setNumberUniqueTreatmentObservations(int numberUniqueTreatmentObservations) {
        this.numberUniqueTreatmentObservations = numberUniqueTreatmentObservations;
    }

    public int getNumberUniqueControlObservations() {
        return numberUniqueControlObservations;
    }

    public void setNumberUniqueControlObservations(int numberUniqueControlObservations) {
        this.numberUniqueControlObservations = numberUniqueControlObservations;
    }

    public int[] getCounts() {
        return counts;
    }

    public void setCounts(int[] counts) {
        this.counts = counts;
    }
}
