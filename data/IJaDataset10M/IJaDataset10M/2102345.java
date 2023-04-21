package quickfix.field;

import quickfix.IntField;

public class PriorityIndicator extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 638;

    public static final int PRIORITY_UNCHANGED = 0;

    public static final int LOST_PRIORITY_AS_RESULT_OF_ORDER_CHANGE = 1;

    public PriorityIndicator() {
        super(638);
    }

    public PriorityIndicator(int data) {
        super(638, data);
    }
}
