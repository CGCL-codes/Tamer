package org.databene.task;

import org.databene.commons.Context;
import org.databene.commons.ErrorHandler;
import org.databene.commons.MessageHolder;

/**
 * Wraps a Task and forwards invocations.<br/>
 * <br/>
 * Created: 06.07.2007 06:36:22
 * @since 0.2
 * @author Volker Bergmann
 */
public abstract class TaskProxy<E extends Task> extends AbstractTask implements Cloneable, MessageHolder {

    protected E realTask;

    public TaskProxy(E realTask) {
        setRealTask(realTask);
    }

    public E getRealTask() {
        return realTask;
    }

    public void setRealTask(E realTask) {
        this.realTask = realTask;
        setTaskName(realTask != null ? realTask.getClass().getSimpleName() : "undefined");
    }

    public TaskResult execute(Context context, ErrorHandler errorHandler) {
        return realTask.execute(context, errorHandler);
    }

    @Override
    public void pageFinished() {
        realTask.pageFinished();
    }

    @Override
    public boolean isThreadSafe() {
        return realTask.isThreadSafe();
    }

    @Override
    public boolean isParallelizable() {
        return realTask.isParallelizable();
    }

    public String getMessage() {
        return (realTask instanceof MessageHolder ? ((MessageHolder) realTask).getMessage() : null);
    }

    @Override
    public void close() {
        realTask.close();
    }

    @Override
    public abstract Object clone();

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + realTask.toString() + ']';
    }
}
