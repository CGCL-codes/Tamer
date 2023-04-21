package org.systemsbiology.apps.corragui.server.executor;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.systemsbiology.apps.corragui.client.constants.PipelineStep;
import org.systemsbiology.apps.corragui.domain.User;

public final class ExecutorManager implements IExecutorListener {

    private List<IExecutor> executors;

    private List<IExecutor> runningExecs;

    private static final long ONE_MIN = 60 * 1000;

    private static final long SLEEP_TIME = ONE_MIN;

    private static final int MAX_RUNNING_EXECS = 1000;

    private static final Logger log = Logger.getLogger(ExecutorManager.class.getName());

    private static ExecutorManager instance;

    private ExecutorManager() {
        executors = new ArrayList<IExecutor>();
        runningExecs = new ArrayList<IExecutor>();
        ManagerThread thread = new ManagerThread();
        thread.setDaemon(true);
        thread.setName(ExecutorManager.class.getName() + " " + Thread.currentThread().getId());
        thread.start();
    }

    public static synchronized ExecutorManager instance() {
        if (instance == null) instance = new ExecutorManager();
        return instance;
    }

    public void addExecutor(IExecutor executor) {
        executor.addListener(this);
        executor.setPending();
        synchronized (executors) {
            log.debug("Adding executor -- " + executor.getExecutorName());
            executors.add(executor);
            log.debug("Number of executors in queue: " + executors.size());
            executors.notifyAll();
        }
    }

    public void cancelExecutor(User user, String projectName, PipelineStep step) {
        IExecutor exe = findExecutor(user, projectName, step);
        if (exe == null) {
            log.error("Executor not found for user: " + user.getLoginName() + " and project: " + projectName);
            return;
        }
        exe.cancel();
    }

    private IExecutor findExecutor(User user, String projectName, PipelineStep step) {
        for (IExecutor exe : executors) if (exe.getUserName().equals(user.getLoginName()) && exe.getProjectName().equals(projectName) && exe.getPipelineStep().equals(step)) return exe;
        return null;
    }

    private class ManagerThread extends Thread {

        public void run() {
            while (true) {
                synchronized (executors) {
                    while (executors.size() == 0 || runningExecs.size() == MAX_RUNNING_EXECS) try {
                        log.debug("Idle......");
                        executors.wait();
                    } catch (InterruptedException e) {
                        log.error("ManagerThread interrupted", e);
                    }
                }
                startPendingExecutors();
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    log.error("ManagerThread interrupted", e);
                }
            }
        }
    }

    private void startPendingExecutors() {
        log.debug("Start Pending executors....");
        List<IExecutor> toExecute = new ArrayList<IExecutor>();
        List<IExecutor> failedExecs = new ArrayList<IExecutor>();
        List<IExecutor> canceledExecs = new ArrayList<IExecutor>();
        synchronized (executors) {
            for (IExecutor exec : executors) {
                if (runningExecs.size() >= MAX_RUNNING_EXECS) break;
                if (exec.isFailed()) {
                    log.debug("adding executor to failedExecutors");
                    failedExecs.add(exec);
                }
                if (exec.isCanceled()) {
                    log.debug("adding executor to canceledExecutors");
                    canceledExecs.add(exec);
                } else if (exec.isRunning() || exec.isWaiting()) continue; else {
                    log.debug("adding executor to toExecute");
                    toExecute.add(exec);
                    runningExecs.add(exec);
                }
            }
            log.debug("After starting executors -- Running executors: " + runningExecs.size() + "; Total executors: " + this.executors.size());
        }
        for (IExecutor exec : toExecute) exec.execute();
        synchronized (executors) {
            log.debug("removing failed executors : " + failedExecs.size());
            for (IExecutor failedExec : failedExecs) {
                removeExecutor(failedExec);
                executors.notifyAll();
            }
            if (failedExecs.size() > 0) log.debug("After removing failed executors -- Total executors: " + this.executors.size());
        }
    }

    public void executorDone(IExecutor exec) {
        synchronized (executors) {
            if (!executors.contains(exec)) return;
            removeExecutor(exec);
            log.debug("After executor done -- Running executors: " + runningExecs.size() + "; Total executors: " + this.executors.size());
            executors.notifyAll();
        }
    }

    public void executorFailed(IExecutor exec) {
        synchronized (executors) {
            if (!executors.contains(exec)) return;
            removeExecutor(exec);
            log.debug("After executor failed -- Running executors: " + runningExecs.size() + "; Total executors: " + this.executors.size());
            executors.notifyAll();
        }
    }

    public void executorCanceled(IExecutor exec) {
        synchronized (executors) {
            if (!executors.contains(exec)) return;
            removeExecutor(exec);
            log.debug("After executor canceled -- Running executors: " + runningExecs.size() + "; Total executors: " + this.executors.size());
            executors.notifyAll();
        }
    }

    private void removeExecutor(IExecutor exec) {
        executors.remove(exec);
        runningExecs.remove(exec);
    }
}
