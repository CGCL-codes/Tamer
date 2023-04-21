package org.skuebeck.ooc.invoker;

import java.lang.reflect.Method;
import org.skuebeck.ooc.DeadlockOrTimeoutException;
import org.skuebeck.ooc.annotations.Blocking;

public class BlockingInvoker extends SynchronousInvoker {

    public BlockingInvoker(ConcurrentObjectContext context) {
        super(context);
    }

    @Override
    public boolean canInvoke(Method method) {
        return method.isAnnotationPresent(Blocking.class);
    }

    public Object invoke(final Method method, final Object[] args) throws Throwable {
        synchronized (context.getObject()) {
            long startTime = System.currentTimeMillis();
            while (!isReleaseConditionFulfilled(method, startTime, args)) {
                config.getObserver().notifyBlocked(context.getObject(), method, args, Thread.currentThread());
                long timeout = config.getTimeOut(context.getObject());
                if (timeout >= 0) {
                    context.getObject().wait(timeout);
                } else {
                    context.getObject().wait();
                }
            }
        }
        config.getObserver().notifyReleased(context.getObject(), method, args, Thread.currentThread());
        return super.invoke(method, args);
    }

    private boolean isReleaseConditionFulfilled(Method method, long startTime, final Object[] args) throws Throwable {
        synchronized (context.getObject()) {
            if (((Boolean) context.invokeMethod(context.getReleaseCondition(), args)).booleanValue()) {
                return true;
            } else {
                checkIfTimedOut(method, startTime);
                return false;
            }
        }
    }

    private void checkIfTimedOut(Method method, long startTime) {
        long timeout = config.getTimeOut(context.getObject());
        if (timeout > 0 && System.currentTimeMillis() - startTime >= timeout) {
            throw new DeadlockOrTimeoutException(context.getObject(), method);
        }
    }
}
