package syntelos.lang;

/**
 * 
 * @author jdp
 * @since 1.5
 */
public final class Time extends java.lang.Object {

    /**
     * @return Seconds with fix format (*# "." 3#).
     */
    public static final java.lang.String ToString(long millis) {
        long seconds = (millis / 1000L);
        java.lang.String string = java.lang.String.valueOf(seconds);
        int idx = string.indexOf('.');
        if (0 < idx) {
            idx += 3;
            if (idx < string.length()) return string.substring(0, idx + 1);
        }
        return string;
    }
}
