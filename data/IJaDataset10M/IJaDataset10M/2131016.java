package quickfix.field;

import quickfix.DoubleField;

public class MidYield extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 633;

    public MidYield() {
        super(633);
    }

    public MidYield(double data) {
        super(633, data);
    }
}
