package net.sourceforge.transumanza.task;

public class LoaderUtils {

    public static final LoaderUtils INSTANCE = new LoaderUtils();

    LoaderTask currentTask;

    private LoaderUtils() {
    }

    public LoaderTask getCurrentTask() {
        if (currentTask == null) throw new IllegalStateException("No task created! this operation is not available in I/O level mode");
        return currentTask;
    }

    public LoaderSession getSession() {
        return LoaderSession.INSTANCE;
    }
}
