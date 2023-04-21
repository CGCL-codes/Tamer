package org.databene.commons.collection;

/**
 * Represents a range of int values from a <code>min</code> to a <code>max</code> value 
 * (including min and max).<br/><br/>
 * Created: 05.10.2010 19:33:23
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class IntRange {

    protected int min;

    protected int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean contains(int i) {
        return (min <= i && i <= max);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + max;
        result = prime * result + min;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IntRange that = (IntRange) obj;
        return (max == that.max && min == that.min);
    }

    @Override
    public String toString() {
        return (min != max ? min + "..." + max : String.valueOf(min));
    }
}
