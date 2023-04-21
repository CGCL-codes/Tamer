package quickfix.field;

import quickfix.DecimalField;

public class HighPx extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 332;

    public HighPx() {
        super(332);
    }

    public HighPx(java.math.BigDecimal data) {
        super(332, data);
    }

    public HighPx(double data) {
        super(332, new java.math.BigDecimal(data));
    }
}
