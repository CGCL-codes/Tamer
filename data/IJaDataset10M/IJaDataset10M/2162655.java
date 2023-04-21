package quickfix.field;

import quickfix.IntField;

public class DiscretionRoundDirection extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 844;

    public static final int MORE_AGGRESSIVE = 1;

    public static final int MORE_PASSIVE = 2;

    public DiscretionRoundDirection() {
        super(844);
    }

    public DiscretionRoundDirection(int data) {
        super(844, data);
    }
}
