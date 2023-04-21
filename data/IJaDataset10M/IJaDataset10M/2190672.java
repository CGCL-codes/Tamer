package hu.sztaki.lpds.wfs.utils;

/**
 *
 * @author krisztian
 */
public class Status {

    /** FINISH status code */
    public static final int FINISH = 6;

    /** ERROR status code */
    public static final int ERROR = 7;

    /** Job Not Runnable status code */
    public static final int NOTRUNNABLE = 99;

    /** INIT status code */
    public static final int INIT = 1;

    /** INPUT ERROR status code */
    public static final int NOINPUT = 21;

    /** Aborting status code */
    public static final int ABORTING = 22;

    /** Aborted status code */
    public static final int ABORTED = 28;

    /** Input condition is false status code */
    public static final int FALSEINPUT = 25;

    /** Running status code */
    public static final int RUNNING = 5;

    /** Submitted status code */
    public static final int SUBMITED = 2;

    /**
 * Megvizsgalja hogy a parameterben megadott status job veget jelzo status-e
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isEndStatus(int pStatus) {
        return pStatus == FINISH || pStatus == ERROR || pStatus == NOTRUNNABLE || pStatus == NOINPUT || pStatus == FALSEINPUT || pStatus == ABORTED || pStatus == FALSEINPUT;
    }

    /**
 * A megadott statusz olyan job vege statusz ami nem sikeres futas eredmenye?
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isEndStatusNotFinished(int pStatus) {
        return pStatus == ERROR || pStatus == NOTRUNNABLE || pStatus == NOINPUT || pStatus == FALSEINPUT || pStatus == ABORTED;
    }

    /**
 * Statusz vizsgalat sikeres futasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isFinished(int pStatus) {
        return pStatus == FINISH;
    }

    /**
 * Statusz vizsgalat Hibas futasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isError(int pStatus) {
        return pStatus == ERROR;
    }

    /**
 * Statusz vizsgalat Job nem tuttathato allapotra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isNotRunnable(int pStatus) {
        return pStatus == NOTRUNNABLE;
    }

    /**
 * Statusz vizsgalat megszakitasi allpotra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isAbort(int pStatus) {
        return pStatus == ABORTED || pStatus == ABORTING;
    }

    /**
 * Statusz vizsgalat megszakitasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isAborted(int pStatus) {
        return pStatus == ABORTED;
    }

    /**
 * Statusz vizsgalat folyamatban levo megszakitasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isAborting(int pStatus) {
        return pStatus == ABORTING;
    }

    /**
 * Statusz vizsgalat Initalasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isInit(int pStatus) {
        return pStatus == INIT;
    }

    /**
 * Statusz vizsgalat be nem fejezett futasra
 * @param pStatus vizsgalando status kod
 * @return logikai ertek
 */
    public static boolean isNotEndAndNotInit(int pStatus) {
        return !isEndStatus(pStatus) && pStatus != INIT;
    }
}
