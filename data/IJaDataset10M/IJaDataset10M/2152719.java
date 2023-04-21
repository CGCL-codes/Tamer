package quickfix.field;

import quickfix.DecimalField;

public class LastShares extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 32;

    public LastShares() {
        super(32);
    }

    public LastShares(java.math.BigDecimal data) {
        super(32, data);
    }

    public LastShares(double data) {
        super(32, new java.math.BigDecimal(data));
    }
}
