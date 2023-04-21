package quickfix.field;

import quickfix.DecimalField;

public class LegStrikePrice extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 612;

    public LegStrikePrice() {
        super(612);
    }

    public LegStrikePrice(java.math.BigDecimal data) {
        super(612, data);
    }

    public LegStrikePrice(double data) {
        super(612, new java.math.BigDecimal(data));
    }
}
