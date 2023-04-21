package starcraft.webapp.client.utils;

import java.util.Collection;

public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || ("".equals(s)) || ("null".equals(s));
    }

    public static <T> String join(String delimiter, UnaryFunction<String, T> formatter, Collection<T> array) {
        if (array.isEmpty()) {
            return "<empty>";
        }
        int i = 0;
        StringBuffer res = new StringBuffer();
        for (T item : array) {
            if (formatter != null) {
                res.append(formatter.eval(item));
            } else {
                res.append(item);
            }
            i++;
            if (i < array.size()) {
                res.append(delimiter);
            }
        }
        return res.toString();
    }
}
