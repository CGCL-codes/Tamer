package net.sf.alc.cfpj.iterables;

import junit.framework.TestCase;
import net.sf.alc.cfpj.algorithms.Functor;
import java.util.ArrayList;
import java.util.List;

public class TestTransformIterable extends TestCase {

    private static class DoubleUp implements Functor<Integer, Integer> {

        @Override
        public Integer eval(Integer i) {
            return 2 * i;
        }
    }

    public void test1() {
        final List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        int i = 0;
        for (int value : Iterables.transform(list, new DoubleUp())) {
            assertEquals(i, value);
            i += 2;
        }
    }
}
