package org.objectwiz.uibuilder.model.action.result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.process.AsynchroneousProcess;
import org.objectwiz.process.ProcessPool;
import org.objectwiz.process.ProcessStatus;
import org.objectwiz.process.ProcessStatusImpl;
import org.objectwiz.uibuilder.EvaluationContext;
import org.objectwiz.uibuilder.UIBuilderSecurityException;
import org.objectwiz.util.StoppableRunnable;
import org.objectwiz.util.TypeUtils;

/**
 * Result indicating that the action triggered the start of an asynchronous
 * process the progress of which can be followed.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class ProcessRunningActionResult extends ActionResult<ProcessRunningActionResult> {

    private static final Log logger = LogFactory.getLog(ProcessRunningActionResult.class);

    private static final String POLL = "poll";

    private static final String PROCESS_ID = "processId";

    private static final String NEXT_ACTION = "nextAction";

    private static final String CANCEL = "cancel";

    private static final int MAX_POLLING_ERRORS = 10;

    private boolean finished = false;

    private int maxExecutionTimeSec;

    private int progress;

    /**
     * No-args constructor (required by Hessian)
     */
    public ProcessRunningActionResult() {
        super();
    }

    public ProcessRunningActionResult(EvaluationContext ctx, String processId, int maxExecutionTimeSec) {
        super(ctx, createSecureAttributes(processId), null);
        this.maxExecutionTimeSec = maxExecutionTimeSec;
    }

    private static Map<String, Object> createSecureAttributes(String processId) {
        Map<String, Object> map = new HashMap();
        map.put(PROCESS_ID, processId);
        return map;
    }

    private int computeProgress(ProcessStatus status) {
        if (status.getTotalObjectsToProcess() < 0) {
            return -1;
        } else if (status.getTotalObjectsToProcess() == 0) {
            return 0;
        } else {
            int objectsProcessed = status.getSuccesses() + status.getFailures();
            return objectsProcessed / status.getTotalObjectsToProcess();
        }
    }

    /**
     * Start polling the server for the progress of the running process.
     * @param interval            Polling interval in milliseconds
     * @param progressMade        Callback that is triggered when some progress
     *                            is made. Callback implementation shall use
     *                            other methods from this class for getting
     *                            status information (e.g. {@link #getProgress()}).
     * @param onFinishedTrigger   Callback that is triggered when the process
     *                            finishes (either on a success/failure or on
     *                            a polling error).
     */
    public Future startPolling(final int interval, final Runnable progressMade, final Runnable onFinishedTrigger) {
        return Executors.newSingleThreadExecutor().submit(createPollingRunnable(interval, progressMade, onFinishedTrigger));
    }

    public ActionResult cancel() {
        return (ActionResult) execute(CANCEL, null);
    }

    public ActionResult getNextAction() {
        return (ActionResult) execute(NEXT_ACTION, null);
    }

    public ActionResult executeSynchronously(int pollingInterval) {
        StoppableRunnable r = createPollingRunnable(pollingInterval, null, null);
        Future f = Executors.newSingleThreadExecutor().submit(r);
        try {
            f.get(maxExecutionTimeSec, TimeUnit.SECONDS);
            return getNextAction();
        } catch (TimeoutException e) {
            logger.warn("Time out on " + new Date() + " after " + maxExecutionTimeSec + " seconds", e);
            r.stop();
            return new DisplayMessageActionResult("Process takes too long, abording...");
        } catch (Exception e) {
            throw new RuntimeException("Synchronous execution interrupted", e);
        }
    }

    public int getProgress() {
        return progress;
    }

    private StoppableRunnable createPollingRunnable(final int pollingInterval, final Runnable progressMade, final Runnable onFinishedTrigger) {
        if (finished) {
            throw new IllegalStateException("Process is already finished");
        }
        return new StoppableRunnable() {

            @Override
            public void run() {
                ProcessStatus status = null;
                int errors = 0;
                int pollingCallsCount = 0;
                do {
                    try {
                        Date start = new Date();
                        logger.info("Polling #" + pollingCallsCount + " (" + new Date() + ")");
                        status = (ProcessStatus) execute(POLL, null);
                        logger.info("Got polling result #" + pollingCallsCount + ", success: " + status.success());
                        pollingCallsCount++;
                        long elapsed = new Date().getTime() - start.getTime();
                        if (elapsed < pollingInterval) {
                            Thread.sleep(pollingInterval - elapsed);
                        }
                    } catch (Exception e) {
                        logger.warn("Error while polling status: " + e.getMessage());
                        if (errors >= MAX_POLLING_ERRORS) {
                            onFinishedTrigger.run();
                            return;
                        }
                        errors++;
                    }
                    if (status != null) {
                        int newProgress = computeProgress(status);
                        if (newProgress > progress && progressMade != null) {
                            progressMade.run();
                        }
                    }
                } while (running() && status != null && (!status.finished()));
                finished = true;
                if (onFinishedTrigger != null) {
                    onFinishedTrigger.run();
                }
            }
        };
    }

    @Override
    public Object perform(String operationId, EvaluationContext trustedCtx, EvaluationContext parameters) throws Exception {
        String processId = getAttributeBag().getAttribute(String.class, PROCESS_ID);
        ProcessPool pool = ProcessPool.instance();
        AsynchroneousProcess process = pool.getProcessById(processId);
        if (!process.getUsername().equals(getAttributeBag().getUsername())) {
            throw new UIBuilderSecurityException("User mismatch");
        }
        if (POLL.equals(operationId)) {
            return new ProcessStatusImpl(process.getStatus(), false);
        } else if (CANCEL.equals(operationId)) {
            boolean cancelledSuccessfully = pool.stopProcess(processId);
            if (cancelledSuccessfully) {
                return new DisplayMessageActionResult("Cancelled.");
            } else {
                return new DisplayMessageActionResult("Process already ended, could not cancel it.");
            }
        } else if (NEXT_ACTION.equals(operationId)) {
            if (!process.getStatus().finished()) {
                throw new IllegalArgumentException("Process is not finished yet");
            }
            logger.warn("Process is finished [success=" + process.getStatus().success() + "]. " + "Summary is:\n" + process.getStatus().getSummary());
            if (!process.getStatus().success()) {
                return new DisplayMessageActionResult("Process failed");
            }
            Object result = process.getStatus().getResult();
            if (result != null && TypeUtils.isByteArray(result)) {
                return new BinaryStreamActionResult(trustedCtx, (byte[]) result);
            } else {
                return new DisplayMessageActionResult("Success!");
            }
        }
        throw new UnsupportedOperationException("Unsupported operation: " + operationId);
    }
}
