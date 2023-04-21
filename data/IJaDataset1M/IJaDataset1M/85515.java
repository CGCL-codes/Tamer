package quickfix.field;

import quickfix.DecimalField;

public class MinOfferSize extends DecimalField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 648;

    public MinOfferSize() {
        super(648);
    }

    public MinOfferSize(java.math.BigDecimal data) {
        super(648, data);
    }

    public MinOfferSize(double data) {
        super(648, new java.math.BigDecimal(data));
    }
}
