package auo.cms.hsv.chroma;

import java.math.BigInteger;
import java.math.BigDecimal;
import flanagan.math.Conv;
import java.util.ArrayList;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ArrayMaths {

    protected ArrayList<Object> array = null;

    protected int length = 0;

    protected int maxIndex = -1;

    protected int minIndex = -1;

    protected ArrayList<Object> minmax = new ArrayList<Object>(2);

    public Object[] getArray_as_Object() {
        Object[] arrayo = new Object[this.length];
        for (int i = 0; i < this.length; i++) {
            arrayo[i] = this.array.get(i);
        }
        return arrayo;
    }

    public int getMaximum_as_int() {
        int max = 0;
        max = ((Integer) this.minmax.get(0)).intValue();
        Conv.restoreMessages();
        return max;
    }

    public double[] getArray_as_double() {
        double[] retArray = new double[this.length];
        for (int i = 0; i < this.length; i++) {
            retArray[i] = Conv.convert_Integer_to_double((Integer) this.array.get(i));
        }
        Conv.restoreMessages();
        return retArray;
    }

    protected static void findMinMax(Object[] arrayo, ArrayList<Object> minmaxx, int[] maxminIndices) {
        int maxIndexx = 0;
        int minIndexx = 0;
        int arraylength = arrayo.length;
        int[] arrayI = new int[arraylength];
        for (int i = 0; i < arraylength; i++) {
            arrayI[i] = ((Integer) arrayo[i]).intValue();
        }
        int amaxI = arrayI[0];
        int aminI = arrayI[0];
        maxIndexx = 0;
        minIndexx = 0;
        for (int i = 1; i < arraylength; i++) {
            if (arrayI[i] > amaxI) {
                amaxI = arrayI[i];
                maxIndexx = i;
            }
            if (arrayI[i] < aminI) {
                aminI = arrayI[i];
                minIndexx = i;
            }
        }
        minmaxx.add(new Integer(amaxI));
        minmaxx.add(new Integer(aminI));
        maxminIndices[0] = maxIndexx;
        maxminIndices[1] = minIndexx;
    }

    protected void minmax() {
        int[] maxminIndices = new int[2];
        ArrayMaths.findMinMax(this.getArray_as_Object(), this.minmax, maxminIndices);
        this.maxIndex = maxminIndices[0];
        this.minIndex = maxminIndices[1];
    }

    public ArrayMaths(int[] array) {
        this.length = array.length;
        this.array = new ArrayList<Object>(this.length);
        for (int i = 0; i < this.length; i++) {
            this.array.add(new Integer(array[i]));
        }
        this.minmax();
    }
}
