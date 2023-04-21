package info.jonclark.clientserver;

/**
 * An interface to handle to result of a task delegated by a TaskMaster.
 */
public interface TaskMasterIface {

    public void taskCompleted(final String task, final String[] args, final String[] results);
}
