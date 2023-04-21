package quickfix.field;

import quickfix.DecimalField;

public class PegDifference extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 211;

    public PegDifference() {
        super(211);
    }

    public PegDifference(java.math.BigDecimal data) {
        super(211, data);
    }

    public PegDifference(double data) {
        super(211, new java.math.BigDecimal(data));
    }
}
