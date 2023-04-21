package quickfix.field;

import quickfix.IntField;

public class EncodedListExecInstLen extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 352;

    public EncodedListExecInstLen() {
        super(352);
    }

    public EncodedListExecInstLen(int data) {
        super(352, data);
    }
}
