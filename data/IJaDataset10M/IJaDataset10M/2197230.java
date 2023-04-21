package quickfix.field;

import quickfix.DoubleField;

public class RoundingModulus extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 469;

    public RoundingModulus() {
        super(469);
    }

    public RoundingModulus(double data) {
        super(469, data);
    }
}
