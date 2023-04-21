package tests.jfun.models;

public class LifecycleTransition {

    private int status = 0;

    private String status_name = "uninitialized";

    public LifecycleTransition() {
        target_stamp = 0;
    }

    private final int target_stamp;

    public LifecycleTransition(int target_stamp) {
        this.target_stamp = target_stamp;
    }

    private void migrateStatus(String name, int target) {
        if (status != target - 1) {
            throw new IllegalStateException("cannot migrate from " + status_name + " to " + name);
        }
        status_name = name;
        status = target;
    }

    public void init() {
        migrateStatus("initialized", 1);
    }

    public void start() {
        migrateStatus("started", 2);
    }

    public void stop() {
        migrateStatus("stopped", 3);
    }

    public void dispose() {
        migrateStatus("disposed", 4);
    }

    public String toString() {
        return "lifecycle";
    }

    private int stamp = 0;

    public void init(int s) {
        migrateStatus("initialized", 1);
        stamp += s;
    }

    public void start(int s) {
        migrateStatus("started", 2);
        stamp += s;
    }

    public void stop(int s) {
        migrateStatus("stopped", 3);
        stamp += s;
    }

    public void dispose(int s) {
        migrateStatus("disposed", 4);
        stamp += s;
        if (stamp != target_stamp) {
            throw new IllegalStateException("stamp mismatch. " + stamp + "!=" + target_stamp);
        }
    }
}
