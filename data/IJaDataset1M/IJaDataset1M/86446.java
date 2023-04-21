package jcckit.data;

import jcckit.util.Point;

/**
 *  Immutable two-dimensional point in data coordinates.
 *
 *  @author Franz-Josef Elmer
 */
public class DataPoint extends Point implements DataElement {

    public DataPoint(double x, double y) {
        super(x, y);
    }

    /** Returns always <tt>null</tt>. */
    public DataContainer getContainer() {
        return null;
    }

    /** Does nothing. */
    public void setContainer(DataContainer container) {
    }
}
