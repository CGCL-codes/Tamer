package photorganizer.common.helper;

public class StringHelper {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static String removePrefix(String value, String prefix) {
        if (isNullOrEmpty(value)) {
            return value;
        }
        return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
    }

    public static String removeSuffix(String value, String suffix) {
        if (isNullOrEmpty(value)) {
            return value;
        }
        int index = value.indexOf(suffix);
        return index < 0 ? value : value.substring(0, index);
    }

    private StringHelper() {
        assert false;
    }
}
