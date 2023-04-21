package phex.prefs;

public class RangeSetting<T extends Comparable<T>> extends Setting<T> {

    protected final T minValue;

    protected final T maxValue;

    /**
     * @param value
     * @param defaultValue
     * @param minValue
     * @param maxValue
     */
    public RangeSetting(String key, T value, T defaultValue, T minValue, T maxValue, Preferences preferences) {
        super(key, value, defaultValue, preferences);
        if (maxValue == null || minValue == null) {
            throw new NullPointerException("Min or max value is null");
        }
        if (maxValue.compareTo(minValue) < 0) {
            throw new IllegalArgumentException("Max less then min.");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public T max() {
        return maxValue;
    }

    @Override
    public void set(T newValue) {
        if (newValue.compareTo(maxValue) > 0) {
            super.set(maxValue);
        } else if (newValue.compareTo(minValue) < 0) {
            super.set(minValue);
        } else {
            super.set(newValue);
        }
    }
}
