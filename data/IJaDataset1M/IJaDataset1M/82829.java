package logahawk;

import logahawk.listeners.*;
import net.jcip.annotations.*;
import java.util.*;

/**
 * Helper class to filter out log messages based on {@link Severity}.
 *
 * This class is used by classes such as {@link SeverityFilterLogger} and {@link SeverityFilterListener}. A single
 * instance of this class can be shared with multiple classes to consolidate filtering.
 *
 * @see SeverityFilterLogger
 * @see SeverityFilterListener
 */
@ThreadSafe
public class SeverityFilter {

    protected Map<Severity, Boolean> enableMap = Collections.synchronizedMap(new EnumMap<Severity, Boolean>(Severity.class));

    /** Default is {@link Severity} levels are enabled. */
    public SeverityFilter() {
        this(true);
    }

    public SeverityFilter(boolean defaultValue) {
        setAllEnabled(defaultValue);
    }

    /**
   * Returns true if the {@link Severity} is enabled or allowed. Conversely this returns false if log statements
   * with the provided {@link Severity} should be ignored or filtered out.
   */
    public boolean getEnabled(Severity s) {
        return enableMap.get(s);
    }

    public void setEnabled(Severity s, boolean enabled) {
        enableMap.put(s, enabled);
    }

    /** Performs {@link #setEnabled(Severity, boolean)} for all {@link Severity} values. */
    public void setAllEnabled(boolean enabled) {
        for (Severity s : Severity.values()) setEnabled(s, enabled);
    }
}
