package quickfix.field;

import quickfix.DecimalField;

public class StartCash extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 921;

    public StartCash() {
        super(921);
    }

    public StartCash(java.math.BigDecimal data) {
        super(921, data);
    }

    public StartCash(double data) {
        super(921, new java.math.BigDecimal(data));
    }
}
