package jasel.timer;

import org.lwjgl.Sys;

public class LWTimer extends Timer {

    private static boolean dead;

    private static double factor;

    static {
        double res = Sys.getTimerResolution();
        if (res == 0.0) {
            dead = true;
        } else {
            factor = 1000 / res;
        }
    }

    public static boolean isDead() {
        return dead;
    }

    public long getTicks() {
        double ticks = Sys.getTime();
        return (long) (ticks * factor);
    }
}
