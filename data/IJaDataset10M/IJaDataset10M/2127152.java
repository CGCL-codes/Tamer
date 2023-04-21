package sun.net;

import java.net.URL;

/**
 * ProgressSource represents the source of progress changes.
 *
 * @author Stanley Man-Kit Ho
 */
public class ProgressSource {

    public enum State {

        NEW, CONNECTED, UPDATE, DELETE
    }

    ;

    private URL url;

    private String method;

    private String contentType;

    private long progress = 0;

    private long lastProgress = 0;

    private long expected = -1;

    private State state;

    private boolean connected = false;

    private int threshold = 8192;

    private ProgressMonitor progressMonitor;

    /**
     * Construct progress source object.
     */
    public ProgressSource(URL url, String method) {
        this(url, method, -1);
    }

    /**
     * Construct progress source object.
     */
    public ProgressSource(URL url, String method, long expected) {
        this.url = url;
        this.method = method;
        this.contentType = "content/unknown";
        this.progress = 0;
        this.lastProgress = 0;
        this.expected = expected;
        this.state = State.NEW;
        this.progressMonitor = ProgressMonitor.getDefault();
        this.threshold = progressMonitor.getProgressUpdateThreshold();
    }

    public boolean connected() {
        if (!connected) {
            connected = true;
            state = State.CONNECTED;
            return false;
        }
        return true;
    }

    /**
     * Close progress source.
     */
    public void close() {
        state = State.DELETE;
    }

    /**
     * Return URL of progress source.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Return method of URL.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Return content type of URL.
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String ct) {
        contentType = ct;
    }

    /**
     * Return current progress.
     */
    public long getProgress() {
        return progress;
    }

    /**
     * Return expected maximum progress; -1 if expected is unknown.
     */
    public long getExpected() {
        return expected;
    }

    /**
     * Return state.
     */
    public State getState() {
        return state;
    }

    /**
     * Begin progress tracking.
     */
    public void beginTracking() {
        progressMonitor.registerSource(this);
    }

    /**
     * Finish progress tracking.
     */
    public void finishTracking() {
        progressMonitor.unregisterSource(this);
    }

    /**
     * Update progress.
     */
    public void updateProgress(long latestProgress, long expectedProgress) {
        lastProgress = progress;
        progress = latestProgress;
        expected = expectedProgress;
        if (connected() == false) state = State.CONNECTED; else state = State.UPDATE;
        if (lastProgress / threshold != progress / threshold) {
            progressMonitor.updateProgress(this);
        }
        if (expected != -1) {
            if (progress >= expected && progress != 0) close();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return getClass().getName() + "[url=" + url + ", method=" + method + ", state=" + state + ", content-type=" + contentType + ", progress=" + progress + ", expected=" + expected + "]";
    }
}
