package neembuu.util;

/**
 *
 * @author Shashank Tulsyan
 */
public final class Functions {

    public static long downloadDecay(long a0, long delta_t) {
        return (long) (a0 * Math.exp(-Math.pow(delta_t, 2)));
    }
}
