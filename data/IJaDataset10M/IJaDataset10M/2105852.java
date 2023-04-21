package uk.org.ogsadai.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import uk.org.ogsadai.config.ConfigurationValueIllegalException;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.engine.event.CompositeEngineListener;
import uk.org.ogsadai.engine.event.EngineListener;
import uk.org.ogsadai.engine.event.EventfulRequest;
import uk.org.ogsadai.exception.RequestTerminatedException;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.drer.EngineState;

/**
 * A request engine which submits a maximum number of requests to an executor 
 * service. A queue is maintained for pending requests. Both the size of the
 * processing pool and the queue length can be configured.
 *
 * @author The OGSA-DAI Project Team
 */
public class RequestQueueingEngine implements RequestEngine {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Engine state configuration key for maximum request queue length. */
    public static final Key CONCURRENCY_QUEUE_LENGTH = new Key("dai.concurrency.queue.length");

    /** Engine state configuration key for request pool size. */
    public static final Key CONCURRENCY_REQUEST_POOL_SIZE = new Key("dai.concurrency.request.pool.size");

    /** Tracks the active requests. */
    private Map mActiveRequests = new HashMap();

    /** Maximum size of processing pool. */
    private final int mMaxRequests;

    /** Service which executes requests. */
    private final ExecutorService mExecutorService;

    /** Queue for requests. */
    private final RequestQueue mRequestQueue;

    /** Listener for engine events. */
    private final CompositeEngineListener mListener = new CompositeEngineListener();

    /**
     * Creates a new engine which allows the submission of a maximum number of
     * requests to the given executor services and manages a queue to hold
     * pending requests.
     * 
     * @param executorService
     *            executor service to which requests will be submitted
     * @param state
     *            engine configuration which specifies the length of the queue
     *            and the maximum number of requests in the processing pool
     * @throws ConfigurationValueMissingException
     *     Pool size and queue size parameters cannot be found in the state.
     * @throws ConfigurationValueIllegalException
     *     Pool size and queue size parameters cannot be parsed into
     *     integers.
     */
    public RequestQueueingEngine(ExecutorService executorService, EngineState state) throws ConfigurationValueMissingException, ConfigurationValueIllegalException {
        mExecutorService = executorService;
        mMaxRequests = getQueueParameter(CONCURRENCY_REQUEST_POOL_SIZE, state);
        int queueLength = getQueueParameter(CONCURRENCY_QUEUE_LENGTH, state);
        mRequestQueue = new RequestQueue(queueLength);
    }

    /**
     * Get a named parameter from the engine state and convert to an
     * integer.
     *
     * @param parameterID
     *     ID of parameter.
     * @param state
     *     Engine state
     * @return integer value of parameter.
     * @throws ConfigurationValueMissingException
     *     The parameter cannot be found in the state.
     * @throws ConfigurationValueIllegalException
     *     The parameter can be found but cannot be parsed into an
     *     integer.
     */
    private int getQueueParameter(Key parameterID, EngineState state) throws ConfigurationValueMissingException, ConfigurationValueIllegalException {
        int parameter = 0;
        try {
            parameter = (Integer.valueOf((String) state.getConfiguration().get(parameterID))).intValue();
        } catch (KeyValueUnknownException e) {
            throw new ConfigurationValueMissingException(e.getKey());
        } catch (NumberFormatException e) {
            throw new ConfigurationValueIllegalException(parameterID, e);
        }
        if (parameter < 1) {
            throw new ConfigurationValueIllegalException(parameterID);
        }
        return parameter;
    }

    public synchronized void submit(Request request) throws RequestRejectedException {
        mListener.requestReceived(request);
        if (request == null) {
            mListener.requestRejected(request);
            throw new RequestRejectedException(request);
        }
        if (mRequestQueue.offer(request)) {
            mListener.requestAccepted(request);
            if (request instanceof EventfulRequest) {
                EventfulRequest eventfulRequest = (EventfulRequest) request;
                eventfulRequest.queued();
            }
            fillProcessingPool();
        } else {
            mListener.requestRejected(request);
            throw new RequestRejectedException(request);
        }
    }

    /**
     * Fills the processing pool from the queue of pending requests if there are
     * any available. This method is typically called when a request has been
     * accepted or has completed.
     */
    private void fillProcessingPool() {
        synchronized (mRequestQueue) {
            synchronized (mActiveRequests) {
                while (!mRequestQueue.isEmpty() && mActiveRequests.size() < mMaxRequests) {
                    Request currentRequest = (Request) mRequestQueue.poll();
                    FutureTask futureTask = createFutureTask(currentRequest);
                    mExecutorService.submit(futureTask);
                    mActiveRequests.put(currentRequest.getID(), futureTask);
                }
            }
        }
    }

    public void terminateRequest(ResourceID requestID) {
        synchronized (mRequestQueue) {
            Request request = (Request) mRequestQueue.remove(requestID);
            if (request != null) {
                mListener.requestTerminated(request);
                if (request instanceof EventfulRequest) {
                    EventfulRequest eventfulRequest = (EventfulRequest) request;
                    eventfulRequest.terminated(new RequestTerminatedException());
                }
            }
            final Future future;
            synchronized (mActiveRequests) {
                future = (Future) mActiveRequests.get(requestID);
            }
            if (future != null) {
                future.cancel(true);
            }
        }
    }

    /**
     * Indicates whether the engine is currently accepting requests. The return
     * value of this method indicates the current state at this point in time.
     * In particular, if this method returns true this does not guarantee that a
     * new request will be accepted by the engine as the request queue could be
     * filled up by other threads submitting requests concurrently.
     * 
     * @return <code>true</code> if there is capacity in the request queue for
     *         new requests, <code>false</code> otherwise
     */
    public boolean isAcceptingRequests() {
        return mRequestQueue.remainingCapacity() > 0;
    }

    /**
     * Registers a new listener that listens for engine events.
     
     * @param listener
     *            engine listener to register
     */
    public void registerListener(final EngineListener listener) {
        mListener.registerEngineListener(listener);
    }

    /**
     * Removes (deregisters) a listener that listens for engine events.
     * 
     * @param listener
     *            engine listener to deregister
     */
    public void removeListener(final EngineListener listener) {
        mListener.removeEngineListener(listener);
    }

    /**
     * Creates a future task that encapsulates a specified request. When the
     * request processing is done (or has been cancelled), the associated
     * request will automatically be removed from the request map using the
     * <code>FutureTask#done()</code> call-back method.
     * 
     * @param request
     *            request to wrap in the future task
     * @return a new <code>FutureTask</code> ready for submission
     */
    private FutureTask createFutureTask(final Request request) {
        final Runnable runnable = new RequestRunner(request, mListener);
        return new FutureTask(runnable, null) {

            protected void done() {
                synchronized (mActiveRequests) {
                    mActiveRequests.remove(request.getID());
                }
                fillProcessingPool();
            }
        };
    }

    RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    Map getActiveRequests() {
        return mActiveRequests;
    }
}
