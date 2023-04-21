package jsysmon;

import java.util.Arrays;

/**
 * This class stores monitoring information for the whole system.
 * CPU Usages are provided as a double value between 0.0 and 1.0.
 * If the underlying system cannot provide some information, they are
 * set to 0
 * @author Francois-Xavier Detourniere
 *
 */
public class CPUMonitoringData {

    /**
	 * Index of User information in returned results
	 */
    public static final int USER_INDEX = 0;

    /**
	 * Index of Nice information in returned results
	 */
    public static final int NICE_INDEX = 1;

    /**
	 * Index of System information in returned results
	 */
    public static final int SYSTEM_INDEX = 2;

    /**
	 * Index of Idle information in returned results
	 */
    public static final int IDLE_INDEX = 3;

    /**
	 * Index of IRQ information in returned results
	 */
    public static final int IRQ_INDEX = 4;

    /**
	 * Index of Soft IRQ information in returned results
	 */
    public static final int SOFT_IRQ_INDEX = 5;

    /**
	 * Index of Steal information in returned results
	 */
    public static final int STEAL_INDEX = 6;

    /**
	 * Index of IOWait information in returned results
	 */
    public static final int IO_INDEX = 7;

    /**
	 * Index of Aggregated information in returned results
	 * The value at this index are compute on recalcTotal call
	 */
    public static final int TOTAL_INDEX = 8;

    /**
	 * Number of indexes available.
	 * Keep this value up to date.
	 */
    public static final int NB_INDEXES = 9;

    /**
	 * Labels corresponding to each index
	 */
    public static final String[] LABELS = new String[] { "User CPU Usage", "Nice CPU Usage", "System CPU Usage", "Idle CPU Usage", "IRQ CPU Usage", "Soft IRQ CPU Usage", "Steal CPU Usage", "IO Wait CPU Usage", "Total CPU Usage" };

    /**
	 * Number of CPUs installed
	 */
    private int nbCpus = 0;

    /**
	 * Total Times
	 */
    private long[] totalTimes = new long[NB_INDEXES];

    /**
	 * Times per CPU
	 */
    private long[][] timesPerCPU;

    /**
	 * Total CPU Usages
	 */
    private double[] totalUsages = new double[NB_INDEXES];

    /**
	 * Total CPU Usage per CPU
	 */
    private double[][] usagesPerCPU;

    /**
	 * Initializes SystemMonitoringData for given number of CPUs
	 * @param nbCpus
	 */
    public CPUMonitoringData(int nbCpus) {
        this.nbCpus = nbCpus;
        timesPerCPU = new long[nbCpus][NB_INDEXES];
        usagesPerCPU = new double[nbCpus][NB_INDEXES];
        Arrays.fill(totalTimes, 0);
        Arrays.fill(totalUsages, 0.0);
        for (int i = 0; i < nbCpus; ++i) {
            Arrays.fill(timesPerCPU[i], 0);
            Arrays.fill(usagesPerCPU[i], 0.0);
        }
    }

    /**
	 * Returns the Total time for the specified type
	 * @param index One of *_INDEX
	 * @return Corresponding Total time
	 */
    public long getTotalTime(int index) {
        return totalTimes[index];
    }

    /**
	 * Returns the CPU time for the specified type and CPU
	 * @param cpuIndex CPU index to consider
	 * @param index One of *_INDEX
	 * @return Corresponding time
	 */
    public long getTime(int cpuIndex, int index) {
        return timesPerCPU[cpuIndex][index];
    }

    /**
	 * Return the Total CPU Usage for the specified type
	 * @param index One of *_INDEX
	 * @return Corresponding usage
	 */
    public double getTotalUsage(int index) {
        return totalUsages[index];
    }

    /**
	 * Return the CPU Usage for the specified type and CPU
	 * @param cpuIndex CPU index to consider
	 * @param index One of *_INDEX
	 * @return Corresponding usage
	 */
    public double getUsage(int cpuIndex, int index) {
        return usagesPerCPU[cpuIndex][index];
    }

    /**
	 * Returns all the Total Times.
	 * The indexes of the information in the returned array
	 * are ones of *_INDEX
	 * @return Array containing the Total Times
	 */
    public long[] getTotalTimes() {
        return totalTimes;
    }

    /**
	 * Returns all the Times for the given CPU
	 * The indexes of the information in the returned array
	 * are ones of *_INDEX
	 * @param cpuIndex CPU Index to consider
	 * @return Array containing the times for the given CPU
	 */
    public long[] getTimes(int cpuIndex) {
        return timesPerCPU[cpuIndex];
    }

    /**
	 * Returns all the Total Usages
	 * The indexes of the information in the returned array
	 * are ones of *_INDEX
	 * @return Array containing Total Usages
	 */
    public double[] getTotalUsages() {
        return totalUsages;
    }

    /**
	 * Returns all the Usages for a given CPU
	 * The indexes of the information in the returned array
	 * are ones of *_INDEX
	 * @param cpuIndex CPU Index to consider
	 * @return Array containing Usages for given CPU
	 */
    public double[] getUsages(int cpuIndex) {
        return usagesPerCPU[cpuIndex];
    }

    /**
	 * Recalculates the CPU Usage during time spent since provided information
	 * @param previousInfo
	 */
    public void recalcUsages(CPUMonitoringData previousInfo) {
        recalcTotal();
        computeUsages(totalTimes, previousInfo.totalTimes, totalUsages);
        for (int i = 0; i < nbCpus; ++i) {
            computeUsages(timesPerCPU[i], previousInfo.timesPerCPU[i], usagesPerCPU[i]);
        }
    }

    /**
	 * Recalculate all values at TOTAL_INDEX
	 *
	 */
    protected void recalcTotal() {
        computeTotal(totalTimes);
        for (int i = 0; i < nbCpus; ++i) {
            computeTotal(timesPerCPU[i]);
        }
    }

    /**
	 * Recalculate value at TOTAL_INDEX for given Times
	 * @param times Times to consider
	 */
    private void computeTotal(long[] times) {
        times[TOTAL_INDEX] = 0;
        for (int i = 0; i < NB_INDEXES; ++i) {
            if (i != TOTAL_INDEX) {
                times[TOTAL_INDEX] += times[i];
            }
        }
    }

    /**
	 * Fills Usages from given Times and Previous Times
	 * @param times Last captures Times
	 * @param previousTimes Previously captured Times
	 * @param usages Usages to fill
	 */
    private void computeUsages(long[] times, long[] previousTimes, double[] usages) {
        long[] diffTimes = new long[NB_INDEXES];
        for (int i = 0; i < NB_INDEXES; ++i) {
            diffTimes[i] = times[i] - previousTimes[i];
        }
        for (int i = 0; i < NB_INDEXES; ++i) {
            usages[i] = (double) diffTimes[i] / (double) diffTimes[TOTAL_INDEX];
        }
        usages[TOTAL_INDEX] -= usages[IDLE_INDEX];
    }

    public String toString() {
        String toReturn = "Total Times : ";
        for (int i = 0; i < NB_INDEXES; ++i) {
            toReturn += i + ":" + totalTimes[i] + " - ";
        }
        toReturn += "\n";
        for (int cpuIndex = 0; cpuIndex < nbCpus; ++cpuIndex) {
            toReturn += "CPU [" + cpuIndex + "] Times :";
            for (int i = 0; i < NB_INDEXES; ++i) {
                toReturn += i + ":" + timesPerCPU[cpuIndex][i] + " - ";
            }
            toReturn += "\n";
        }
        toReturn += "Total Usages : ";
        for (int i = 0; i < NB_INDEXES; ++i) {
            toReturn += i + ":" + totalUsages[i] + " - ";
        }
        toReturn += "\n";
        for (int cpuIndex = 0; cpuIndex < nbCpus; ++cpuIndex) {
            toReturn += "CPU [" + cpuIndex + "] Usages :";
            for (int i = 0; i < NB_INDEXES; ++i) {
                toReturn += i + ":" + usagesPerCPU[cpuIndex][i] + " - ";
            }
            toReturn += "\n";
        }
        return toReturn;
    }
}
