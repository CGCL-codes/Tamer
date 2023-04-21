package quickfix.field;

import quickfix.IntField;

public class SideQty extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 1009;

    public SideQty() {
        super(1009);
    }

    public SideQty(int data) {
        super(1009, data);
    }
}
