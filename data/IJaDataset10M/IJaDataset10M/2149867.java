package net.simapro.connector.model;

/**
 * The contribution of a process to a impact category of a product design
 * result.
 * 
 * @author Michael Srocka
 * 
 */
public class ProcessContributionResult {

    private String impactCategory;

    private String unit;

    private double totalAmount;

    private double processAmount;

    public ProcessContributionResult() {
    }

    public ProcessContributionResult(String impactCategory, String unit, double totalAmount, double processAmount) {
        this.impactCategory = impactCategory;
        this.unit = unit;
        this.totalAmount = totalAmount;
        this.processAmount = processAmount;
    }

    public String getImpactCategory() {
        return impactCategory;
    }

    public void setImpactCategory(String impactCategory) {
        this.impactCategory = impactCategory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getProcessAmount() {
        return processAmount;
    }

    public void setProcessAmount(double processAmount) {
        this.processAmount = processAmount;
    }

    public double getProcessContribution() {
        return totalAmount != 0 ? (processAmount / totalAmount) * 100 : 0;
    }
}
