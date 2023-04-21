package jflowmap.util;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Doubles;

/**
 * @author Ilya Boyandin
 */
public class ArrayUtils {

    private ArrayUtils() {
    }

    /**
   * NOTE: This method modifies the input array
   */
    public static int[] reverse(int[] a) {
        for (int left = 0, right = a.length - 1; left < right; left++, right--) {
            int _ = a[left];
            a[left] = a[right];
            a[right] = _;
        }
        return a;
    }

    /**
   * NOTE: This method modifies the input array
   */
    public static <T> T[] reverse(T[] a) {
        for (int left = 0, right = a.length - 1; left < right; left++, right--) {
            T _ = a[left];
            a[left] = a[right];
            a[right] = _;
        }
        return a;
    }

    public static double[] toArrayOfPrimitives(Iterable<Double> data) {
        if (data instanceof Collection) {
            return Doubles.toArray((Collection<Double>) data);
        } else {
            return Doubles.toArray(ImmutableList.copyOf(data));
        }
    }
}
