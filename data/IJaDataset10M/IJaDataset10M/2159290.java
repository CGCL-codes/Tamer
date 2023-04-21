package com.javampire.util.sorting;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1 $ $Date: 2007/05/08 17:48:27 $
 */
public class DoubleArraySwapper implements Swapper {

    private final double[] array;

    public DoubleArraySwapper(double[] array) {
        this.array = array;
    }

    public void swap(int index1, int index2) {
        final double t = array[index1];
        array[index1] = array[index2];
        array[index2] = t;
    }
}
