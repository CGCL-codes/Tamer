package net.sourceforge.javabits.velocity;

import net.sourceforge.javabits.error.ErrorHandler;
import net.sourceforge.javabits.error.ErrorHandler.Severity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

/**
 * @author Jochen Kuhnle
 */
public class VelocityLogAdapter implements LogSystem {

    public static final Severity[] LEVEL_MAP = new Severity[4];

    private final ErrorHandler err;

    static {
        LEVEL_MAP[LogSystem.DEBUG_ID] = Severity.DEBUG;
        LEVEL_MAP[LogSystem.INFO_ID] = Severity.INFO;
        LEVEL_MAP[LogSystem.WARN_ID] = Severity.WARNING;
        LEVEL_MAP[LogSystem.ERROR_ID] = Severity.ERROR;
    }

    public VelocityLogAdapter(final ErrorHandler err) {
        this.err = err;
    }

    public void init(RuntimeServices rs) throws Exception {
    }

    public void logVelocityMessage(int level, String message) {
        err.problem(LEVEL_MAP[level], message, (Object[]) null);
    }
}
