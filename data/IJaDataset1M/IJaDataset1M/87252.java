package gov.nasa.jpf.jvm;

import java.util.BitSet;
import gov.nasa.jpf.Config;

/**
 * Contains the list of all currently active threads.
 *
 * Note that this list may both shrink or (re-) grow on backtrack. This imposes
 * a challenge for keeping ThreadInfo identities, which are otherwise nice for
 * directly storing ThreadInfo references in Monitors and/or listeners.
 */
public class ThreadList implements Cloneable {

    /**
   * The threads.
   */
    private ThreadInfo[] threads;

    /**
   * Reference of the kernel state this thread list belongs to.
   */
    public KernelState ks;

    private ThreadList() {
    }

    /**
   * Creates a new empty thread list.
   */
    public ThreadList(Config config, KernelState ks) {
        this.ks = ks;
        threads = new ThreadInfo[0];
    }

    public Object clone() {
        ThreadList other = new ThreadList();
        other.ks = ks;
        other.threads = new ThreadInfo[threads.length];
        for (int i = 0; i < threads.length; i++) {
            other.threads[i] = (ThreadInfo) threads[i].clone();
        }
        return other;
    }

    public int add(ThreadInfo ti) {
        int n = threads.length;
        for (ThreadInfo t : threads) {
            if (t == ti) {
                return t.getIndex();
            }
        }
        ThreadInfo[] newList = new ThreadInfo[n + 1];
        System.arraycopy(threads, 0, newList, 0, n);
        newList[n] = ti;
        threads = newList;
        return n;
    }

    public boolean hasAnyAliveThread() {
        for (int i = 0, l = threads.length; i < l; i++) {
            if (threads[i].isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
   * Returns the array of threads.
   */
    public ThreadInfo[] getThreads() {
        return threads.clone();
    }

    /**
   * Returns a specific thread.
   */
    public ThreadInfo get(int index) {
        return threads[index];
    }

    /**
   * Returns the length of the list.
   */
    public int length() {
        return threads.length;
    }

    /**
   * Replaces the array of ThreadInfos.
   */
    public void setAll(ThreadInfo[] threads) {
        this.threads = threads;
    }

    public ThreadInfo locate(int objref) {
        for (int i = 0, l = threads.length; i < l; i++) {
            if (threads[i].getThreadObjectRef() == objref) {
                return threads[i];
            }
        }
        return null;
    }

    public void markRoots() {
        for (int i = 0, l = threads.length; i < l; i++) {
            if (threads[i].isAlive()) {
                threads[i].markRoots();
            }
        }
    }

    /**
   * return if there are still runnables, and there is at least one
   * non-daemon thread left 
   */
    public boolean hasMoreThreadsToRun() {
        int nonDaemons = 0;
        int runnables = 0;
        for (int i = 0; i < threads.length; i++) {
            ThreadInfo ti = threads[i];
            if (!ti.isDaemon() && !ti.isTerminated()) {
                nonDaemons++;
            }
            if (ti.isTimeoutRunnable()) {
                runnables++;
            }
        }
        return (nonDaemons > 0) && (runnables > 0);
    }

    public int getNonDaemonThreadCount() {
        int nd = 0;
        for (int i = 0; i < threads.length; i++) {
            if (!threads[i].isDaemon()) {
                nd++;
            }
        }
        return nd;
    }

    public int getRunnableThreadCount() {
        int n = 0;
        for (int i = 0; i < threads.length; i++) {
            if (threads[i].isTimeoutRunnable()) {
                n++;
            }
        }
        return n;
    }

    public ThreadInfo[] getRunnableThreads() {
        int nRunnable = getRunnableThreadCount();
        ThreadInfo[] list = new ThreadInfo[nRunnable];
        for (int i = 0, j = 0; i < threads.length; i++) {
            if (threads[i].isTimeoutRunnable()) {
                list[j++] = threads[i];
                if (j == nRunnable) {
                    break;
                }
            }
        }
        return list;
    }

    public ThreadInfo[] getRunnableThreadsWith(ThreadInfo ti) {
        int nRunnable = getRunnableThreadCount();
        ThreadInfo[] list = new ThreadInfo[ti.isRunnable() ? nRunnable : nRunnable + 1];
        for (int i = 0, j = 0; i < threads.length; i++) {
            if (threads[i].isTimeoutRunnable() || (threads[i] == ti)) {
                list[j++] = threads[i];
                if (j == list.length) {
                    break;
                }
            }
        }
        return list;
    }

    public ThreadInfo[] getRunnableThreadsWithout(ThreadInfo ti) {
        int nRunnable = getRunnableThreadCount();
        if (ti.isRunnable()) {
            nRunnable--;
        }
        ThreadInfo[] list = new ThreadInfo[nRunnable];
        for (int i = 0, j = 0; i < threads.length; i++) {
            if (threads[i].isTimeoutRunnable() && (ti != threads[i])) {
                list[j++] = threads[i];
                if (j == nRunnable) {
                    break;
                }
            }
        }
        return list;
    }

    public int getLiveThreadCount() {
        int n = 0;
        for (int i = 0; i < threads.length; i++) {
            if (threads[i].isAlive()) {
                n++;
            }
        }
        return n;
    }

    boolean hasOtherRunnablesThan(ThreadInfo ti) {
        int n = threads.length;
        for (int i = 0; i < n; i++) {
            if (threads[i] != ti) {
                if (threads[i].isRunnable()) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isDeadlocked() {
        boolean hasBlockedThreads = false;
        for (int i = 0; i < threads.length; i++) {
            ThreadInfo ti = threads[i];
            if (ti.isTimeoutRunnable()) {
                return false;
            }
            if (ti.isAlive()) {
                hasBlockedThreads = true;
            }
        }
        return hasBlockedThreads;
    }

    public void sweepTerminated(BitSet isUsed) {
    }

    public void dump() {
        int i = 0;
        for (ThreadInfo t : threads) {
            System.err.println("[" + i++ + "] " + t);
        }
    }
}
