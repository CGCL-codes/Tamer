package com.iver.utiles.swing.threads;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PipeTask implements IMonitorableTask {

    private IPipedTask task1;

    private IPipedTask task2;

    private boolean canceled = false;

    private IPipedTask currentTask = null;

    public PipeTask(IPipedTask task1, IPipedTask task2) {
        this.task1 = task1;
        this.task2 = task2;
        this.currentTask = task1;
    }

    public int getInitialStep() {
        return 0;
    }

    public int getFinishStep() {
        return currentTask.getFinishStep();
    }

    public int getCurrentStep() {
        return currentTask.getCurrentStep();
    }

    public String getStatusMessage() {
        return currentTask.getStatusMessage();
    }

    public String getNote() {
        if (currentTask != null) {
            return currentTask.getNote();
        } else {
            return "";
        }
    }

    public boolean isDefined() {
        return currentTask.isDefined();
    }

    public void cancel() {
        currentTask.cancel();
        canceled = true;
    }

    public void run() throws Exception {
        currentTask.run();
        if (currentTask.isCanceled()) return;
        Object result = currentTask.getResult();
        currentTask = task2;
        currentTask.setEntry(result);
        currentTask.run();
    }

    public boolean isCanceled() {
        return currentTask.isCanceled();
    }

    public boolean isFinished() {
        return task1.isFinished() && task2.isFinished();
    }

    public void finished() {
    }
}
