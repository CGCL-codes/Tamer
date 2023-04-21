package com.googlecode.icefusion.ui.commons.push;

import java.io.Serializable;
import com.googlecode.icefusion.ui.commons.dialog.Dialog;
import com.icesoft.faces.async.render.SessionRenderer;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * Manages the behavior of a ProgressDialog. Opening the dialog initializes the progress bar. A change of the progress
 * attribute initiates an explicit rendering in the browser.
 * 
 * @author Rainer Eschen
 * 
 */
public class ProgressDialog extends Dialog implements Serializable {

    protected static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 15, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(20));

    /**
     * Percentage value to show in dialog.
     */
    private Long progress = 0L;

    /**
     * Process the progress is presented for.
     */
    private IProgressDialog process;

    /**
     * Context for session renderer output
     */
    private String renderGroup = "progressbar";

    /**
     * true: Cancel button pressed. Stop progress bar, close dialog.
     */
    private Boolean cancel = false;

    /**
     * true: everything is processed (progress==100). Show OK button instead of Cancel.
     */
    private Boolean ready = false;

    /**
     * Initialize the progress dialog.
     * 
     * @param process process that delivers data and can process cancellation.
     */
    public void startProcess(IProgressDialog process) {
        this.process = process;
        this.setReady(false);
        this.setCancel(false);
        this.setProgress(0L);
        this.setShow(true);
        SessionRenderer.addCurrentSession(this.renderGroup);
        threadPool.execute(new updateThread());
    }

    /**
     * CANCEL button management.
     * 
     * @return navigation id
     */
    public String buttonCancel() {
        this.setCancel(true);
        this.setShow(false);
        return null;
    }

    /**
     * OK button management.
     * 
     * @return navigation id
     */
    public String buttonOk() {
        this.setShow(false);
        return null;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    /**
     * Manages the continuous updates of the progress bar.
     */
    protected class updateThread implements Runnable {

        /**
         * Last presented percentage
         */
        private Long last = 0L;

        private Long current = 0L;

        public void run() {
            while ((last < 100) && !cancel) {
                this.current = process.getProgress();
                if (this.current > this.last) {
                    last = current;
                    progress = current;
                    SessionRenderer.render(renderGroup);
                }
                try {
                    Thread.sleep(500);
                    if (cancel) {
                        break;
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            if (!cancel) {
                ready = true;
            } else {
                process.setCancel(cancel);
            }
            SessionRenderer.render(renderGroup);
        }
    }
}
