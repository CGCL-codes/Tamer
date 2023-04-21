package hermes.browser.tasks;

import hermes.HermesConstants;
import hermes.browser.HermesBrowser;
import hermes.browser.IconCache;
import java.util.HashSet;
import java.util.Set;
import javax.jms.Message;
import javax.swing.Icon;
import org.apache.log4j.Logger;

/**
 * @author colincrist@hermesjms.com
 */
public abstract class TaskSupport implements Task, Runnable {

    private static final Logger log = Logger.getLogger(TaskSupport.class);

    private static final Set<TaskListener> globalListeners = new HashSet<TaskListener>();

    private Set<TaskListener> listeners = new HashSet<TaskListener>();

    private boolean isRunning = true;

    private Icon icon = IconCache.getIcon("jms.unknown");

    private Thread myThread;

    public static void addGlobalListener(TaskListener listener) {
        globalListeners.add(listener);
    }

    public static void removeGlobalListener(TaskListener listener) {
        globalListeners.remove(listener);
    }

    protected TaskSupport(Icon icon) {
        this.icon = icon;
        listeners.addAll(globalListeners);
    }

    public String getTitle() {
        return HermesConstants.EMPTY_STRING;
    }

    public Icon getIcon() {
        return icon;
    }

    public void addTaskListener(TaskListener taskListener) {
        synchronized (listeners) {
            listeners.add(taskListener);
        }
    }

    public void removeTaskListener(TaskListener taskListener) {
        synchronized (listeners) {
            listeners.remove(taskListener);
        }
    }

    protected void notifyThrowable(Throwable t) {
        for (TaskListener listener : listeners) {
            listener.onThrowable(this, t);
        }
    }

    protected void notifyMessage(Message message) {
        for (TaskListener listener : listeners) {
            if (listener instanceof MessageTaskListener) {
                ((MessageTaskListener) listener).onMessage(this, message);
            }
        }
    }

    protected void notifyStatus(String status) {
        for (TaskListener listener : listeners) {
            listener.onStatus(this, status);
        }
    }

    public synchronized void stop() {
        isRunning = false;
        if (myThread != null) {
            myThread.interrupt();
            myThread = null;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public abstract void invoke() throws Exception;

    public void run() {
        try {
            myThread = Thread.currentThread();
            try {
                for (TaskListener listener : listeners) {
                    listener.onStarted(this);
                }
                TaskSupport.this.invoke();
            } catch (Throwable t) {
                for (TaskListener listener : listeners) {
                    listener.onThrowable(this, t);
                }
            }
        } finally {
            for (TaskListener listener : listeners) {
                listener.onStopped(this);
            }
            listeners.clear();
            isRunning = false;
            myThread = null;
        }
    }

    public void start() {
        HermesBrowser.getBrowser().getThreadPool().invokeLater(this);
    }
}
