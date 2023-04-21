package gwtupload.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This File Upload Listener is used by Apache Commons File Upload to
 * monitor the progress of the uploaded file.
 * 
 * This Listener saves itself into a unique map in memory.
 * It doesn't work when the application is deployed in cluster.
 * 
 * It is thought to be used in systems where session objects 
 * are not updated until the request has finished. 
 * 
 * @author Manolo Carrasco Moñino
 * 
 */
public class MemoryUploadListener extends AbstractUploadListener {

    private static final long serialVersionUID = 7395899170157906525L;

    private static final Map<String, MemoryUploadListener> listeners = new HashMap<String, MemoryUploadListener>();

    public static MemoryUploadListener current(String sessionId) {
        MemoryUploadListener listener = listeners.get(sessionId);
        logger.debug(className + " " + sessionId + " get " + listener);
        return listener;
    }

    public MemoryUploadListener(int sleepMilliseconds, long requestSize) {
        super(sleepMilliseconds, requestSize);
    }

    public void remove() {
        listeners.remove(sessionId);
        logger.info(className + " " + sessionId + " Remove " + this.toString());
        current(sessionId);
    }

    public void save() {
        listeners.put(sessionId, this);
        saved = new Date();
        logger.debug(className + " " + sessionId + " Saved " + this.toString());
    }
}
