package architecture.ee.upgrade;

public interface UpgradeTask {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getEstimatedRunTime();

    public abstract String getInstructions();

    public abstract boolean isBackgroundTask();

    public abstract void doTask() throws Exception;
}
