package quickfix.field;

import quickfix.IntField;

public class SettlPartyRole extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 784;

    public SettlPartyRole() {
        super(784);
    }

    public SettlPartyRole(int data) {
        super(784, data);
    }
}
