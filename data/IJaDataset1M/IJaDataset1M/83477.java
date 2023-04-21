package nz.ac.waikato.mcennis.rat.reusablecores.datavector;

/**

 * Class cretaing a data vector backed by a double array.  

 * @author Daniel McEnnis

 */
public class DoubleArrayDataVector implements DataVector {

    double[] source;

    int index = -1;

    int size = 0;

    /**

     * Create a data vector from the given double array.  The array is stored by 

     * reference, not value.

     * @param source source data array

     */
    public DoubleArrayDataVector(double[] source) {
        this.source = source;
        size = this.source.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setSize(int s) {
        size = s;
    }

    @Override
    public double getValue(Comparable index) {
        if (index.getClass().getName().contentEquals("java.lang.Integer")) {
            return source[((Integer) index).intValue()];
        } else {
            return Double.NaN;
        }
    }

    @Override
    public void reset() {
        index = -1;
    }

    @Override
    public double getCurrentValue() {
        return source[index];
    }

    @Override
    public Comparable getCurrentIndex() {
        return index;
    }

    @Override
    public void next() {
        index++;
    }

    @Override
    public boolean hasNext() {
        if (index < source.length - 1) {
            return true;
        } else {
            return false;
        }
    }

    public int compareTo(Object o) {
        if (this.getClass().getName().compareTo(o.getClass().getName()) == 0) {
            DoubleArrayDataVector right = (DoubleArrayDataVector) o;
            if (this.size - right.size != 0) {
                return this.size - right.size;
            }
            for (int i = 0; i < size; ++i) {
                if (source[i] - right.source[i] > 0) {
                    return -1;
                } else if (source[i] - right.source[i] < 0) {
                    return 1;
                }
            }
            return index - right.index;
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }
}
