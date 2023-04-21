package org.objectwiz.plugin.async;

import java.util.Date;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.core.Application;

/**
 * Base implementation of {@link AsynchroneousProcess}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public abstract class AsynchroneousProcessBase extends AsynchroneousProcess {

    private static final Log logger = LogFactory.getLog(AsynchroneousProcessBase.class);

    private Exception error;

    private boolean success = false;

    private boolean finished = false;

    private int errors;

    private int objectsProcessed;

    private int maxErrors = 10;

    public AsynchroneousProcessBase(Application application) {
        super(application);
    }

    @Override
    public final void run() {
        try {
            runProcess();
            success = true;
        } catch (Exception e) {
            error = e;
            errors++;
            logger.warn("Asynchronous processed raised an exception", e);
        } finally {
            finished = true;
            logger.info("Process finished: " + new Date());
        }
    }

    @Override
    public ProcessStatus getStatus() {
        Object output = null;
        if (success) try {
            output = getOutput();
        } catch (Exception e) {
            logger.warn("Could not read output", e);
        }
        return new ProcessStatusImpl(finished, success, error == null ? null : ExceptionUtils.getFullStackTrace(error), output, objectsProcessed, Math.max(0, objectsProcessed - errors), errors);
    }

    protected abstract void runProcess() throws Exception;

    protected abstract Object getOutput() throws Exception;

    protected final void logError(Exception e) {
        errors++;
        logger.warn("Non-fatal error occured", e);
        if (errors > maxErrors) {
            throw new RuntimeException("Too many errors, stopping");
        }
    }

    protected final void logStartProcessingObject(String info) {
        logger.info("Processing new object: " + info);
        objectsProcessed++;
    }

    public final boolean isFinished() {
        return finished;
    }
}
