package quickfix.field;

import quickfix.IntField;

public class PegRoundDirection extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 838;

    public static final int MORE_AGGRESSIVE = 1;

    public static final int MORE_PASSIVE = 2;

    public PegRoundDirection() {
        super(838);
    }

    public PegRoundDirection(int data) {
        super(838, data);
    }
}
