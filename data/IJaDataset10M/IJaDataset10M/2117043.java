package scikit.jobs.params;

public class IntValue extends StringValue {

    private int _lo = Integer.MIN_VALUE, _hi = Integer.MAX_VALUE;

    public IntValue(int x) {
        super(x);
    }

    public IntValue(int x, int lo, int hi) {
        super(x);
        _lo = lo;
        _hi = hi;
    }

    public int getInt() {
        return Integer.valueOf(getValue());
    }

    protected boolean testValidity(String v) {
        try {
            int i = Integer.valueOf(v);
            return _lo <= i && i <= _hi;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
