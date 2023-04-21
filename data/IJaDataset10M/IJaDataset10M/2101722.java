package demandpa;

public class TestMethodRecursion {

    static Object foo(Object p1, Object p2, boolean b) {
        if (b) {
            return p1;
        } else {
            return bar(id(p2), p1, !b);
        }
    }

    static Object choose(Object o1, Object o2, boolean b) {
        return foo(o1, o2, b);
    }

    static Object bar(Object p1, Object p2, boolean b) {
        return choose(p1, p2, b);
    }

    static Object id(Object x) {
        return x;
    }

    public static void main(String[] args) {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = foo(o1, o2, false);
        DemandPATestUtil.testThisVar(o3);
    }
}
