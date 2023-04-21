package org.columba.core.command;

import org.columba.api.command.IWorkerStatusChangeListener;
import org.columba.api.command.IWorkerStatusController;

/**
 * @author timo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NullWorkerStatusController implements IWorkerStatusController {

    public void removeWorkerStatusChangeListener(IWorkerStatusChangeListener listener) {
    }

    private static NullWorkerStatusController myInstance;

    protected NullWorkerStatusController() {
    }

    public static NullWorkerStatusController getInstance() {
        if (myInstance == null) {
            myInstance = new NullWorkerStatusController();
        }
        return myInstance;
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#setDisplayText(java.lang.String)
     */
    public void setDisplayText(String text) {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#clearDisplayText()
     */
    public void clearDisplayText() {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#clearDisplayTextWithDelay()
     */
    public void clearDisplayTextWithDelay() {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#getDisplayText()
     */
    public String getDisplayText() {
        return null;
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#setProgressBarMaximum(int)
     */
    public void setProgressBarMaximum(int max) {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#setProgressBarValue(int)
     */
    public void setProgressBarValue(int value) {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#resetProgressBar()
     */
    public void resetProgressBar() {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#incProgressBarValue()
     */
    public void incProgressBarValue() {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#incProgressBarValue(int)
     */
    public void incProgressBarValue(int increment) {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#getProgessBarMaximum()
     */
    public int getProgessBarMaximum() {
        return 0;
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#getProgressBarValue()
     */
    public int getProgressBarValue() {
        return 0;
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#cancel()
     */
    public void cancel() {
    }

    /**
     * @see org.columba.api.command.IWorkerStatusController#cancelled()
     */
    public boolean cancelled() {
        return false;
    }

    public void addWorkerStatusChangeListener(IWorkerStatusChangeListener l) {
    }

    public int getTimeStamp() {
        return 0;
    }
}
