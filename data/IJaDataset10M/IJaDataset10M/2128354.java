package quickfix.field;

import quickfix.IntField;

public class NoPosAmt extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 753;

    public NoPosAmt() {
        super(753);
    }

    public NoPosAmt(int data) {
        super(753, data);
    }
}
