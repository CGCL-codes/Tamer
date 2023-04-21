package quickfix.field;

import quickfix.DoubleField;

public class Factor extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 228;

    public Factor() {
        super(228);
    }

    public Factor(double data) {
        super(228, data);
    }
}
