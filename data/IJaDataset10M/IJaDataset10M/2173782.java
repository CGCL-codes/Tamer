package slice;

public class TestId {

    static Object id(Object x) {
        return x;
    }

    static void doNothing(Object o) {
    }

    /**
   * check for context-sensitive handling of the identity function.
   * o2 should be excluded
   * 
   * @param args
   */
    public static void main(String[] args) {
        Object o1 = new Object(), o2 = new Object();
        Object o3 = id(o1);
        id(o2);
        doNothing(o3);
    }
}
