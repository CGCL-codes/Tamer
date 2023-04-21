package org.netbeans.core.startup;

import org.openide.ErrorManager;

/** The ThreadGroup for catching uncaught exceptions in Corona.
*
* @author   Ian Formanek
*/
final class TopThreadGroup extends ThreadGroup implements Runnable {

    /** The command line args */
    private String[] args;

    /** flag that indicates whether the main thread had finished or not */
    private boolean finished;

    /** Constructs a new thread group. The parent of this new group is
    * the thread group of the currently running thread.
    *
    * @param name the name of the new thread group.
    */
    public TopThreadGroup(String name, String[] args) {
        super(name);
        this.args = args;
    }

    /** Creates a new thread group. The parent of this new group is the
    * specified thread group.
    * <p>
    * The <code>checkAccess</code> method of the parent thread group is
    * called with no arguments; this may result in a security exception.
    *
    * @param parent the parent thread group.
    * @param name the name of the new thread group.
    * @exception  NullPointerException  if the thread group argument is
    *             <code>null</code>.
    * @exception  SecurityException  if the current thread cannot create a
    *             thread in the specified thread group.
    * @see java.lang.SecurityException
    * @see java.lang.ThreadGroup#checkAccess()
    */
    public TopThreadGroup(ThreadGroup parent, String name) {
        super(parent, name);
    }

    public void uncaughtException(Thread t, Throwable e) {
        if (!(e instanceof ThreadDeath) && !(e instanceof IllegalMonitorStateException && "AWT-Selection".equals(t.getName()))) {
            if (e instanceof VirtualMachineError) {
                e.printStackTrace();
            }
            System.err.flush();
            ErrorManager.getDefault().notify(e);
        } else {
            super.uncaughtException(t, e);
        }
    }

    public synchronized void start() throws InterruptedException {
        Thread t = new Thread(this, this, "main");
        t.start();
        while (!finished) {
            wait();
        }
    }

    public void run() {
        try {
            Main.start(args);
        } catch (Throwable t) {
            t.printStackTrace();
            ErrorManager.getDefault().notify(t);
        } finally {
            synchronized (this) {
                finished = true;
                notify();
            }
        }
    }
}
