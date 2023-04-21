package ideas;

import ideas.Decorator.layout;
import static org.iqual.chaplin.DynaCastUtils.$;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zbynek Slajchrt
 * @since May 23, 2009 10:59:01 AM
 */
public abstract class Functions {

    @Function
    public static class modN {

        int n;

        int x;

        boolean _() {
            return x % n == 0;
        }
    }

    @Function
    public abstract static class filter {

        List<Integer> xs;

        abstract boolean p(int i);

        List<Integer> _() {
            if (xs.isEmpty()) {
                return xs;
            } else if (p(xs.get(0))) {
                return concat(xs.get(0), $((filter) (xs.subList(1, xs.size())), this));
            } else {
                return $((filter) (xs.subList(1, xs.size())), this);
            }
        }
    }

    public void a() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> filtered = $((filter) $(nums, (modN) $(2)));
        System.out.println(filtered);
        filtered = $((filter) $(nums, (modN) $(3)));
        System.out.println(filtered);
    }

    private static <T, S> List<T> concat(T t, S l) {
        return null;
    }

    public interface I {

        <A> String layout(A x);
    }

    public static class FunTest {

        @Function
        public abstract static class apply {

            abstract String f(int i);

            int v;

            String _() {
                return f(v);
            }
        }

        Decorator decorator = $("[", "]");

        {
            I i = $(decorator);
            System.out.println(i.layout("aaa"));
            String result = $((layout) $(decorator, 7));
            System.out.println(result);
            result = $((apply) $((layout) $(decorator), 7));
            System.out.println(result);
            layout l = (layout) $(decorator, 7);
            result = $((apply) $(l, 7));
            System.out.println(result);
        }
    }

    public static void annonymousFunctions() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> filtered = $((filter) $(nums, new IFunction() {

            int n = 2;

            int x;

            boolean _() {
                return x % n == 0;
            }
        }));
        System.out.println(filtered);
    }

    private String getX(int i) {
        return i == 0 ? null : "" + i;
    }

    @NullObjects
    public void nullObjectDerefence() {
        int l = getX(9).length();
        System.out.println(l);
    }
}
