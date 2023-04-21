package org.dicom4j.toolkit.dimse.service;

import org.dicom4j.network.dimse.messages.DimseMessageFactory;
import org.dicom4j.network.dimse.messages.support.AbstractDimseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class to handle a DimseMessage received and notified if a C-Cancel was received during the process. 
 * <p>to use the worker you have to 
 * <ul>
 * <li>creates a sub-class and override <b>handleMessage</b> </li>
 * <li>set the {@link ServiceWorkerListener} when you creates a new worker</li>
 * </ul>
 *
 * @since 0.0.6
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public abstract class ServiceWorker extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ServiceWorker.class);

    protected static final DimseMessageFactory messageFactory = new DimseMessageFactory();

    private AbstractDimseMessage fMessage;

    /**
	 * the event listener
	 */
    private ServiceWorkerListener serviceWorkerListener;

    private boolean cancelRequested = false;

    public ServiceWorker(ServiceWorkerListener serviceWorkerListener, AbstractDimseMessage aMessage) {
        fMessage = aMessage;
        this.serviceWorkerListener = serviceWorkerListener;
    }

    public void run() {
        try {
            handleMessage();
        } catch (Exception e) {
            notifyException(e);
        }
    }

    /**
	 * methods to override to perform the message handling
	 * @throws Exception
	 */
    public abstract void handleMessage() throws Exception;

    /**
	 * must be called is a cancel request was sent while handleMessage is still in progress
	 */
    public void cancelRequested() {
        logger.debug("cancelRequested");
        cancelRequested = true;
    }

    /**
	 * used to notify the listener that an exceptions occurs
	 * @param cause
	 */
    protected void notifyException(Throwable cause) {
        if (this.serviceWorkerListener != null) {
            this.serviceWorkerListener.handleException(cause);
        } else {
            logger.warn("notifyException: no serviceWorkerListener was set");
        }
    }

    /**
	 * to notify the listener it should send a message to the remote peer
	 * @param message the message to send
	 * @param isFinished true to indicates this is the last message, and the worker has finish to perform the requested message
	 */
    protected void notifyMessageToSend(AbstractDimseMessage message, boolean isFinished) {
        if (this.serviceWorkerListener != null) {
            this.serviceWorkerListener.handleMessageToSend(message, isFinished);
        }
    }

    /**
	 * the {@link AbstractDimseMessage} to handle
	 * @return
	 */
    protected AbstractDimseMessage getMessage() {
        return fMessage;
    }

    /**
	 * return true if a Cancel request must be handle
	 * @return true if cancel
	 */
    public boolean isCancelRequested() {
        return cancelRequested;
    }
}
