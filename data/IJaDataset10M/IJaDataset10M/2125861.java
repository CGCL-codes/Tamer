package javax.time.calendar;

/**
 * A calendar system, consisting of rules controlling the passage of human-scale time.
 * <p>
 * Calendar systems describe a set of fields that can be used to describe time
 * in a human-scale. Typical fields include year, month-of-year and day-of-month.
 * <p>
 * This abstract base class provides a common mechanism to access the standard
 * fields which are supported in the vast majority of calendar systems.
 * Subclasses will provide the full set of fields for that calendar system.
 * <p>
 * The default chronology is {@link ISOChronology ISO8601} which is the
 * <i>de facto</i> world calendar today.
 * <p>
 * This is an abstract class and must be implemented with care to
 * ensure other classes in the framework operate correctly.
 * All instantiable subclasses must be final, immutable and thread-safe.
 * Wherever possible subclasses should be singletons with no public constructor.
 * It is recommended that subclasses implement {@code Serializable}
 *
 * @author Stephen Colebourne
 */
public abstract class Chronology implements Calendrical {

    /**
     * Restrictive constructor.
     */
    protected Chronology() {
    }

    /**
     * Gets the rule for {@code Chronology}.
     *
     * @return the rule for the chronology, not null
     */
    public static CalendricalRule<Chronology> rule() {
        return ISOCalendricalRule.CHRONOLOGY;
    }

    /**
     * Gets the value of the specified calendrical rule.
     * <p>
     * This method queries the value of the specified calendrical rule.
     * If the value cannot be returned for the rule from this offset then
     * {@code null} will be returned.
     *
     * @param rule  the rule to use, not null
     * @return the value for the rule, null if the value cannot be returned
     */
    @SuppressWarnings("unchecked")
    public <T> T get(CalendricalRule<T> rule) {
        if (rule instanceof ISOCalendricalRule<?>) {
            if (rule.equals(ISOCalendricalRule.CHRONOLOGY)) {
                return (T) this;
            }
            return null;
        }
        if (rule instanceof ISODateTimeRule) {
            return null;
        }
        return rule.derive(this);
    }

    /**
     * Gets the name of the chronology.
     * <p>
     * The name should not have the suffix 'Chronology'.
     * For example, the name of {@link ISOChronology} is 'ISO'.
     *
     * @return the name of the chronology, not null
     */
    public abstract String getName();

    /**
     * Returns a textual description of the chronology.
     *
     * @return a string form for debugging, not null
     */
    @Override
    public String toString() {
        return getName();
    }
}
