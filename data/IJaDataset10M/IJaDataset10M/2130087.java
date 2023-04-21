package mipt.crec.lab.compmath.gui.math;

import mipt.gui.graph.field.ScalarFieldFunction;
import mipt.math.Number;
import mipt.math.array.Vector;

/**
 * Stores values of scalar field as Vector - one-dimensional Array of Numbers.
 * It is enough to set xCount because yCount = vector.size() / xCount;
 * Does not store xs and ys themselves (min and max should be set to Plot;
 *  and uniform grid is usually assumed by FieldRenderer).
@author Evdokimov
 */
public class ScalarFieldAsVector implements ScalarFieldFunction {

    private int xCount, yCount;

    private Vector vector;

    private boolean xThenY = false;

    /**
	 * 
	 */
    public ScalarFieldAsVector() {
    }

    /**
	 * @param vector
	 * @param xCount
	 */
    public ScalarFieldAsVector(Vector vector, int xCount) {
        setField(vector, xCount);
    }

    public final boolean isXThenY() {
        return xThenY;
    }

    /**
	 * Sets enumeration in a vector; default is false. 
	 * If true, first block conforms to various y's with fixed x
	 *  so value(x,y) = vector(x*yCount+y)
	 * @param XthenY
	 */
    public void setXThenY(boolean XthenY) {
        this.xThenY = XthenY;
    }

    /**
	 * @see mipt.gui.graph.field.ScalarFieldFunction#getValueAt(int, int)
	 */
    public double getValueAt(int xIndex, int yIndex) {
        return vector.get(xThenY ? xIndex * yCount + yIndex : yIndex * xCount + xIndex).doubleValue();
    }

    /**
	 * @see mipt.gui.graph.field.FieldFunction#getXCount()
	 */
    public final int getXCount() {
        return xCount;
    }

    /**
	 * @see mipt.gui.graph.field.FieldFunction#getYCount()
	 */
    public final int getYCount() {
        return yCount;
    }

    /**
	 * @see mipt.gui.graph.field.FieldFunction#getMinMaxXY()
	 */
    public double[] getMinMaxXY() {
        return null;
    }

    public final Vector getVector() {
        return vector;
    }

    protected void setXCount(int count) {
        xCount = count;
    }

    protected void setYCount(int count) {
        yCount = count;
    }

    protected void setVector(Vector vector) {
        this.vector = vector;
    }

    public void setField(Vector vector, int xCount) {
        int n = vector.size();
        if (n % xCount != 0) throw new IllegalArgumentException("Vector in ScalarFieldAsVector should contain xCount*yCount elements");
        this.vector = vector;
        this.xCount = xCount;
        this.yCount = n / xCount;
    }

    /**
	 * @see mipt.gui.graph.field.FieldFunction#removeAll()
	 */
    public void removeAll() {
        setXCount(0);
        setYCount(0);
        this.vector = null;
    }

    public String toString() {
        if (vector == null) return "null vector";
        StringBuffer buf = new StringBuffer(16 * vector.size());
        int m = getYCount() - 1, n = getXCount() - 1;
        buf.append("i:\t");
        for (int i = 0; i <= n; i++) {
            buf.append(i);
            if (i < n) buf.append('\t');
        }
        buf.append("\r\n");
        for (int j = 0; j <= m; j++) {
            buf.append(j);
            buf.append(":\t");
            for (int i = 0; i <= n; i++) {
                double value = getValueAt(i, j);
                buf.append(Number.toString(value));
                if (i < n) buf.append('\t');
            }
            if (j < m) buf.append("\r\n");
        }
        return buf.toString();
    }
}
