package jy2.common.db;

/**
 *
 * @author yehuitragasitscs
 */
public class FieldDef {

    private boolean required = false;

    private int maxLength = 0;

    private String minValue = null;

    private String maxValue = null;

    private boolean number = false;

    /** Creates a new instance of Domain */
    public FieldDef() {
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isNumber() {
        return number;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }
}
