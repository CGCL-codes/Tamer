package net.sourceforge.ondex.webservice2.Executor;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import net.sourceforge.ondex.event.ONDEXEvent;
import net.sourceforge.ondex.event.type.EventType;
import net.sourceforge.ondex.webservice2.Exceptions.JobException;
import org.apache.log4j.Logger;

/**
 *
 * @author christian
 */
public abstract class WebserviceJob<E> implements Callable {

    private Date submitTime;

    private Date startTime;

    private Date finishTime;

    private static Random random = new Random();

    private Future<E> future;

    private UUID jobId;

    protected StringBuffer buffer;

    protected StringBuffer errorBuffer;

    protected String errorMsg;

    protected String description;

    public static final String NEW_LINE = System.getProperty("line.separator");

    private static final Logger logger = Logger.getLogger(WebserviceJob.class);

    public WebserviceJob() throws JobException {
        try {
            buffer = new StringBuffer();
            log("creating webservice job.");
            UUID jobId = UUID.randomUUID();
            setJobId(jobId);
            submitTime = new Date();
            description = jobId.toString();
            log("end creating webservice job.");
        } catch (Exception e) {
            throw new JobException("Exception creating WebserviceJob", e, logger);
        }
    }

    protected void submitJob() {
        future = ExecutorRegister.getRegister().submitJob(this);
        log("Job " + jobId + " submitted at: " + submitTime);
    }

    @Override
    public E call() throws Exception {
        startTime = new Date();
        log("Job " + description + " id " + jobId + " started at: " + startTime);
        try {
            E result = innerCall();
            log("Result for job " + jobId + " is: " + result);
            finishTime = new Date();
            log("Job " + description + " id " + jobId + " finished at: " + finishTime);
            return result;
        } catch (Exception e) {
            finishTime = new Date();
            errorMsg = e.getMessage();
            errorBuffer = new StringBuffer();
            errorLog("Error caused by job: " + getDescription());
            errorLog(e.getLocalizedMessage());
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement element : trace) {
                errorLog(element.toString());
            }
            errorLog("Job " + description + " id " + jobId + " aborted at: " + finishTime);
            throw e;
        }
    }

    public String getDescription() {
        return jobId.toString();
    }

    public abstract E innerCall() throws Exception;

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public Future getFuture() {
        return future;
    }

    public boolean cancel(boolean mayInterrupIfRunning) {
        return future.cancel(mayInterrupIfRunning);
    }

    public E get() throws JobException {
        try {
            logger.info("super get");
            return future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new JobException("Job not finished on get() call to job" + description + " id " + jobId, logger);
        } catch (InterruptedException ex) {
            throw new JobException("Error on get() call to job" + description + " id " + jobId, ex, logger);
        } catch (ExecutionException ex) {
            throw new JobException("Error on get() call to job" + description + " id " + jobId, ex, logger);
        }
    }

    public E get(Long timeout, TimeUnit unit) throws JobException {
        try {
            return future.get(timeout, unit);
        } catch (InterruptedException ex) {
            throw new JobException("Error on get() call to job" + description + " id " + jobId, ex, logger);
        } catch (ExecutionException ex) {
            throw new JobException("Error on get() call to job" + description + " id " + jobId, ex, logger);
        } catch (TimeoutException ex) {
            throw new JobException("Error on get() call to job" + description + " id " + jobId, ex, logger);
        }
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public String checkStatus() {
        if (startTime == null) {
            return Status.PENDING.toString();
        }
        if (finishTime == null) {
            return Status.RUNNING.toString();
        }
        if (errorBuffer == null) {
            return Status.DONE.toString();
        } else {
            return Status.ERROR.toString();
        }
    }

    public boolean isSuccessful() throws JobException {
        if (errorMsg != null) {
            throw new JobException(errorMsg, logger);
        }
        return (finishTime != null);
    }

    public void eventOccurred(ONDEXEvent e) {
        EventType eventType = e.getEventType();
        log(eventType.getCompleteMessage());
    }

    public String getHistory() {
        if (errorBuffer == null) {
            return buffer.toString();
        } else {
            return errorBuffer.toString() + NEW_LINE + buffer.toString();
        }
    }

    public String getErrorHistory() {
        if (errorBuffer == null) {
            if (startTime == null) {
                return "Job " + description + " id " + jobId + " not yet started";
            }
            if (finishTime == null) {
                return "Job " + description + " id " + jobId + " still running";
            }
            return "Job " + description + " id " + jobId + " Finished Successfully.";
        } else {
            return errorBuffer.toString();
        }
    }

    protected void log(String message) {
        buffer.append(message);
        buffer.append(NEW_LINE);
        logger.info(message);
    }

    protected void errorLog(String message) {
        errorBuffer.append(message);
        errorBuffer.append(NEW_LINE);
        logger.error(message);
    }

    public String getUrl() throws JobException {
        throw new JobException("No Url for job " + description + " id " + jobId, logger);
    }

    public Long getSize() throws JobException {
        return (long) getString().length();
    }

    public String getString() throws JobException {
        return get().toString();
    }

    public boolean deleteJob() throws JobException {
        if (isDone()) {
            submitTime = null;
            startTime = null;
            finishTime = null;
            future = null;
            ExecutorRegister.getRegister().clearJob(jobId);
            jobId = null;
            buffer = null;
            errorBuffer = null;
            errorMsg = null;
            description = null;
            return true;
        } else {
            throw new JobException("Unable to delete job " + description + " id " + jobId + " It is not done", logger);
        }
    }
}
