package quickfix.field;

import quickfix.DecimalField;

public class LastForwardPoints extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 195;

    public LastForwardPoints() {
        super(195);
    }

    public LastForwardPoints(java.math.BigDecimal data) {
        super(195, data);
    }

    public LastForwardPoints(double data) {
        super(195, new java.math.BigDecimal(data));
    }
}
