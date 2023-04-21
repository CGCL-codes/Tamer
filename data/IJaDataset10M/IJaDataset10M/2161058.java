package com.ibm.JikesRVM;

/**
 * HPM configuration information.
 *
 * @author Peter F. Sweeney 
 */
public final class HPM_info {

    public static final int MAX_COUNTERS = 8;

    public static final int MAX_VALUES = MAX_COUNTERS + 1;

    public int version_number = 1;

    public int numberOfCounters = 0;

    public int maxNumberOfEventsPerCounter = 0;

    public String processorName = "";

    public String filenamePrefix = "HPM";

    public String headerFilename() {
        String filename = filenamePrefix;
        int index = filenamePrefix.lastIndexOf('/');
        if (index != -1) {
            filename = filenamePrefix.substring(index);
        }
        return filename + ".headerFile";
    }

    public int mode = 12;

    public String[] short_names;

    public int[] ids;

    public int[] status;

    public boolean[] thresholdable;

    public HPM_info() {
        short_names = new String[MAX_VALUES];
        ids = new int[MAX_VALUES];
        status = new int[MAX_VALUES];
        thresholdable = new boolean[MAX_VALUES];
        for (int i = 0; i < MAX_VALUES; i++) {
            short_names[i] = "";
            ids[i] = 0;
            status[i] = -1;
            thresholdable[i] = false;
        }
        short_names[0] = "REAL_TIME     ";
    }

    public void dump() {
        System.out.println(processorName + " has " + numberOfCounters + " counters with " + mode + " mode");
        dump_info();
    }

    public void dump_short_names() {
        for (int i = 0; i <= numberOfCounters; i++) {
            System.out.println(short_names[i] + " ");
        }
    }

    public String short_name(int i) {
        if (i > numberOfCounters) {
            System.err.println("***HPM_info.short_name(" + i + ") " + i + " > number of counters " + numberOfCounters + "!***");
            System.exit(-1);
        }
        return short_names[i];
    }

    public void dump_info() {
        for (int i = 0; i <= numberOfCounters; i++) {
            System.out.println(short_names[i] + "," + ids[i] + "," + status[i] + "," + thresholdable[i] + ";");
        }
    }
}
