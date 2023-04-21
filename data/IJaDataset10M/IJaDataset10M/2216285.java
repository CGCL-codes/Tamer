package quickfix.field;

import quickfix.DecimalField;

public class MinQty extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 110;

    public MinQty() {
        super(110);
    }

    public MinQty(java.math.BigDecimal data) {
        super(110, data);
    }

    public MinQty(double data) {
        super(110, new java.math.BigDecimal(data));
    }
}
