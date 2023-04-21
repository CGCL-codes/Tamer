package javax.media.ding3d;

import java.util.LinkedList;

/**
 * The NotificationThread class is used for asynchronous error notification,
 * such as notifying ShaderError listeners.
 */
class NotificationThread extends Thread {

    private static final int WAIT = 0;

    private static final int NOTIFY = 1;

    private static final int STOP = 2;

    private volatile boolean running = true;

    private boolean waiting = false;

    private boolean ready = false;

    private LinkedList notificationQueue = new LinkedList();

    /**
     * Creates a new instance of NotificationThread
     */
    NotificationThread(ThreadGroup t) {
        super(t, "Ding3d-NotificationThread");
    }

    /**
     * Adds a notification message to the queue
     */
    synchronized void addNotification(Ding3dNotification n) {
        notificationQueue.add(n);
        runMonitor(NOTIFY);
    }

    /**
     * Gets the list of queued notification messages
     */
    private synchronized Ding3dNotification[] getNotifications() {
        Ding3dNotification[] notifications = (Ding3dNotification[]) notificationQueue.toArray(new Ding3dNotification[0]);
        notificationQueue.clear();
        return notifications;
    }

    /**
     * Processes all pending notification messages
     */
    private void processNotifications() {
        Ding3dNotification[] notifications = getNotifications();
        for (int i = 0; i < notifications.length; i++) {
            Ding3dNotification n = notifications[i];
            switch(n.type) {
                case Ding3dNotification.SHADER_ERROR:
                    n.universe.notifyShaderErrorListeners((ShaderError) n.args[0]);
                    break;
                case Ding3dNotification.RENDERING_ERROR:
                    VirtualUniverse.notifyRenderingErrorListeners((RenderingError) n.args[0]);
                    break;
                default:
                    System.err.println("Ding3dNotification.processNotifications: unrecognized type = " + n.type);
            }
        }
    }

    void finish() {
        runMonitor(STOP);
    }

    public void run() {
        while (running) {
            runMonitor(WAIT);
            processNotifications();
        }
    }

    private synchronized void runMonitor(int action) {
        switch(action) {
            case WAIT:
                while (running && !ready) {
                    waiting = true;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                    waiting = false;
                }
                ready = false;
                break;
            case NOTIFY:
                ready = true;
                if (waiting) {
                    notify();
                }
                break;
            case STOP:
                running = false;
                notify();
                break;
            default:
                assert (false);
        }
    }
}
