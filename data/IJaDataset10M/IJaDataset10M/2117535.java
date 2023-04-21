package name.huzhenbo.java.klass;

import org.junit.Test;
import java.util.Comparator;

/**
 * Effective Java, Item 22, favor static member class over non-static
 * <p/>
 * A nested class is a class defined within another class. A nested class should exist
 * only to serve its enclosing class. If a nested class would be useful in some other
 * context, then it should be a top-level class. There are four kinds of nested classes:
 * static member classes, nonstatic member classes, anonymous classes, and local
 * classes. All but the first kind are known as inner classes. This item tells you when
 * to use which kind of nested class and why.
 * <p/>
 * To recap, there are four different kinds of nested classes, and each has its
 * place. If a nested class needs to be visible outside of a single method or is too long
 * to fit comfortably inside a method, use a member class. If each instance of the
 * member class needs a reference to its enclosing instance, make it nonstatic; other-
 * wise, make it static. Assuming the class belongs inside a method, if you need to
 * create instances from only one location and there is a preexisting type that charac-
 * terizes the class, make it an anonymous class; otherwise, make it a local class.
 */
public class NestedClassTest {

    /**
     * Anonymous class.
     * <p/>
     * Actually, anonymous class can be static or non-static. Almost same as static nested class and non-static member class,
     * except it's anonymous.
     */
    private static final Comparator lengthComparator = new Comparator<String>() {

        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    };

    /**
     * Local method.
     */
    @Test
    public void test_local_class() {
        Comparator anotherComparator = new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };
    }

    /**
     * A static member class is the simplest kind of nested class. It is best thought of
     * as an ordinary class that happens to be declared inside another class and has
     * access to all of the enclosing class�s members, even those declared private. A
     * static member class is a static member of its enclosing class and obeys the same
     * accessibility rules as other static members. If it is declared private, it is accessible
     * only within the enclosing class, and so forth.
     * One common use of a static member class is as a public helper class, useful
     * only in conjunction with its outer class. For example, consider an enum describing
     * the operations supported by a calculator (Item 30). The Operation enum should
     * be a public static member class of the Calculator class. Clients of Calculator
     * could then refer to operations using names like Calculator.Operation.PLUS and
     * Calculator.Operation.MINUS.
     * <p/>
     * If you declare a member class that does not require access to an enclosing
     * instance, always put the static modifier in its declaration, making it a static
     * rather than a nonstatic member class. If you omit this modifier, each instance will
     * have an extraneous reference to its enclosing instance.
     */
    private static class Operator {
    }

    /**
     * Each instance of a nonstatic member class is implicitly associated with an
     * enclosing instance of its containing class.
     * <p/>
     * Within instance methods of a nonstatic
     * member class, you can invoke methods on the enclosing instance or obtain a refer-
     * ence to the enclosing instance using the qualifiedthis construct [JLS, 15.8.4]. If an
     * instance of a nested class can exist in isolation from an instance of its enclosing
     * class, then the nested class must be a static member class: it is impossible to create
     * an instance of a nonstatic member class without an enclosing instance.
     */
    private class Adapter {

        public void test() {
            System.out.println(NestedClassTest.this.getClass());
        }
    }
}
