package quickfix.field;

import quickfix.DoubleField;

public class StrikeValue extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 968;

    public StrikeValue() {
        super(968);
    }

    public StrikeValue(double data) {
        super(968, data);
    }
}
