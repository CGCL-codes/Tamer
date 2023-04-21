package sample.threadCheck.predicate;

import edu.rice.cs.cunit.threadCheck.PredicateLink;
import java.util.Arrays;

/**
 * Examples of annotations passing method arguments.
 */
public class TCSample12 {

    public static class CheckIdentity {

        public static boolean check(Object thisObject, Object[] args, boolean value) {
            if (args.length < 2) return false;
            return value ? (args[0] == args[1]) : (args[0] != args[1]);
        }
    }

    @PredicateLink(value = CheckIdentity.class, arguments = true)
    @interface TestIdentity {

        boolean value() default true;
    }

    public static void main(String[] args) {
        TCSample12 o = new TCSample12();
        System.out.println("main!");
        String s = "foo";
        String t = "bar";
        o.distinct(s, t);
        o.same(s, s);
        String[] ss = new String[] { "foo", "foo" };
        String[] tt = new String[] { "bar", "bar" };
        System.out.println("end main!");
        o.distinct2(ss, tt);
        o.same2(ss, ss);
        o.distinct3(s, t);
        o.same3(s, s);
    }

    @TestIdentity(value = false)
    public void distinct(Object s, Object t) {
        System.out.println("distinct! s = '" + s + "', t = '" + t + "'");
    }

    @TestIdentity
    public void same(Object s, Object t) {
        System.out.println("same! s = '" + s + "', t = '" + t + "'");
    }

    @TestIdentity(value = false)
    public void distinct2(String[] s, String[] t) {
        System.out.println("distinct2! s = '" + Arrays.toString(s) + "', t = '" + Arrays.toString(t) + "'");
    }

    @TestIdentity
    public void same2(String[] s, String[] t) {
        System.out.println("same2! s = '" + Arrays.toString(s) + "', t = '" + Arrays.toString(t) + "'");
    }

    @TestIdentity(value = false)
    public void distinct3(String s, String t) {
        System.out.println("distinct! s = '" + s + "', t = '" + t + "'");
    }

    @TestIdentity
    public void same3(String s, String t) {
        System.out.println("same! s = '" + s + "', t = '" + t + "'");
    }
}
