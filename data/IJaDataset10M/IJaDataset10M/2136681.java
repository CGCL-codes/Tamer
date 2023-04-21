package net.sf.jabs.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.jabs.util.Helper;

public class JobMonitorMessage {

    private String _message;

    private String _details;

    private String _time = Helper.getDateString("hh:mm:ss", System.currentTimeMillis());

    private List<String> _progress = Collections.synchronizedList(new ArrayList<String>());

    public static final int PROGRESS_HISTORY_SIZE = 30;

    public JobMonitorMessage() {
        _message = "";
    }

    public JobMonitorMessage(String message, String monitor) {
        _message = message;
        _details = monitor;
    }

    public JobMonitorMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getTime() {
        return _time;
    }

    public void setIndent(String i) {
        _message = i + _message;
    }

    public String getDetails() {
        return _details;
    }

    public void setDetails(String monitor) {
        _details = monitor;
    }

    /**
     * Adds lines to the progress list from a string with 
     * multiple lines separated by line feeds.  
     * @param progress
     */
    public void setProgressMultiline(String progress) {
    }

    /**
     * Adds a single line to the progress array.
     * @param progress
     */
    public void setProgress(String progress) {
        _progress.add(progress);
        if (_progress.size() > PROGRESS_HISTORY_SIZE) {
            _progress.remove(0);
        }
    }

    /**
     * Return the list of messages.
     * @return
     */
    public List<String> getProgress() {
        return _progress;
    }

    /**
     * Return the messages as a single String.
     * @return
     */
    public String getProgressAsString() {
        StringBuilder sb = new StringBuilder();
        synchronized (_progress) {
            for (String line : _progress) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
