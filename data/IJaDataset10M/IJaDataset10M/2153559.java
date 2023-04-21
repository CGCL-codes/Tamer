package net.floogle.jTide;

/**
 *
 * @author chas
 */
public class Nullable {

    private boolean isNull;

    public Nullable() {
        isNull = true;
    }

    protected Nullable(boolean isNull) {
        this.isNull = isNull;
    }

    public boolean isNull() {
        return isNull;
    }

    public void makeNull() {
        isNull = true;
    }

    public void makeNull(boolean nullIt) {
        this.isNull = nullIt;
    }
}
