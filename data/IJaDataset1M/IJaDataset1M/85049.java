package Watermill.kernel;

import Watermill.util.TimeMeasure;

public class Chrono {

    private static long startTime;

    private static long time[] = { (long) 0.0, (long) 0.0, (long) 0.0 };

    private static int mode;

    public static final int CPU_MODE = 0;

    public static final int QUERY_MODE = 1;

    public static final int STOP_MODE = 2;

    public static void reset() {
        resetTime();
        time[0] = 0;
        time[1] = 0;
        time[2] = 0;
        mode = CPU_MODE;
    }

    private static void resetTime() {
        startTime = System.currentTimeMillis();
    }

    private static void accumulate(int desMode) {
        time[desMode] += System.currentTimeMillis() - startTime;
        resetTime();
    }

    public static void setMode(int newMode) {
        accumulate(mode);
        mode = newMode;
    }

    public static void setCpuMode() {
        setMode(CPU_MODE);
    }

    public static void setQueryMode() {
        setMode(QUERY_MODE);
    }

    public static void stop() {
        setMode(STOP_MODE);
    }

    public static TimeMeasure stats() {
        stop();
        return new TimeMeasure(time[0], time[1]);
    }
}
