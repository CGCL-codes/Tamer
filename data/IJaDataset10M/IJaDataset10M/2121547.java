package quickfix.field;

import quickfix.IntField;

public class OrderHandlingInstSource extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 1032;

    public static final int NASD_OATS = 1;

    public OrderHandlingInstSource() {
        super(1032);
    }

    public OrderHandlingInstSource(int data) {
        super(1032, data);
    }
}
