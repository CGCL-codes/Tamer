package foo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleClass {

    interface Func<T> {

        T plusOne(T value);
    }

    public static void main(final String[] args) {
        final ExampleClass e = new ExampleClass();
        final List<Integer> plusOne = e.mapp(new Func<Integer>() {

            public Integer plusOne(final Integer value) {
                return value + 1;
            }
        }, Arrays.asList(1, 2, 3, 4));
        System.out.println(plusOne);
    }

    private List<Integer> mapp(final Func<Integer> func, final List<Integer> list) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final Integer v : list) {
            result.add(func.plusOne(v));
        }
        return result;
    }
}
