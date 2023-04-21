package es.gavab.jmh.util;

import java.util.Arrays;
import es.gavab.jmh.util.ArraysUtil;

public class MoveRandomPositionsTest {

    public static void main(String[] args) {
        int size = 10;
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        System.out.println(Arrays.toString(array));
        array = ArraysUtil.moveRandomPositions(array);
        System.out.println(Arrays.toString(array));
    }
}
