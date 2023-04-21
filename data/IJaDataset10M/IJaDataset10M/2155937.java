package jlib.misc;

import jlib.jmxMBean.BaseCloseMBean;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id$
 */
public abstract class BaseJmxGeneralStat extends BaseCloseMBean {

    protected static int ms_tnCounters[] = null;

    public static final int COUNTER_INDEX_NbAggressiveGC = 0;

    public static final int COUNTER_INDEX_NbNonFinalizedConnection = 1;

    public static final int COUNTER_INDEX_NbActiveConnection = 2;

    public static final int COUNTER_INDEX_NbWaitDuringConnectionCreate = 3;

    public static final int COUNTER_INDEX_NbRunThreadGC = 4;

    public BaseJmxGeneralStat(String csName, String csDescription) {
        super(csName, csDescription);
        ms_tnCounters = new int[5];
    }

    public static void incCounter(int nCounter) {
        if (ms_tnCounters == null) ms_tnCounters = new int[5];
        ms_tnCounters[nCounter]++;
    }

    public static void decCounter(int nCounter) {
        if (ms_tnCounters == null) ms_tnCounters = new int[5];
        ms_tnCounters[nCounter]--;
    }

    public static void addCounter(int nCounter, int nStep) {
        if (ms_tnCounters == null) ms_tnCounters = new int[5];
        ms_tnCounters[nCounter] += nStep;
    }
}
