package com.foursoft.fourever.history.impl;

import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.fourever.history.History;
import com.foursoft.fourever.history.HistoryManager;
import com.foursoft.fourever.history.MementoReceiverRegistration;
import org.apache.commons.logging.Log;

/**
 * Manager of the history component. It is basically a factory for new history
 * objects which are then used to undo and redo changes.
 * 
 * 
 */
public class HistoryManagerImpl implements HistoryManager {

    private static HistoryManager instance = null;

    static Log log = null;

    /**
	 * Constructor to be called only by createInstance.
	 */
    private HistoryManagerImpl() {
    }

    /**
	 * Create the component manager, thus initializing the component.
	 * 
	 * @param myLog
	 *            the Log to use
	 * @return the singleton instance of the HistoryManager
	 */
    public static HistoryManager createInstance(Log myLog) {
        if (instance != null) {
            throw new ComponentInternalException("Could not initialize the history manager - already existing.");
        }
        instance = new HistoryManagerImpl();
        if (myLog == null) {
            throw new ComponentInternalException("Could not initialize the history log.");
        }
        log = myLog;
        log.debug("Creating the history manager");
        return instance;
    }

    /**
	 * Destroy the component manager.
	 */
    public static void destroy() {
        log.debug("Destroying the history manager");
        instance = null;
        log = null;
    }

    /**
	 * Create a new History instance and register it with a
	 * MementoReceiverRegistration.
	 * 
	 * @param mReg
	 *            the MementoReceiverRegistration to register with
	 * @return a new history
	 */
    public History createHistory(MementoReceiverRegistration mReg) {
        assert (mReg != null);
        log.debug("Creating a new history.");
        return new HistoryImpl(mReg);
    }
}
