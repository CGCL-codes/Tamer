package edu.ucdavis.genomics.metabolomics.util.sort;

import java.util.Arrays;

/**
 * <p>
 * @author wohlgemuth
 * @version Jul 17, 2003
 * </p>
 *
 * <h4>File: Quicksort.java </h4>
 * <h4>Project: glibj </h4>
 * <h4>Package: edu.ucdavis.genomics.metabolomics.glibj.alg.sort </h4>
 * <h4>Type: Quicksort </h4>
 *
 * Quicksortimplementation des Interfaces Sortable aus dem selben packages
 *
 */
public class Quicksort implements Sortable {

    /**
     * @see edu.ucdavis.genomics.metabolomics.glibj.alg.sort.Sortable#sort(double[])
     */
    public double[] sort(double[] array) {
        double[] temp = array.clone();
        Arrays.sort(temp);
        return temp;
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.glibj.alg.sort.Sortable#sort(double[][], int)
     */
    public double[][] sort(double[][] array, int key) {
        return sort(array, key, 0, array.length - 1);
    }

    /**
     * die rekrusive methode zur Sortierung eines mehrdimensionalen arrays
     * @param array
     * @param key
     * @param down
     * @param top
     * @return
     */
    private double[][] sort(double[][] array, int key, int down, int top) {
        int i = down;
        int j = top;
        double x = array[(down + top) / 2][key];
        do {
            while (array[i][key] < x) {
                i++;
            }
            while (array[j][key] > x) {
                j--;
            }
            if (i <= j) {
                double[] temp = new double[array[i].length];
                for (int y = 0; y < array[i].length; y++) {
                    temp[y] = array[i][y];
                    array[i][y] = array[j][y];
                    array[j][y] = temp[y];
                }
                i++;
                j--;
            }
        } while (i <= j);
        if (down < j) {
            array = sort(array, key, down, j);
        }
        if (i < top) {
            array = sort(array, key, i, top);
        }
        return array;
    }
}
