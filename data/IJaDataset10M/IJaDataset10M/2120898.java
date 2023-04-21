package spamwatch.gui.util;

public class NumberParameter extends Parameter {

    public static final String PROPERTY_VALUE = "PropValue";

    private int minValue = 0;

    private int maxValue = 0;

    private int value = 0;

    public NumberParameter(String name, int minValue, int maxValue, int value) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isValid() {
        return (value >= minValue && value <= maxValue);
    }

    public void setValue(int newValue) {
        int oldValue = this.value;
        this.value = newValue;
        fireChangeEvent(PROPERTY_VALUE, oldValue, newValue);
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
