package org.t2framework.vili.util;

import java.lang.reflect.Array;

public class ArrayUtils {

    private ArrayUtils() {
    }

    public static <T> T[] add(T[] array, T element) {
        @SuppressWarnings("unchecked") T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }
}
