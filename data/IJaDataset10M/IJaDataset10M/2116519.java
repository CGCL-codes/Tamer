package quickfix.field;

import quickfix.IntField;

public class NoAllocs extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 78;

    public NoAllocs() {
        super(78);
    }

    public NoAllocs(int data) {
        super(78, data);
    }
}
