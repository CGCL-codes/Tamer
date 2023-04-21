package quickfix.field;

import quickfix.DecimalField;

public class LastPx extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 31;

    public LastPx() {
        super(31);
    }

    public LastPx(java.math.BigDecimal data) {
        super(31, data);
    }

    public LastPx(double data) {
        super(31, new java.math.BigDecimal(data));
    }
}
