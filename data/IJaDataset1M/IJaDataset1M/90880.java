package quickfix.field;

import quickfix.IntField;

public class LegSwapType extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 690;

    public static final int PAR_FOR_PAR = 1;

    public static final int MODIFIED_DURATION = 2;

    public static final int RISK = 4;

    public static final int PROCEEDS = 5;

    public LegSwapType() {
        super(690);
    }

    public LegSwapType(int data) {
        super(690, data);
    }
}
