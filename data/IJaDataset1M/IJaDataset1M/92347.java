package utils;

public final class Checker {

    private Checker() {
    }

    public static void checkStringNotEmpty(String s, String varName) {
        if (isBlank(s)) {
            throw new IllegalArgumentException("String " + varName + " is empty.");
        }
    }

    public static void checkObjectNotNull(Object o, String varName) {
        if (o == null) {
            throw new IllegalArgumentException("Object " + varName + " is null!");
        }
    }

    public static void checkGreaterOrEqual(long var, long bound, String varName) {
        if (var < bound) {
            throw new IllegalArgumentException(varName + " (" + var + ") is smaller than the specified bound (" + bound + ").");
        }
    }

    public static boolean isArrayEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isBlank(String s) {
        boolean retval = true;
        int l;
        if (s != null && (l = s.length()) > 0) {
            for (int i = 0; i < l && retval; i++) {
                retval = Character.isWhitespace(s.charAt(i));
            }
        }
        return retval;
    }
}
