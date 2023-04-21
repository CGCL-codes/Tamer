package net.sf.alc.cfpj.iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import net.sf.alc.cfpj.algorithms.Algorithms;
import net.sf.alc.cfpj.algorithms.Appender;
import net.sf.alc.cfpj.algorithms.Appenders;

/**
 * @author Alain
 *
 */
public class TestConcateningIterable extends TestCase {

    public void test1() {
        final Iterable<Integer> iter1 = Iterables.fromArray(1, 2);
        final Iterable<Integer> iter2 = Iterables.fromArray(3, 4);
        final List<Integer> result = new ArrayList<Integer>();
        final Appender<Integer> appender = Appenders.fromCollection(result);
        final Iterable<Integer> concat = Iterables.concat(iter1, iter2);
        Algorithms.addAll(concat.iterator(), appender);
        assertEquals(result, Arrays.asList(1, 2, 3, 4));
    }

    public void test2() {
        final List<Integer> iter1 = Arrays.asList(1, 2);
        final List<Integer> iter2 = Arrays.asList(3, 4);
        final List<Integer> result = new ArrayList<Integer>();
        for (int i : Iterables.concat(iter1, iter2)) {
            result.add(i);
        }
        assertEquals(result, Arrays.asList(1, 2, 3, 4));
    }
}
