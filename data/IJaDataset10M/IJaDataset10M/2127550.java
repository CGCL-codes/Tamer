package DEOS;

/**
 * DOCUMENT ME!
 */
class StartOfPeriodEvent {

    static StartOfPeriodEvent[] periodicEvents;

    int itsPeriodId;

    int itsPassCount;

    int countDown;

    int itsPeriodIndex;

    threadList itsWaitingThreads;

    StartOfPeriodEvent itsSuccessor;

    private StartOfPeriodEvent(int thePeriodIndex, int thePassCount) {
        itsPassCount = thePassCount;
        itsPeriodIndex = thePeriodIndex;
        itsWaitingThreads = new threadList();
        countDown = 1;
        itsPeriodId = 0;
        itsSuccessor = null;
    }

    public int currentPeriod() {
        return itsPeriodId;
    }

    public static StartOfPeriodEvent eventForPeriodIndex(int i) {
        return periodicEvents[i];
    }

    public static void initialize() {
        int numPeriods = Registry.numPeriods;
        periodicEvents = new StartOfPeriodEvent[numPeriods];
        int ticksInLastPeriod = 1;
        for (int i = 0; i < numPeriods; i++) {
            int ticksInThisPeriod = Registry.periodDurationInSystemTicks(i);
            periodicEvents[i] = new StartOfPeriodEvent(i, ticksInThisPeriod / ticksInLastPeriod);
            if (i > 0) {
                periodicEvents[i - 1].itsSuccessor = periodicEvents[i];
            }
            ticksInLastPeriod = ticksInThisPeriod;
        }
    }

    public void makeThreadWait(Thread theThread) {
        itsWaitingThreads.addAtEnd(theThread.startOfPeriodWaitNode);
    }

    public void pulseEvent(int systemTickCount) {
        countDown = countDown - 1;
        if (countDown == 0) {
            itsPeriodId = (itsPeriodId + 1) % 2;
            countDown = itsPassCount;
            Scheduler.runnableList().mergeList(itsWaitingThreads);
            if (itsSuccessor != null) {
                itsSuccessor.pulseEvent(systemTickCount);
            }
        }
    }
}
