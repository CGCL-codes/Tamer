package es.optsicom.lib.rcl.test;

import java.util.Arrays;
import es.optsicom.lib.util.RandomManager;

/**
 * Opción recomendada cuando los valores están ordenados
 * @author mica
 *
 */
public class RCL2 {

    public static int selectElem(double[] sortedValues, double closeToBest) {
        int size = sortedValues.length;
        double maxValue = sortedValues[size - 1];
        double thresold = closeToBest * maxValue;
        int i = Arrays.binarySearch(sortedValues, thresold);
        if (i >= 0) {
            while (i > 0) {
                if (sortedValues[i] == sortedValues[i - 1]) {
                    i--;
                }
            }
        } else {
            i = -i - 1;
        }
        return RandomManager.getRandom().nextInt(size - i) + i;
    }
}
