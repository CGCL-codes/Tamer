package quickfix.field;

import quickfix.IntField;

public class NoInstrumentParties extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 1018;

    public NoInstrumentParties() {
        super(1018);
    }

    public NoInstrumentParties(int data) {
        super(1018, data);
    }
}
