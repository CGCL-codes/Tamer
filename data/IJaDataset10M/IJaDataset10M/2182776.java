package projectviewer;

/**
Parse out the Project Resources if they haven't already been done.
*/
public class ThreadedParser extends Thread {

    /**
   * Create a new <code>ThreadedParser</code>.
   */
    public ThreadedParser() {
        setPriority(Thread.MIN_PRIORITY);
    }

    /**
   * Thread logic.
   */
    public void run() {
        ProjectManager.getInstance();
    }
}
