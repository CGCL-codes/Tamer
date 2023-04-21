package org.isqlviewer.event;

import java.util.EventListener;

/**
 * Listener Object to complement the ProgressEvent object.
 * <p>
 * 
 * @author Mark A. Kobold &lt;mkobold at isqlviewer dot com&gt;
 * @version 1.0
 */
public interface ProgressListener extends EventListener {

    /**
     * Delegate method for progress events.
     * <p>
     * ProgressEvents should be processed with this method.
     * 
     * @param pe current progress event to process.
     */
    public void progressUpdated(ProgressEvent pe);
}
