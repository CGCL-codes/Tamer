package quickfix.field;

import quickfix.BooleanField;

public class ExchangeForPhysical extends BooleanField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 411;

    public ExchangeForPhysical() {
        super(411);
    }

    public ExchangeForPhysical(boolean data) {
        super(411, data);
    }
}
