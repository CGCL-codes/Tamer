package no.ugland.utransprod.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DeviationSumJobFunctionV extends BaseObject {

    private DeviationSumJobFunctionVPK deviationSumJobFunctionVPK;

    private Integer countDeviations;

    public DeviationSumJobFunctionV() {
    }

    public DeviationSumJobFunctionV(String productAreaGroupName, Integer registrationYear, Integer registrationWeek, String deviationFunction, String functionCategoryName, Integer countDeviation) {
        deviationSumJobFunctionVPK = new DeviationSumJobFunctionVPK(registrationYear, registrationWeek, deviationFunction, functionCategoryName, productAreaGroupName);
        this.countDeviations = countDeviation;
    }

    public Integer getRegistrationYear() {
        return deviationSumJobFunctionVPK.getRegistrationYear();
    }

    public Integer getMonth() {
        return deviationSumJobFunctionVPK.getMonth();
    }

    public Integer getRegistrationWeek() {
        return deviationSumJobFunctionVPK.getRegistrationWeek();
    }

    public String getDeviationFunction() {
        return deviationSumJobFunctionVPK.getDeviationFunction();
    }

    public String getFunctionCategoryName() {
        return deviationSumJobFunctionVPK.getFunctionCategoryName();
    }

    public Integer getCountDeviations() {
        return countDeviations;
    }

    public void setCountDeviations(Integer countDeviations) {
        this.countDeviations = countDeviations;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DeviationSumJobFunctionV)) return false;
        DeviationSumJobFunctionV castOther = (DeviationSumJobFunctionV) other;
        return new EqualsBuilder().append(deviationSumJobFunctionVPK, castOther.deviationSumJobFunctionVPK).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deviationSumJobFunctionVPK).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deviationSumJobFunctionVPK", deviationSumJobFunctionVPK).append("countDeviations", countDeviations).toString();
    }

    public DeviationSumJobFunctionVPK getDeviationSumJobFunctionVPK() {
        return deviationSumJobFunctionVPK;
    }

    public void setDeviationSumJobFunctionVPK(DeviationSumJobFunctionVPK deviationSumJobFunctionVPK) {
        this.deviationSumJobFunctionVPK = deviationSumJobFunctionVPK;
    }
}
