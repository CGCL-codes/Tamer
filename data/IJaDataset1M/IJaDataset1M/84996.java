package net.sourceforge.domian.repository;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.TimeoutException;
import static org.apache.commons.lang.StringUtils.uncapitalize;
import static org.apache.commons.lang.SystemUtils.FILE_SEPARATOR;
import net.sourceforge.domian.entity.Entity;
import net.sourceforge.domian.repository.AbstractDomianCoreRepository;
import net.sourceforge.domian.repository.HumanReadableFormatRepository;
import net.sourceforge.domian.repository.PersistentRepository;
import net.sourceforge.domian.repository.RepositoryException;
import static net.sourceforge.domian.util.InstrumentationUtils.buildMessageWithStackTrace;
import static net.sourceforge.domian.util.InstrumentationUtils.buildThreadNumberAndMessage;
import net.sourceforge.domian.util.concurrent.locks.Synchronizer;
import static net.sourceforge.domian.util.concurrent.locks.Synchronizer.MODE.CONCURRENT;
import static net.sourceforge.domian.util.concurrent.locks.Synchronizer.MODE.EXCLUSIVE;

/**
 * Common functionality for all Domian XStream-based repositories,
 * e.g. an extensive set of {@link net.sourceforge.domian.util.concurrent.locks.Synchronizer} convenience methods.
 * <p/>
 * File encoding UTF-8 is defined in this class.
 *
 * @author Eirik Torske
 * @since 0.4
 */
abstract class AbstractXStreamXmlFileRepository<T extends Entity> extends AbstractDomianCoreRepository<T> implements HumanReadableFormatRepository<T> {

    protected static final String XSTREAM_XML_FILE_SUFFIX = ".xstream-1.3.1.xml";

    private static final int ALL_STACKTRACE_LINES = 60;

    /** For file-based repositories the <code>repositoryRootPath</code> is the absolute path for all repositories of the same type. */
    protected String repositoryRootPath;

    /**
     * The <i>repository-ID</i> the name of the repository.
     * It should be unique within a running <i>system</i>.
     * <p/>
     * For file-based repositories the <code>${repositoryRootPath}/${repositoryId}</code> forms the absolute folder path in which the repository data resides.
     */
    protected String repositoryId;

    public String getRepositoryRootPath() {
        return this.repositoryRootPath;
    }

    @Override
    public String getRepositoryId() {
        return this.repositoryId;
    }

    public String getRepositoryPathString() {
        return getRepositoryRootPath() + FILE_SEPARATOR + getRepositoryId();
    }

    @Override
    public File getRepositoryDirectory() {
        return new File(getRepositoryPathString());
    }

    /**
     * When using the <code>*WithRetry</code> methods,
     * this value defines the maximum number of times an operation will be retried.
     * <p/>
     * The default value is 2.
     */
    private Integer maxNumberOfRetries = 2;

    /**
     * When using the <code>*WithRetry</code> methods,
     * this value defines the timeout for all retried operations.
     * <p/>
     * The default value is 500.
     */
    private Integer retryTimeoutInMilliseconds = 500;

    public Integer getMaxNumberOfRetries() {
        return maxNumberOfRetries;
    }

    public void setMaxNumberOfRetries(final Integer maxNumberOfRetries) {
        this.maxNumberOfRetries = maxNumberOfRetries;
    }

    public Integer getRetryTimeoutInMilliseconds() {
        return retryTimeoutInMilliseconds;
    }

    public void setRetryTimeoutInMilliseconds(final Integer retryTimeoutInMilliseconds) {
        this.retryTimeoutInMilliseconds = retryTimeoutInMilliseconds;
    }

    @Override
    public String getEncoding() {
        return "UTF-8";
    }

    /** @return the absolute path to the repository location based on root path and the repository ID */
    protected String getRepositoryPath(final String repositoryRootPath, final PersistentRepository<T> repository) {
        return repositoryRootPath + FILE_SEPARATOR + repository.getRepositoryId();
    }

    /** Added for completeness. */
    protected void run(final Runnable runnable) {
        runnable.run();
    }

    /** Added for completeness. */
    protected void call(final Callable<T> callable) throws Exception {
        callable.call();
    }

    /**
     * Convenience method for running a {@link Runnable} in a concurrent manner.
     *
     * @see net.sourceforge.domian.util.concurrent.locks.Synchronizer
     */
    protected void runConcurrently(final Runnable runnable) {
        this.synchronizer.runConcurrently(runnable);
    }

    /**
     * Convenience method for running a {@link java.util.concurrent.Callable} in a concurrent manner.
     *
     * @see net.sourceforge.domian.util.concurrent.locks.Synchronizer
     */
    protected <T> T callConcurrently(final Callable<T> callable) {
        return this.synchronizer.callConcurrently(callable);
    }

    /**
     * Convenience method for running a {@link java.util.concurrent.Callable} in an exclusively manner.
     *
     * @see net.sourceforge.domian.util.concurrent.locks.Synchronizer
     */
    protected <T> T callExclusively(final Callable<T> callable) {
        return this.synchronizer.callExclusively(callable);
    }

    /**
     * Convenience method for running a {@link java.util.concurrent.Callable} in a concurrent manner.
     * If an exception is thrown, the {@link java.util.concurrent.Callable} will be immediately <i>retried</i>.
     * The nature of the retries are further defined by the <code>maxNumberOfRetries</code> and
     * <code>retryTimeoutInMilliseconds</code> member values.
     */
    protected <T> T callConcurrentlyWithRetry(final Callable<T> callable) {
        try {
            return callConcurrently(callable);
        } catch (Exception e) {
            return retry(callable, e, CONCURRENT);
        }
    }

    /**
     * Convenience method for running a {@link java.util.concurrent.Callable} in an exclusively manner.
     * If an exception is thrown, the {@link java.util.concurrent.Callable} will be immediately queued for <i>retrial</i>.
     * The nature of the retries are further defined by the <code>maxNumberOfRetries</code> and
     * <code>retryTimeoutInMilliseconds</code> member values.
     */
    protected <T> T callExclusivelyWithRetry(final Callable<T> callable) {
        try {
            return callExclusively(callable);
        } catch (Exception e) {
            return retry(callable, e, EXCLUSIVE);
        }
    }

    protected <T> T callConcurrentlyInNewThread(final Callable<T> callable) throws ExecutionException, TimeoutException, InterruptedException {
        final Future<T> future = new FutureTask<T>(callable);
        this.synchronizer.runConcurrently((FutureTask) future);
        return future.get(this.retryTimeoutInMilliseconds, MILLISECONDS);
    }

    protected <T> T callExclusivelyInNewThread(final Callable<T> callable) throws ExecutionException, TimeoutException, InterruptedException {
        final Future<T> future = new FutureTask<T>(callable);
        this.synchronizer.runExclusively((FutureTask) future);
        return future.get(this.retryTimeoutInMilliseconds, MILLISECONDS);
    }

    /**
     * Executes the given {@link Runnable} in a fresh and independent thread.
     * <i>This method does not block the original thread.</i>
     * The {@link net.sourceforge.domian.util.concurrent.locks.Synchronizer} mode is <i>concurrent</i>.
     * (An asynchronous version of {@link net.sourceforge.domian.util.concurrent.locks.Synchronizer} in <i>exclusive</i> mode has no meaning,
     * as it is a "stop-the-world" kind of mode.)
     */
    protected void runAsynchronously(final Runnable runnable) {
        runConcurrently(new FutureTask<T>(runnable, null));
    }

    /**
     * Executes the given {@link java.util.concurrent.Callable} in a fresh thread, with time-out set to <code>retryTimeoutInMilliseconds</code>.
     * The {@link java.util.concurrent.Callable} will be retried <code>maxNumberOfRetries</code> times.
     * <p/>
     * This method will use an uncapitalized version of the given callable's simple class name as method name.
     */
    private <T> T retry(final Callable<T> callable, final Exception retryReason, final Synchronizer.MODE synchronizedMode) {
        return retry(uncapitalize(callable.getClass().getSimpleName()), callable, retryReason, synchronizedMode);
    }

    /**
     * Executes the given {@link java.util.concurrent.Callable} in a fresh thread, with time-out set to <code>retryTimeoutInMilliseconds</code>.
     * The {@link java.util.concurrent.Callable} will be retried <code>maxNumberOfRetries</code> times.
     */
    private <T> T retry(final String methodName, final Callable<T> callable, final Exception reasonForRetry, final Synchronizer.MODE synchronizedMode) {
        if (reasonForRetry.getCause() instanceof com.thoughtworks.xstream.converters.ConversionException) {
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("loggelinje nummer 1a"));
            }
            log.warn(buildThreadNumberAndMessage("XStream conversion failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), 0, 9)));
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("XStream conversion failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), ALL_STACKTRACE_LINES, 9)));
            }
        } else if (reasonForRetry.getCause() instanceof com.thoughtworks.xstream.io.StreamException) {
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("loggelinje nummer 1b"));
            }
            log.warn(buildThreadNumberAndMessage("XStream i/o failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), 0, 0)));
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("XStream i/o failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), ALL_STACKTRACE_LINES, 0)));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("loggelinje nummer 2"));
            }
            log.warn(buildThreadNumberAndMessage("XStream failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), 0, 0)));
            if (log.isDebugEnabled()) {
                log.debug(buildThreadNumberAndMessage("XStream failure while working with persistent data: " + buildMessageWithStackTrace(reasonForRetry, reasonForRetry.toString(), ALL_STACKTRACE_LINES, 0)));
            }
        }
        log.warn(buildThreadNumberAndMessage(this.getClass().getName() + "." + methodName + "() will be retried [max " + this.maxNumberOfRetries + " times, with time-out set to " + this.retryTimeoutInMilliseconds + " ms]"));
        int numberOfRetries = 0;
        while (numberOfRetries <= this.maxNumberOfRetries) {
            if (log.isWarnEnabled()) {
                log.warn(buildThreadNumberAndMessage(this.getClass().getName() + "." + methodName + "() retry #" + (numberOfRetries + 1)));
            }
            try {
                switch(synchronizedMode) {
                    case CONCURRENT:
                        return callConcurrentlyInNewThread(callable);
                    case EXCLUSIVE:
                        return callExclusivelyInNewThread(callable);
                    default:
                        throw new IllegalStateException(synchronizedMode + " is not a valid net.sourceforge.domian.util.Synchronizer mode");
                }
            } catch (Exception e) {
                ++numberOfRetries;
                if (e instanceof TimeoutException) {
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("loggelinje nummer 5"));
                    }
                    log.warn(buildThreadNumberAndMessage("XStream timeout after " + this.retryTimeoutInMilliseconds + " ms while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), 0, 9)));
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("XStream timeout after " + this.retryTimeoutInMilliseconds + " ms while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), ALL_STACKTRACE_LINES, 9)));
                    }
                } else if (reasonForRetry.getCause() instanceof com.thoughtworks.xstream.converters.ConversionException) {
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("loggelinje nummer 3a"));
                    }
                    log.warn(buildThreadNumberAndMessage("XStream conversion failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), 0, 9)));
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("XStream conversion failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), ALL_STACKTRACE_LINES, 9)));
                    }
                } else if (reasonForRetry.getCause() instanceof com.thoughtworks.xstream.io.StreamException) {
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("loggelinje nummer 3b"));
                    }
                    log.warn(buildThreadNumberAndMessage("XStream i/o failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), 0, 0)));
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("XStream i/o failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), ALL_STACKTRACE_LINES, 0)));
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("loggelinje nummer 4"));
                    }
                    log.warn(buildThreadNumberAndMessage("XStream failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), 0, 0)));
                    if (log.isDebugEnabled()) {
                        log.debug(buildThreadNumberAndMessage("XStream failure while working with persistent data: " + buildMessageWithStackTrace(e, e.toString(), ALL_STACKTRACE_LINES, 0)));
                    }
                }
                if (numberOfRetries >= this.maxNumberOfRetries) {
                    throw new RepositoryException(this.getClass().getName() + "." + methodName + "() failed! Max number of retries [" + this.maxNumberOfRetries + "] reached, aborting operation!");
                }
            }
        }
        throw new RepositoryException(this.getClass().getName() + "." + methodName + "() failed", reasonForRetry);
    }
}
