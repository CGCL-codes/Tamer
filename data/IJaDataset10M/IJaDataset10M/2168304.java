package net.sourceforge.jnsynch;

public class SynchronizationHandler {

    private SynchronizationProcessor process;

    SynchronizationHandler(SynchronizationProcessor process) {
        this.process = process;
    }

    public void kill() {
        process.interrupt();
    }

    public SynchronizationStatus getStatus() {
        return process.getStatus();
    }

    public boolean isRunning() {
        SynchronizationStatus status = process.getStatus();
        return SynchronizationStatus.ResolvingSource.equals(status) || SynchronizationStatus.ResolvingTarget.equals(status) || SynchronizationStatus.ResolvingOperations.equals(status) || SynchronizationStatus.Synchronizing.equals(status);
    }

    public boolean isFinished() {
        SynchronizationStatus status = process.getStatus();
        return SynchronizationStatus.Finished.equals(status) || SynchronizationStatus.Failed.equals(status) || SynchronizationStatus.Killed.equals(status);
    }
}
