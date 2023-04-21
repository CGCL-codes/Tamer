package abbot.swt;

import java.util.*;
import org.eclipse.swt.widgets.Display;

/** 
 * Since SWT does not use Java's synchronization support, this class was created
 * in order to coordinate calls to Display.syncExec(Runnable) among multiple 
 * displays.  This is accomplished by making all calls to Display.syncExec(Runnable)
 * from a dedicated Synchronizer Thread while preventing Displays associated with the
 * original calling thread from blocking.
 **/
public class Synchronizer {

    public static final String copyright = "Licensed Materials	-- Property of IBM\n" + "(c) Copyright International Business Machines Corporation, 2003\nUS Government " + "Users Restricted Rights - Use, duplication or disclosure restricted by GSA " + "ADP Schedule Contract with IBM Corp.";

    boolean disposed;

    LinkedList displays = new LinkedList();

    LinkedList actions = new LinkedList();

    LinkedList pendingRequests = new LinkedList();

    static final int SLEEP_TIME_NS = 10;

    static Synchronizer singleton;

    private Synchronizer() {
        disposed = false;
        Thread syncThread = new Thread() {

            public void run() {
                while (!isDisposed()) {
                    if (waitingRequest()) serviceRequest();
                }
            }
        };
        syncThread.setName("Abbot Synchronizer Thread");
        syncThread.start();
    }

    public static Synchronizer getSynchronizer() {
        if (singleton == null) {
            singleton = new Synchronizer();
        }
        return singleton;
    }

    public void syncExec(Display display, Runnable action) {
        Display currentThreadsDisplay = Display.findDisplay(Thread.currentThread());
        if (currentThreadsDisplay == display) {
            display.syncExec(action);
        }
        if (display == null) System.out.println("FOUND NULL DISPLAY"); else {
            makeRequest(display, action);
            while (!requestServiced()) {
                if (currentThreadsDisplay != null) {
                    currentThreadsDisplay.readAndDispatch();
                } else {
                    try {
                        Thread.sleep(0, SLEEP_TIME_NS);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized void makeRequest(Display display, Runnable action) {
        if (display.isDisposed()) System.out.println("FOUND DISPOSED DISPLAY");
        displays.add(display);
        actions.add(action);
        pendingRequests.add(Thread.currentThread());
    }

    private synchronized boolean requestServiced() {
        return !pendingRequests.contains(Thread.currentThread());
    }

    private void serviceRequest() {
        Display display;
        Runnable action;
        synchronized (this) {
            display = (Display) displays.getFirst();
            displays.removeFirst();
            action = (Runnable) actions.getFirst();
            actions.removeFirst();
        }
        if (display.isDisposed()) System.out.println("FOUND DISPOSED DISPLAY");
        display.wake();
        display.syncExec(action);
        synchronized (this) {
            pendingRequests.removeFirst();
        }
    }

    private synchronized boolean waitingRequest() {
        return pendingRequests.size() > 0;
    }

    public void dispose() {
        disposed = true;
    }

    public synchronized boolean isDisposed() {
        return disposed;
    }
}
