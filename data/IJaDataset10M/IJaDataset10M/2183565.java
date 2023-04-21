package com.alexmcchesney.poster.plugins.delicious;

import org.apache.log4j.Logger;
import java.util.Calendar;
import com.alexmcchesney.delicious.*;
import com.alexmcchesney.operations.*;

/**
 * Abstract base class for all asynchronous operations provided by the del.icio.us plugin.
 * @author amcchesney
 *
 */
public abstract class DeliciousPluginOperation implements IAsynchronousOperation {

    /** Flag indicating whether a cancellation has been requested */
    private boolean m_bCancelled = false;

    /** Current status of the operation */
    protected DeliciousOperationStatus m_status = new DeliciousOperationStatus();

    /** Log4j interface */
    protected static Logger m_log = Logger.getLogger(DeliciousPluginOperation.class);

    /** Default url to del.icio.us api */
    public static final String DEFAULT_ENDPOINT = Service.DEFAULT_API_URL;

    /**
	 * Called when the user wishes to cancel the operation.  Simply sets the
	 * m_bCancelled flag to true.  Sub-classes can call cancellationCheck
	 * to cause an OperationCancelledException to be thrown if this is true.
	 */
    public void cancel() {
        m_bCancelled = true;
    }

    /**
	 * If it seems that we wish to cancel the operation, throw an exception
	 * @throws OperationCancelledException	Thrown if the operation is to be cancelled
	 */
    public void cancellationCheck() throws OperationCancelledException {
        if (m_bCancelled) {
            throw new OperationCancelledException();
        }
    }

    /**
	 * Gets the status object for the operation
	 */
    public OperationStatus getStatus() {
        return m_status.cloneStatus();
    }

    /**
	 * Actually kicks off the operation
	 */
    public void run() {
        m_status.setStatus(OperationStatus.RUNNING);
        m_status.setStartTime(Calendar.getInstance().getTime());
        try {
            doOperation();
        } catch (Throwable t) {
            if (t instanceof OperationCancelledException) {
                m_status.setStatus(OperationStatus.CANCELLED);
            } else {
                m_status.setError(t);
                m_status.setStatus(OperationStatus.FAILED);
            }
        }
        if (m_status.getStatus() == OperationStatus.RUNNING) {
            m_status.setStatus(OperationStatus.SUCCEEDED);
        }
        m_status.setFinishTime(Calendar.getInstance().getTime());
    }

    /**
	 * Abstract class
	 *
	 */
    public abstract void doOperation() throws Exception;
}
