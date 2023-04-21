package net.grinder.engine.process;

import net.grinder.common.LoggerStubFactory;

/**
 * Factory for <code>ThreadLogger</code> stubs.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public class ThreadLoggerStubFactory extends LoggerStubFactory {

    public ThreadLoggerStubFactory() {
        super(ThreadLogger.class);
    }

    public final ThreadLogger getThreadLogger() {
        return (ThreadLogger) getStub();
    }
}
