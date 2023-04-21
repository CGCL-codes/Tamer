package FinalModeler;

import arch.IModeler.ModelerResult;

/**
 * This class is an extension of the regular <code>ModelerResult</code> class.</br>
 * This extension is needed for holding impressions for each users focus level.
 */
public class ImprovedModelerResult extends ModelerResult {

    private int isImpressions;

    private int f0Impressions;

    private int f1Impressions;

    private int f2Impressions;

    public ImprovedModelerResult(double cpc, int isImpressions, int f0Impressions, int f1Impressions, int f2Impressions, double position) {
        super(cpc, isImpressions + f0Impressions + f1Impressions + f2Impressions, position);
        this.isImpressions = isImpressions;
        this.f0Impressions = f0Impressions;
        this.f1Impressions = f1Impressions;
        this.f2Impressions = f2Impressions;
    }

    public double getImpressions() {
        return isImpressions + f0Impressions + f1Impressions + f2Impressions;
    }

    public double getIsImpressions() {
        return isImpressions;
    }

    public void setIsImpressions(int isImpressions) {
        this.isImpressions = isImpressions;
    }

    public double getF0Impressions() {
        return f0Impressions;
    }

    public void setF0Impressions(int f0Impressions) {
        this.f0Impressions = f0Impressions;
    }

    public double getF1Impressions() {
        return f1Impressions;
    }

    public void setF1Impressions(int f1Impressions) {
        this.f1Impressions = f1Impressions;
    }

    public double getF2Impressions() {
        return f2Impressions;
    }

    public void setF2Impressions(int f2Impressions) {
        this.f2Impressions = f2Impressions;
    }
}
