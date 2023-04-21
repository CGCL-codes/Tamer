package metric.core.util;

public class DateUtils {

    public static int toDays(long baseline, long time) {
        long diff = time - baseline;
        return toDays(diff);
    }

    private static int toDays(long time) {
        return (int) (time / (1000 * 60 * 60 * 24));
    }
}
