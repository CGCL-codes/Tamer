package DEOS;

import gov.nasa.jpf.jvm.Verify;

/**
 * DOCUMENT ME!
 */
class DEOSThread {

    Thread thread;

    boolean running = false;

    boolean isMain = false;

    boolean isIdle = false;

    boolean firstTime = true;

    boolean setDelete = false;

    boolean setWaitUntilNextPeriod = false;

    public DEOSThread(Thread th) {
        thread = th;
        isIdle = thread.isIdle();
        isMain = thread.isMain();
        System.out.println(thread.toString() + " created");
    }

    public void run(int tickResult) {
        DEOS.inc();
        if (tickResult == Clock.NOTIMECHANGE) {
            if (Verify.randomBool()) {
                DEOS.println("No interrupt - Choice 0:");
                DEOS.println(thread.toString() + " waiting until next period");
                DEOSKernel.waitUntilNextPeriodK(thread);
            } else {
                DEOS.println("No interrupt - Choice 1:");
                DEOS.println(thread.toString() + " deleting");
                DEOSKernel.deleteThreadK(thread);
            }
        } else {
            switch(Verify.random(2)) {
                case 0:
                    DEOS.println("Choice 0:");
                    DEOS.println(thread.toString() + " waiting until next period");
                    DEOSKernel.waitUntilNextPeriodK(thread);
                    break;
                case 2:
                    DEOS.println("Choice 2:");
                    DEOS.println(thread.toString() + " deleting");
                    DEOSKernel.deleteThreadK(thread);
                    break;
                case 1:
                    DEOS.println("Choice 1: ");
                    getInterrupted(tickResult);
                    break;
            }
        }
    }

    void getInterrupted(int tickResult) {
        if (tickResult == Clock.SYSTEMINTERRUPT) {
            DEOS.println(thread.toString() + " interrupted by system tick");
            DEOS.thePeriodicClock.resetUsedTime();
            Scheduler.handleSystemTickInterrupt();
        } else if (tickResult == Clock.TIMEOUT) {
            DEOS.println(thread.toString() + " interrupted by timer");
            Scheduler.handleTimerInterrupt();
        } else {
            DEOS.println(thread.toString() + " waiting for time to pass");
        }
    }
}
