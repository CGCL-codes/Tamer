package net.jadoth.logging;

public class InvocationLogging {

    protected static final boolean LOGGING_ENABLED = false;

    protected static final InvocationLogging LOGGING = new InvocationLogging();

    protected static final <T> T dispatch(final T instance) {
        return LOGGING_ENABLED ? LOGGING.addLoggingAspect(instance) : instance;
    }

    private final InvocationLogger invocationLogger;

    public InvocationLogging() {
        this(new InvocationLogger.Implementation());
    }

    public InvocationLogging(final InvocationLogger invocationLogger) {
        super();
        this.invocationLogger = invocationLogger;
    }

    public final <T> T addLoggingAspect(final T subject) {
        return new LoggingAspect<T>(this.invocationLogger, subject).newProxyInstance();
    }
}
