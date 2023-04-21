package frost.messaging.frost.boards;

import java.util.*;

/**
 * This class implements most methods needed by the BoardUpdateThread interface.
 * A thread have to extend this class and to implement BoardUpdateThread.
 */
public class BoardUpdateThreadObject extends Thread {

    private final Board targetBoard;

    private final Vector<BoardUpdateThreadListener> registeredListeners;

    long startTimeMillis = -1;

    boolean isFinished = false;

    public BoardUpdateThreadObject(final Board board) {
        super(board.getName());
        this.targetBoard = board;
        this.registeredListeners = new Vector<BoardUpdateThreadListener>();
    }

    public Board getTargetBoard() {
        return targetBoard;
    }

    /**
     * Returns -1 if not yet started
    */
    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public synchronized boolean isFinished() {
        return isFinished;
    }

    protected synchronized void threadFinished() {
        isFinished = true;
    }

    /**
     * Called from Thread to notify all listeners that thread is started now
     */
    protected void notifyThreadStarted(final BoardUpdateThread thread) {
        this.startTimeMillis = System.currentTimeMillis();
        final Iterator<BoardUpdateThreadListener> i = registeredListeners.iterator();
        while (i.hasNext()) {
            i.next().boardUpdateThreadStarted(thread);
        }
    }

    /**
     * Called from Thread to notify all listeners that thread is started now
     */
    protected void notifyThreadFinished(final BoardUpdateThread thread) {
        threadFinished();
        final Iterator<BoardUpdateThreadListener> i = registeredListeners.iterator();
        while (i.hasNext()) {
            i.next().boardUpdateThreadFinished(thread);
        }
    }

    /**
     * Called from Thread to notify all listeners that the BoardUpdateInformation changed.
     */
    protected void notifyBoardUpdateInformationChanged(final BoardUpdateThread thread, final BoardUpdateInformation bui) {
        final Iterator<BoardUpdateThreadListener> i = registeredListeners.iterator();
        while (i.hasNext()) {
            i.next().boardUpdateInformationChanged(thread, bui);
        }
    }

    /**
     * allow to register listener, should only be used by RunningBoardUpdateThreads class
     * other classes should register to the RunningBoardUpdateThreads class
     * the difference is: if the thread class the listener directly, the ArrayList = null
     * the underlying Thread class should fire the finished event with parameters:
     *   boardUpdateThreadFinished(null, this)
     */
    public void addBoardUpdateThreadListener(final BoardUpdateThreadListener listener) {
        registeredListeners.add(listener);
    }

    public void removeBoardUpdateThreadListener(final BoardUpdateThreadListener listener) {
        registeredListeners.remove(listener);
    }
}
