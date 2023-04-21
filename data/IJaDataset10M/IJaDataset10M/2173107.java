package net.maizegenetics.stats.GLM;

/**
 * Created by IntelliJ IDEA.
 * User: pjb39
 * Date: Mar 27, 2004
 * Time: 5:26:08 PM
 * A data object holds a double value, a key, and a use indicator
 */
public class DataObject {

    private double value;

    private boolean use;

    private String key;

    public DataObject(double dblValue, String key) {
        value = dblValue;
        this.key = key;
        use = true;
    }

    public String toString() {
        return Double.toString(value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DataObject)) return false;
        DataObject dobj = (DataObject) obj;
        return ((key.equals(dobj.key)) && (value == dobj.value) && (use == dobj.use));
    }

    public double getdouble() {
        return value;
    }

    public void setUseable(boolean useThis) {
        use = useThis;
    }

    public boolean isUseable() {
        return use;
    }

    public String getKey() {
        return key;
    }
}
