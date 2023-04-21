package sample.threadCheck.old;

import edu.rice.cs.cunit.threadCheck.OnlyRunBy;

/**
 * A test for annotating classes and methods with allowed threads.
 * @author Mathias Ricken
 */
@OnlyRunBy(threadNames = { "main2" })
public class ThreadCheckSample3 {

    @OnlyRunBy(threadNames = { "main", "main3" }, threadIds = { 3, 4 }, eventThread = OnlyRunBy.EVENT_THREAD.ONLY)
    public void run() {
        System.out.println("run");
    }

    @OnlyRunBy(threadNames = { "main3" }, threadGroups = { "foo", "bar" }, threadIds = { 3, 4 })
    public static void main(String[] args) {
        System.out.println("main");
        final Thread ct = Thread.currentThread();
        System.out.println("thread name = '" + ct.getName() + "'");
        System.out.println("thread id   = " + ct.getId());
        System.out.println("thread grp  = '" + ct.getThreadGroup().getName() + "'");
        (new ThreadCheckSample3()).run();
    }
}
