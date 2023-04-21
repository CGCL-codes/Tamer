package javax.time.calendar;

import javax.time.Duration;

/**
 * Mock chronology.
 *
 * @author Stephen Colebourne
 */
public final class MockOtherChronology extends Chronology {

    /** Singleton instance. */
    public static final MockOtherChronology INSTANCE = new MockOtherChronology();

    /** A serialization identifier for this class. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    private MockOtherChronology() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "Mock";
    }

    public static PeriodUnit OTHER_MONTHS = new Unit();

    private static class Unit extends PeriodUnit {

        private static final long serialVersionUID = 1L;

        Unit() {
            super("OtherMonths", Duration.ofStandardDays(29));
        }
    }

    ;
}
