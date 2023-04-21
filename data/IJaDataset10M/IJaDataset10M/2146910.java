package org.systemsbiology.apps.corragui.server.executor;

public interface IExecutorListener {

    public abstract void executorDone(IExecutor exec);

    public abstract void executorFailed(IExecutor exec);

    public abstract void executorCanceled(IExecutor exec);
}
