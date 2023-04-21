package us.wthr.jdem846.ui;

import java.util.LinkedList;
import java.util.List;

public class MonitoredThread extends Thread {

    private List<ProgressListener> progressListeners = new LinkedList<ProgressListener>();

    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    public void removeProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    protected void fireOnStartListeners() {
        for (ProgressListener listener : progressListeners) {
            listener.onStart();
        }
    }

    protected void fireProgressListeners(double progress) {
        for (ProgressListener listener : progressListeners) {
            listener.onProgress(progress);
        }
    }

    protected void fireOnCompleteListeners() {
        for (ProgressListener listener : progressListeners) {
            listener.onComplete();
        }
    }

    public interface ProgressListener {

        public void onStart();

        public void onProgress(double progress);

        public void onComplete();
    }
}
