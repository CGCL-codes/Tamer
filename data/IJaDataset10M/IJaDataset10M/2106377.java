package edu.rice.cs.cunit.util;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to create permutations.
 * Algorithm by Phillip Paul Fuchs.
 * @author Mathias Ricken
 */
public class Permutation {

    /**
     * Lambda to process each generated permutation.
     */
    public static interface IPermutationLambda<T> extends ILambda.Binary<Void, ArrayList<T>, ArrayList<Integer>> {
    }

    public static <T> void permutations(ArrayList<T> list, IPermutationLambda<T> lambda) {
        int N = list.size();
        ArrayList<Integer> a = new ArrayList<Integer>(N);
        int[] p = new int[N + 1];
        int i, j, tmp;
        for (i = 0; i < N; i++) {
            a.add(i);
            p[i] = i;
        }
        p[N] = N;
        lambda.apply(list, a);
        i = 1;
        while (i < N) {
            p[i]--;
            j = i % 2 * p[i];
            tmp = a.get(j);
            a.set(j, a.get(i));
            a.set(i, tmp);
            lambda.apply(list, a);
            i = 1;
            while (p[i] == 0) {
                p[i] = i;
                i++;
            }
        }
    }

    /**
     * Reindex one list in the original order based on a list of indices.
     */
    public static class ReIndexLambda<T> implements ILambda.Binary<ArrayList<T>, ArrayList<T>, ArrayList<Integer>> {

        /**
         * Apply the lambda.
         * @param list list in original order
         * @param indices list of indices
         *
         * @return list in order specified by list of indices
         */
        public ArrayList<T> apply(ArrayList<T> list, ArrayList<Integer> indices) {
            ArrayList<T> newList = new ArrayList<T>(list.size());
            for (int i : indices) {
                newList.add(list.get(i));
            }
            return newList;
        }
    }

    /**
     * Test case for permutation.
     */
    public static class PermutationTest extends TestCase {

        public static void testSimple() {
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(0);
            list.add(1);
            list.add(2);
            final ArrayList<ArrayList<Integer>> perms = new ArrayList<ArrayList<Integer>>();
            final ReIndexLambda<Integer> l = new ReIndexLambda<Integer>();
            Permutation.permutations(list, new IPermutationLambda<Integer>() {

                /**
                 * Apply the lambda.
                 * @param list list in original order
                 * @param indices lsit of indices
                 *
                 * @return lambda-specific return value
                 */
                public Void apply(ArrayList<Integer> list, ArrayList<Integer> indices) {
                    perms.add(l.apply(list, indices));
                    return null;
                }
            });
            ArrayList<ArrayList<Integer>> expected = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> p;
            p = new ArrayList<Integer>();
            p.add(0);
            p.add(1);
            p.add(2);
            expected.add(p);
            p = new ArrayList<Integer>();
            p.add(0);
            p.add(2);
            p.add(1);
            expected.add(p);
            p = new ArrayList<Integer>();
            p.add(1);
            p.add(0);
            p.add(2);
            expected.add(p);
            p = new ArrayList<Integer>();
            p.add(1);
            p.add(2);
            p.add(0);
            expected.add(p);
            p = new ArrayList<Integer>();
            p.add(2);
            p.add(0);
            p.add(1);
            expected.add(p);
            p = new ArrayList<Integer>();
            p.add(2);
            p.add(1);
            p.add(0);
            expected.add(p);
            assertEquals("incorrect count", expected.size(), perms.size());
            for (ArrayList<Integer> e : perms) {
                boolean found = false;
                for (ArrayList<Integer> a : expected) {
                    if (a.equals(e)) {
                        found = true;
                        break;
                    }
                }
                assertTrue(Arrays.toString(e.toArray()), found);
            }
        }
    }
}
