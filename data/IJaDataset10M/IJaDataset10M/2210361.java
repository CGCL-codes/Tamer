package tefkat.plugin.stats;

public class Validator {

    public static void notNull(Object... os) {
        for (Object o : os) {
            if (o == null) throw new NullPointerException();
        }
    }
}
