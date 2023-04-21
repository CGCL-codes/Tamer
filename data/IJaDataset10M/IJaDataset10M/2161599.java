package org.iocframework;

import org.iocframework.Factory.Context;
import org.iocframework.Factory.Worker;
import com.taliasplayground.lang.Assert;

/**
 * @author David M. Sledge
 */
public class Catcher extends Worker {

    private final Worker worker;

    private final Class<? extends Exception> handledType;

    private final Worker exceptionHandler;

    private final Worker keyWorker;

    public Catcher(Worker worker, Class<? extends Exception> handledType, Worker exceptionHandler, Worker keyWorker, String srcDescr) {
        super(srcDescr);
        Assert.notNullArg(worker, "worker may not be null");
        Assert.notNullArg(handledType, "handledType may not be null");
        Assert.isTrueArg(Exception.class.isAssignableFrom(handledType), "handledType must be java.lang.Exception or a subclass" + " thereof");
        Assert.notNullArg(exceptionHandler, "exceptionHandler may not be null");
        this.worker = worker;
        this.handledType = handledType;
        this.exceptionHandler = exceptionHandler;
        this.keyWorker = keyWorker;
    }

    @Override
    protected Object putToWork(Context context) throws WorkerTraceException {
        try {
            Assert.notNullArg(context, getSourceDescription() + ":  context may not be null");
            try {
                return context.putWorkerToWork(worker);
            } catch (WorkerTraceException t) {
                Throwable cause = t.getCause();
                if (handledType.isAssignableFrom(cause.getClass())) {
                    return keyWorker == null ? context.putWorkerToWork(exceptionHandler) : context.putWorkerToWork(exceptionHandler, context.putWorkerToWork(keyWorker), cause);
                }
                throw t;
            }
        } catch (WorkerTraceException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkerTraceException(getSourceDescription(), e);
        }
    }

    /**
     * @return
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * @return
     */
    public Class<? extends Exception> getHandledType() {
        return handledType;
    }

    /**
     * @return
     */
    public Worker getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * @return
     */
    public Worker getKeyWorker() {
        return keyWorker;
    }
}
