package starcraft.gamemodel.utils;

public class ObjectUtils {

    /**
	 * returns true if <code>(obj1 == obj2)</code> or
	 * <code>(obj1 != null && obj1.equals(obj2))</code>.
	 */
    public static boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 != null && obj1.equals(obj2)) {
            return true;
        }
        return false;
    }

    /**
	 * this requires the variables of both objects to be of the same type
	 */
    public static <T> boolean isTypeSafeEqual(Class<T> type, T obj1, T obj2) {
        return isEqual(obj1, obj2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Comparable> int compare(T i1, T i2) {
        if (i1 == i2) {
            return 0;
        }
        if (i1 == null) {
            return -1;
        }
        if (i2 == null) {
            return 1;
        }
        return i2.compareTo(i1);
    }
}
