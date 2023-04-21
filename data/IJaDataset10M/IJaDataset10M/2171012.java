package quickfix.field;

import quickfix.DecimalField;

public class DealingCapacity extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 1048;

    public DealingCapacity() {
        super(1048);
    }

    public DealingCapacity(java.math.BigDecimal data) {
        super(1048, data);
    }

    public DealingCapacity(double data) {
        super(1048, new java.math.BigDecimal(data));
    }
}
