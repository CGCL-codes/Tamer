package com.k_int.IR;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public abstract class SearchTask extends Observable {

    public static final int TASK_IDLE = 0;

    public static final int TASK_EXECUTING_SYNC = 1;

    public static final int TASK_EXECUTING_ASYNC = 2;

    public static final int TASK_COMPLETE = 3;

    public static final int TASK_FAILURE = 4;

    protected int task_status_code = 0;

    protected String task_identifier = null;

    protected Object user_data = null;

    protected IRQuery query = null;

    protected long create_time = System.currentTimeMillis();

    protected Vector message_log = new Vector();

    protected int max_messages = 20;

    public SearchTask() {
        this.task_identifier = "" + this.hashCode();
    }

    public SearchTask(Observer[] observers) {
        this(null, observers);
    }

    public SearchTask(String task_identifier) {
        this(task_identifier, null);
    }

    public SearchTask(String task_identifier, Observer[] observers) {
        if (task_identifier != null) this.task_identifier = task_identifier; else this.task_identifier = "" + this.hashCode();
        if (observers != null) {
            for (int i = 0; i < observers.length; i++) addObserver(observers[i]);
            FragmentSourceEvent e = new FragmentSourceEvent(FragmentSourceEvent.SOURCE_RESET, null);
            setChanged();
            notifyObservers(e);
        }
    }

    /**
   * Evaluate the query, waiting at most timeout milliseconds, returning the
   * search status. InformationFragmentSource object should be used to check
   * the final number of result records.
   */
    public abstract int evaluate(int timeout) throws TimeoutExceededException, SearchException;

    public void addFragmentSourceObserver(Observer o) {
        addObserver(o);
    }

    public String getTaskIdentifier() {
        return task_identifier;
    }

    public int getTaskStatusCode() {
        return task_status_code;
    }

    public void setTaskStatusCode(int task_status_code) {
        synchronized (this) {
            this.task_status_code = task_status_code;
            this.notifyAll();
        }
        IREvent e = new IREvent(SearchTaskEvent.ST_PUBLIC_STATUS_CODE_CHANGE, new Integer(task_status_code));
        setChanged();
        notifyObservers(e);
    }

    public void waitForStatus(int status, long timeout) {
        long endtime = System.currentTimeMillis() + timeout;
        long remain = timeout;
        while ((getTaskStatusCode() != status) && (System.currentTimeMillis() < endtime)) {
            try {
                synchronized (this) {
                    this.wait(remain);
                }
            } catch (java.lang.InterruptedException ie) {
                remain = endtime - System.currentTimeMillis() + 10;
            }
        }
    }

    public int getPrivateTaskStatusCode() {
        return -1;
    }

    public String lookupPrivateStatusCode(int code) {
        return "Unknown";
    }

    public boolean hasSubtasks() {
        return false;
    }

    public Enumeration getSubtasks() {
        return null;
    }

    public void setUserData(Object o) {
        this.user_data = o;
    }

    public Object getUserData() {
        return user_data;
    }

    /** Cancel any active operation, but leave all the searchTask's data intact */
    public void cancelTask() {
    }

    /** getTaskResultSet. Search tasks delagate the responsibility of managing a result set to an instance
   * of the InformationFragmentSource. Often, the SearchTask will implement InformationFragmentSource
   * itself, and return (this) as the realisation of the getTaskResultSet method. Other SearchTasks
   * may use some cache managing FragmentSource to wrapper the source result set and return that object.
   */
    public abstract InformationFragmentSource getTaskResultSet();

    public void setQuery(IRQuery query) {
        this.query = query;
    }

    public IRQuery getQuery() {
        return query;
    }

    /** Get the last n status messages. */
    public Vector getLastStatusMessages() {
        return message_log;
    }

    /** Log a status message. */
    public void logStatusMessage(String s) {
        message_log.add(s);
        while (message_log.size() > max_messages) {
            message_log.removeElementAt(0);
        }
    }

    /** Store at most n status messages. */
    public void setMaxStatusMessageItems(int i) {
        max_messages = i;
    }

    public long getTaskCreationTime() {
        return create_time;
    }

    /** Shut down the task and release any resources, maybe notify our creating searchable */
    public void destroyTask() {
        deleteObservers();
    }
}
