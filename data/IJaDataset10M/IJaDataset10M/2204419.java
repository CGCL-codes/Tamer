package slojj.dotsbox.core;

public abstract class TaskBase implements Task {

    protected boolean isTimer = false;

    protected long delaySeconds = 0;

    protected long periodSeconds = 0;

    protected int code = 200;

    protected String status = "";

    protected long cost = 0;

    protected Object source;

    protected Object target;

    protected TaskBase(Object source) {
        this.source = source;
    }

    public long getCost() {
        return cost;
    }

    public long getDelaySeconds() {
        return delaySeconds;
    }

    public long getPeriodSeconds() {
        return periodSeconds;
    }

    public int getReturnCode() {
        return code;
    }

    public Object getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public Object getTarget() {
        return target;
    }

    public boolean isTimerTask() {
        return isTimer;
    }

    public void run() {
        beforeRun();
        target = internalRun();
        afterRun();
    }

    protected void beforeRun() {
        cost = System.currentTimeMillis();
    }

    protected void afterRun() {
        cost = System.currentTimeMillis() - cost;
    }

    protected abstract Object internalRun();
}
