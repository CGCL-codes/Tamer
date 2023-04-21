package net.community.chest.io.jsch;

import net.community.chest.util.logging.LogLevelWrapper;
import net.community.chest.util.logging.LoggerWrapper;
import net.community.chest.util.logging.factory.WrapperFactoryManager;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 24, 2009 9:06:36 AM
 */
public class JschLogger implements Logger {

    private final LoggerWrapper _l;

    public JschLogger(final Class<?> c) {
        _l = WrapperFactoryManager.getLogger(c);
    }

    public JschLogger() {
        this(JSch.class);
    }

    public static final LogLevelWrapper toLogLevelWrapper(final int lvl) {
        switch(lvl) {
            case FATAL:
                return LogLevelWrapper.FATAL;
            case ERROR:
                return LogLevelWrapper.ERROR;
            case WARN:
                return LogLevelWrapper.WARNING;
            case INFO:
                return LogLevelWrapper.INFO;
            case DEBUG:
                return LogLevelWrapper.DEBUG;
            default:
                return null;
        }
    }

    @Override
    public boolean isEnabled(int level) {
        final LogLevelWrapper ll = toLogLevelWrapper(level);
        return (ll != null) && _l.isEnabledFor(ll);
    }

    @Override
    public void log(int level, String message) {
        if ((null == message) || (message.length() <= 0)) return;
        final LogLevelWrapper ll = toLogLevelWrapper(level);
        if (null == ll) return;
        _l.log(ll, message);
    }
}
