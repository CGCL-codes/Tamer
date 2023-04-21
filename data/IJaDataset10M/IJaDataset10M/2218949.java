package main.listeners.listenerClasses.loading;

public interface LoadingListener {

    public static final String LOADINGSTARTED = "loadingStarted";

    public static final String LOADINGSTOPPED = "loadingStopped";

    public static final String LOADINGABORTED = "loadingAborted";

    public void loadingStarted(LoadingStartedEvent event);

    public void loadingStopped(LoadingStoppedEvent event);

    public void loadingAborted(LoadingAbortedEvent event);
}
