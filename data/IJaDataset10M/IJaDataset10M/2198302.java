package quickfix.field;

import quickfix.DecimalField;

public class LegLastForwardPoints extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 1073;

    public LegLastForwardPoints() {
        super(1073);
    }

    public LegLastForwardPoints(java.math.BigDecimal data) {
        super(1073, data);
    }

    public LegLastForwardPoints(double data) {
        super(1073, new java.math.BigDecimal(data));
    }
}
