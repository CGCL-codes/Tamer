package mil.army.usace.ehlschlaeger.rgik.util;

import mil.army.usace.ehlschlaeger.rgik.core.RGIS;

/**
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 * 
 * @author Chuck Ehlschlaeger
 */
public class BubbleSort extends RGIS {

    private boolean desendingValues;

    /** in alpha testing */
    public BubbleSort(boolean desending) {
        super();
        desendingValues = desending;
    }

    /** order[] must have the length of values[]. When sort() is finished,
	 *  order[] will contain the indices of values[] in the order of increasing
	 *  or descending values (based on descending variable in constructor).
	 */
    public void sort(int[] order, double[] values) {
        int temp = 0;
        boolean done = false;
        for (int i = 0; i < values.length; i++) {
            order[i] = i;
        }
        if (desendingValues) {
            while (!done) {
                done = true;
                for (int i = values.length - 2; i >= 0; i--) {
                    if (values[order[i]] < values[order[i + 1]]) {
                        done = false;
                        temp = order[i];
                        order[i] = order[i + 1];
                        order[i + 1] = temp;
                    }
                }
            }
        } else {
            while (!done) {
                done = true;
                for (int i = values.length - 2; i >= 0; i--) {
                    if (values[order[i]] > values[order[i + 1]]) {
                        done = false;
                        temp = order[i];
                        order[i] = order[i + 1];
                        order[i + 1] = temp;
                    }
                }
            }
        }
    }
}
