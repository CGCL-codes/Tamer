package net.sf.rcpforms.widget.generator;

import java.lang.reflect.Array;

public class GenerateUtils {

    public static <T> T[] concatenate(Class<T> type, T[]... arrays) {
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null) {
                totalLength += arrays[i].length;
            }
        }
        T[] result = (T[]) Array.newInstance(type, totalLength);
        int offset = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null) {
                System.arraycopy(arrays[i], 0, result, offset, arrays[i].length);
                offset += arrays[i].length;
            }
        }
        return result;
    }
}
