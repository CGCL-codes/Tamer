package scikit.dataset;

import java.util.ArrayList;
import scikit.util.DoubleArray;

public class DatasetBuffer extends DataSet {

    protected double[] _x, _y, _errY;

    protected DatasetBuffer() {
    }

    public double x(int i) {
        return _x[i];
    }

    public double y(int i) {
        return _y[i];
    }

    public boolean hasErrorY() {
        return _errY != null;
    }

    public double errorY(int i) {
        return _errY[i];
    }

    public int size() {
        return _x.length;
    }

    public double[][] columns() {
        ArrayList<double[]> ret = new ArrayList<double[]>();
        ret.add(_x);
        ret.add(_y);
        if (_errY != null) ret.add(_errY);
        return ret.toArray(new double[0][]);
    }

    public DatasetBuffer copyData() {
        DatasetBuffer ret = new DatasetBuffer();
        ret._x = DoubleArray.clone(_x);
        ret._y = DoubleArray.clone(_y);
        ret._errY = DoubleArray.clone(_errY);
        return ret;
    }
}
