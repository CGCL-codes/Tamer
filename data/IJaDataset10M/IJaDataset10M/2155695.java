package javax.time.calendar;

/**
 * Strategy for adjusting a date.
 * <p>
 * This interface provides a common way to access many different date
 * adjustments. Examples might be an adjuster that sets the date avoiding
 * weekends, or one that sets the date to the last day of the month.
 * <p>
 * DateAdjuster is an interface and must be implemented with care
 * to ensure other classes in the framework operate correctly.
 * All instantiable implementations must be final, immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
public interface DateAdjuster {

    /**
     * Adjusts the input date returning the adjusted date.
     * <p>
     * This is a strategy pattern that allows a range of adjustments to be made
     * to a date.
     *
     * @param date  the date to adjust, not null
     * @return the adjusted date, not null
     */
    LocalDate adjustDate(LocalDate date);
}
