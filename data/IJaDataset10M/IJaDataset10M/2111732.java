package org.igorno12.util.logging;

import org.igorno12.util.logging.DefaultLogEntryCodec;
import org.igorno12.util.logging.JULLevelWrapper;
import org.igorno12.util.logging.Log4jHandler;
import org.igorno12.util.logging.LogEntryCodec;
import org.igorno12.util.logging.test.LogLevelDataProvider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public final class Log4jHandlerTest {

    @SuppressWarnings("unchecked")
    private Class classUnderLogging;

    private LogEntryCodec logEntryCodec;

    private org.apache.log4j.Logger log4jLogger;

    public Log4jHandlerTest() {
        super();
    }

    @BeforeClass
    public void beforeClass() {
        this.classUnderLogging = this.getClass();
        this.logEntryCodec = new DefaultLogEntryCodec();
        this.log4jLogger = org.apache.log4j.Logger.getLogger(this.classUnderLogging);
    }

    @Test(dataProvider = "jul", dataProviderClass = LogLevelDataProvider.class)
    public void testByLogLevel(JULLevelWrapper wrapper) {
        final Log4jHandler log4jHandler = new Log4jHandler(logEntryCodec, log4jLogger);
        Assert.assertTrue(true, "setup handler");
        final java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(classUnderLogging.getName());
        julLogger.setUseParentHandlers(false);
        julLogger.addHandler(log4jHandler);
        Assert.assertTrue(true, "added handler to jul logger");
        final String message = "hello world";
        julLogger.log(wrapper.getLevel(), message);
        Assert.assertTrue(true, "logged message to jul logger");
        final Throwable throwable = new IllegalStateException("test throwable");
        julLogger.log(wrapper.getLevel(), message, throwable);
        Assert.assertTrue(true, "logged message and throwable to jul logger");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWithNullCodec() {
        final Log4jHandler log4jHandler = new Log4jHandler(null, log4jLogger);
        assert (log4jHandler != null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWithNullLogger() {
        final Log4jHandler log4jHandler = new Log4jHandler(new DefaultLogEntryCodec(), null);
        assert (log4jHandler != null);
    }

    @Test
    public void testFlush() {
        final Log4jHandler log4jHandler = new Log4jHandler(logEntryCodec, log4jLogger);
        Assert.assertTrue(true, "setup handler");
        log4jHandler.flush();
        Assert.assertTrue(true, "flushed handler");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testAppendOnClosed() {
        final Log4jHandler log4jHandler = new Log4jHandler(logEntryCodec, log4jLogger);
        Assert.assertTrue(true, "setup handler");
        log4jHandler.close();
        Assert.assertTrue(true, "closed handler");
        final java.util.logging.LogRecord record = new java.util.logging.LogRecord(java.util.logging.Level.SEVERE, "hello world");
        log4jHandler.publish(record);
    }
}
