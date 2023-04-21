package quickfix.field;

import quickfix.IntField;

public class NoQuoteSets extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 296;

    public NoQuoteSets() {
        super(296);
    }

    public NoQuoteSets(int data) {
        super(296, data);
    }
}
