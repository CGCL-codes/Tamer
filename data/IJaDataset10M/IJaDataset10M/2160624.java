package claw.coloureyes;

import android.graphics.Color;

public final class ColourUtils {

    public static double ColourDistance(int first, int second) {
        long rmean = ((long) Color.red(first) + (long) Color.red(second)) / 2;
        long rd = (long) Color.red(first) - (long) Color.red(second);
        long gd = (long) Color.green(first) - (long) Color.green(second);
        long bd = (long) Color.blue(first) - (long) Color.blue(second);
        double distance = Math.sqrt((((512 + rmean) * rd * rd) >> 8) + 4 * gd * gd + (((767 - rmean) * bd * bd) >> 8));
        return distance;
    }
}
