package utility;

/**
 *
 * @author michael
 */
public class StaticStopwatch {

    private static long start = 0;

    private static long stop;

    private static boolean running = true;

    public static void start() {
        running = true;
        start = System.currentTimeMillis();
    }

    public static void stop() {
        running = false;
        stop = System.currentTimeMillis();
    }

    public static long getTime() {
        if (running) {
            running = false;
            stop = System.currentTimeMillis();
            return (stop - start);
        } else if (start != 0) {
            return (stop - start);
        } else return 0;
    }
}
