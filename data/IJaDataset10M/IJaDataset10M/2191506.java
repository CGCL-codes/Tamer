package co.edu.usbcali.adt.utils;

public class ArrayUtil {

    public static int getPosition(Object[] array, Object o) {
        int pos = 0;
        for (Object object : array) {
            if (object.equals(o)) return pos;
            pos++;
        }
        return -1;
    }
}
