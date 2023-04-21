package be.vanvlerken.bert.logmonitor.logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Provides a basic implementation for firing database events to database listeners
 */
public abstract class AbstractLogView implements ILogView, Runnable {

    private static Logger logger = Logger.getLogger(AbstractLogView.class);

    class ModifiedSection {

        public ModifiedSection() {
            startIndex = 0;
            endIndex = 0;
            sectionModified = false;
        }

        public int startIndex;

        public int endIndex;

        public boolean sectionModified;
    }

    private List listeners;

    private Thread dispatchThread;

    private List entriesAdded;

    private ModifiedSection modifiedSection;

    private boolean fullModify;

    private boolean active;

    public AbstractLogView() {
        listeners = new ArrayList();
        entriesAdded = new ArrayList();
        modifiedSection = new ModifiedSection();
        fullModify = true;
        active = false;
        dispatchThread = null;
    }

    public synchronized void activate() {
        dispatchThread = new Thread(this);
        active = true;
        dispatchThread.start();
    }

    public synchronized void deactivate() {
        active = false;
        notify();
    }

    /**
	 * @see be.vanvlerken.bert.logmonitor.ILogView#get(int)
	 */
    public abstract ILogEntry get(int index);

    /**
	 * @see be.vanvlerken.bert.logmonitor.ILogView#getSize()
	 */
    public abstract int getSize();

    /**
	 * @see be.vanvlerken.bert.logmonitor.ILogView#addDatabaseListener(be.vanvlerken.bert.logmonitor.IDatabaseListener)
	 */
    public synchronized void addDatabaseListener(IDatabaseListener listener) {
        listeners.add(listener);
    }

    protected synchronized void fireEntryAdded(ILogEntry entry) {
        entriesAdded.add(entry);
        notify();
    }

    protected synchronized void fireEntriesModified(int startIndex, int endIndex) {
        if (modifiedSection.sectionModified) {
            if (modifiedSection.startIndex > startIndex) {
                modifiedSection.startIndex = startIndex;
            }
            if (modifiedSection.endIndex < endIndex) {
                modifiedSection.endIndex = endIndex;
            }
        } else {
            modifiedSection.startIndex = startIndex;
            modifiedSection.endIndex = endIndex;
            modifiedSection.sectionModified = true;
        }
        notify();
    }

    protected synchronized void fireDatabaseModified() {
        fullModify = true;
        notify();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public synchronized void run() {
        while (active) {
            if (fullModify) {
                triggerDatabaseModified();
            } else {
                if (!entriesAdded.isEmpty()) {
                    triggerEntriesAdded();
                }
                if (modifiedSection.sectionModified) {
                    triggerEntriesModified();
                }
            }
            try {
                wait();
            } catch (InterruptedException e) {
                logger.info("Got interrupted...");
            }
        }
    }

    /**
	 * Method triggerDatabaseModified.
	 */
    private void triggerDatabaseModified() {
        if (listeners.isEmpty()) {
            fullModify = false;
            return;
        }
        IDatabaseListener listener;
        Iterator listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            listener = (IDatabaseListener) listenerIterator.next();
            listener.databaseModified();
        }
        fullModify = false;
    }

    /**
     * Method triggerEntriesAdded
     */
    private void triggerEntriesAdded() {
        if (listeners.isEmpty()) {
            entriesAdded.clear();
            return;
        }
        IDatabaseListener listener;
        ILogEntry logEntry;
        Iterator entryIterator = entriesAdded.iterator();
        while (entryIterator.hasNext()) {
            logEntry = (ILogEntry) entryIterator.next();
            Iterator listenerIterator = listeners.iterator();
            while (listenerIterator.hasNext()) {
                listener = (IDatabaseListener) listenerIterator.next();
                listener.entryAdded(logEntry);
            }
        }
        entriesAdded.clear();
    }

    /**
     * Method triggerEntriesModified.
     */
    private void triggerEntriesModified() {
        if (listeners.isEmpty()) {
            modifiedSection.sectionModified = false;
            modifiedSection.startIndex = Integer.MAX_VALUE;
            modifiedSection.endIndex = Integer.MIN_VALUE;
            return;
        }
        IDatabaseListener listener;
        Iterator listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            listener = (IDatabaseListener) listenerIterator.next();
            listener.entriesModified(modifiedSection.startIndex, modifiedSection.endIndex);
        }
        modifiedSection.sectionModified = false;
        modifiedSection.startIndex = Integer.MAX_VALUE;
        modifiedSection.endIndex = Integer.MIN_VALUE;
    }
}
