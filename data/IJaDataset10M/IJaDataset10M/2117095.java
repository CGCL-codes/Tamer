package org.jcvi.vics.model.vo;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Aug 28, 2006
 * Time: 10:58:00 AM
 */
public class DoubleParameterVO extends ParameterVO {

    public static final String PARAM_DOUBLE = "Double";

    private Double minValue = Double.MIN_VALUE;

    private Double maxValue = Double.MAX_VALUE;

    private Double actualValue;

    /**
     * Required for GWT
     */
    public DoubleParameterVO() {
        super();
    }

    /**
     * Manual boxing
     */
    public DoubleParameterVO(double defaultValue) {
        this(new Double(defaultValue));
    }

    public DoubleParameterVO(Double defaultValue) {
        this.actualValue = defaultValue;
    }

    public String getStringValue() {
        return actualValue.toString();
    }

    /**
     * Manual boxing
     */
    public DoubleParameterVO(double minValue, double maxValue, double defaultValue) {
        this(new Double(minValue), new Double(maxValue), new Double(defaultValue));
    }

    public DoubleParameterVO(Double minValue, Double maxValue, Double defaultValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.actualValue = defaultValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    private void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    private void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public void setActualValue(Double newValue) throws ParameterException {
        this.actualValue = newValue;
    }

    public Double getActualValue() {
        return actualValue;
    }

    public boolean isValid() {
        return null != actualValue && !(null == minValue || null == maxValue || minValue > actualValue || maxValue < actualValue);
    }

    public String getType() {
        return PARAM_DOUBLE;
    }

    public String toString() {
        return "DoubleParameterVO{" + "minValue=" + minValue + "maxValue=" + maxValue + "actualValue=" + actualValue + '}';
    }
}
