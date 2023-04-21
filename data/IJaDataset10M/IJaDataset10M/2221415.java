package quickfix.field;

import quickfix.IntField;

public class NoPartyIDs extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 453;

    public NoPartyIDs() {
        super(453);
    }

    public NoPartyIDs(int data) {
        super(453, data);
    }
}
