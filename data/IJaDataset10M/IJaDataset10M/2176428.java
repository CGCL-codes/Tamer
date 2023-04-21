package org.jikesrvm.scheduler.greenthreads;

import org.jikesrvm.runtime.VM_Time;

/**
 * A collection of static methods for waiting on some type of event.
 * These methods may allocate objects for recordkeeping purposes,
 * and thus may not be called from within the scheduler proper
 * (e.g., <code>VM_Thread</code>, which is uninterruptible).
 */
class VM_Wait {

    private static boolean noIoWait = false;

    /**
   * Called by VM.sysExit() to disable IO waits while the VM is
   * shutting down (and flushing its output streams).
   * The reason is that we can't be sure that thread switching
   * is possible during shutdown.
   */
    public static void disableIoWait() {
        noIoWait = true;
    }

    /**
   * Given a total number of seconds to wait, computes timestamp
   * of time when the wait should time out.
   * Leaves negative times unchanged, since these indicate infinite waits.
   */
    private static long getMaxWaitNano(double totalWaitTimeInSeconds) {
        long maxWaitNano = (long) (totalWaitTimeInSeconds * 1e9);
        if (maxWaitNano >= 0) {
            maxWaitNano += VM_Time.nanoTime();
        }
        return maxWaitNano;
    }

    /**
   * Suspend execution of current thread until "fd" can be read without
   * blocking.
   * @param fd the native file descriptor to wait on
   * @param totalWaitTime the number of seconds to wait; negative values
   *   indicate an infinite wait time
   * @return the wait data object indicating the result of the wait
   */
    public static VM_ThreadIOWaitData ioWaitRead(int fd, double totalWaitTime) {
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitNano);
        waitData.readFds = new int[] { fd };
        if (noIoWait) {
            waitData.markAllAsReady();
        } else {
            VM_GreenThread.ioWaitImpl(waitData);
        }
        return waitData;
    }

    /**
   * Infinite wait for a read file descriptor to become ready.
   */
    public static VM_ThreadIOWaitData ioWaitRead(int fd) {
        return ioWaitRead(fd, VM_ThreadEventConstants.WAIT_INFINITE);
    }

    /**
   * Suspend execution of current thread until "fd" can be written without
   * blocking.
   * @param fd the native file descriptor to wait on
   * @param totalWaitTime the number of seconds to wait; negative values
   *   indicate an infinite wait time
   * @return the wait data object indicating the result of the wait
   */
    public static VM_ThreadIOWaitData ioWaitWrite(int fd, double totalWaitTime) {
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitNano);
        waitData.writeFds = new int[] { fd };
        if (noIoWait) {
            waitData.markAllAsReady();
        } else {
            VM_GreenThread.ioWaitImpl(waitData);
        }
        return waitData;
    }

    /**
   * Infinite wait for a write file descriptor to become ready.
   */
    public static VM_ThreadIOWaitData ioWaitWrite(int fd) {
        return ioWaitWrite(fd, VM_ThreadEventConstants.WAIT_INFINITE);
    }

    /**
   * Suspend execution of current thread until any of the given
   * file descriptors have become ready, or the wait times out.
   * When this method returns, the file descriptors which are
   * ready will have
   * {@link VM_ThreadIOConstants#FD_READY_BIT FD_READY_BIT} set,
   * and file descriptors which are invalid will have
   * {@link VM_ThreadIOConstants#FD_INVALID_BIT FD_INVALID_BIT} set.
   *
   * @param readFds array of read file descriptors
   * @param writeFds array of write file descriptors
   * @param exceptFds array of exception file descriptors
   * @param totalWaitTime amount of time to wait, in seconds; if
   *   negative, wait indefinitely
   * @param fromNative true if this select is being called
   *   from native code
   */
    public static void ioWaitSelect(int[] readFds, int[] writeFds, int[] exceptFds, double totalWaitTime, boolean fromNative) {
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        VM_ThreadIOWaitData waitData = new VM_ThreadIOWaitData(maxWaitNano);
        waitData.readFds = readFds;
        waitData.writeFds = writeFds;
        waitData.exceptFds = exceptFds;
        if (fromNative) {
            waitData.setNative();
        }
        if (noIoWait) {
            waitData.markAllAsReady();
        } else {
            VM_GreenThread.ioWaitImpl(waitData);
        }
    }

    /**
   * Suspend execution of current thread until process whose
   * pid is given has finished, or the thread is interrupted.
   * @param process the object representing the process to wait for
   * @param totalWaitTime number of seconds to wait, or negative if
   *   caller wants to wait indefinitely
   * @return the <code>VM_ThreadProcessWaitData</code> representing
   *   the state of the process
   */
    public static VM_ThreadProcessWaitData processWait(VM_Process process, double totalWaitTime) throws InterruptedException {
        long maxWaitNano = getMaxWaitNano(totalWaitTime);
        VM_ThreadProcessWaitData waitData = new VM_ThreadProcessWaitData(process.getPid(), maxWaitNano);
        VM_GreenThread.processWaitImpl(waitData, process);
        return waitData;
    }
}
