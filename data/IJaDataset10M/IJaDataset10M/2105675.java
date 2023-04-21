package hu.akarnokd.reactive4java;

/**
 * An immutable record holding a value and a
 * time interval value.
 * @author akarnokd, 2011.01.29.
 * @param <T> the contained type
 */
public final class TimeInterval<T> {

    /** The value. */
    private final T value;

    /** The timestamp. */
    private final long interval;

    /**
	 * Construct a value with a time inverval.
	 * @param value the value
	 * @param interval the time interval
	 */
    public TimeInterval(T value, long interval) {
        this.value = value;
        this.interval = interval;
    }

    /** @return the contained value */
    public T value() {
        return value;
    }

    /** @return the associated timestamp. */
    public long interval() {
        return interval;
    }

    /**
	 * A type inference helper to construct a new TimeInterval value.
	 * @param <T> the type of the value
	 * @param value the value
	 * @param interval the time interval
	 * @return the timestamped object
	 */
    public static <T> TimeInterval<T> of(T value, long interval) {
        return new TimeInterval<T>(value, interval);
    }

    /**
	 * A type inference helper to construct a new TimeInterval value from another
	 * timestamped value by keeping the value and assigning a new value.
	 * @param <T> the type of the value
	 * @param value the value
	 * @param interval the time interval
	 * @return the timestamped object
	 */
    public static <T> TimeInterval<T> of(TimeInterval<T> value, long interval) {
        return new TimeInterval<T>(value.value(), interval);
    }

    @Override
    public String toString() {
        return value + " delta " + interval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeInterval) {
            TimeInterval<?> that = (TimeInterval<?>) obj;
            return (this.value == that.value || (this.value != null && this.value.equals(that.value))) && this.interval == that.interval;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (17 + (value != null ? value.hashCode() : 0)) * 31 + (int) ((interval >> 32) ^ (interval & 0xFFFFFFFFL));
    }
}
