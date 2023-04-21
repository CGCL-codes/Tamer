package opala.utils;

public final class Predicate {

    private Predicate() {
    }

    public static boolean isNullOfEquals(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        } else if (obj1 == null || obj2 == null) {
            return false;
        } else {
            return obj1.equals(obj2);
        }
    }

    public static boolean isNullOfEquals(Object[] obj1, Object[] obj2) {
        if (obj1 == obj2) {
            return true;
        } else if (obj1 == null || obj2 == null) {
            return false;
        } else if (obj1.length == obj2.length) {
            for (int i = 0; i < obj1.length; i++) {
                if (obj1[i].equals(obj2[i]) == false) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
