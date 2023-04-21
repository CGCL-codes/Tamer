package quickfix.field;

import quickfix.IntField;

public class NoStrikes extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 428;

    public NoStrikes() {
        super(428);
    }

    public NoStrikes(int data) {
        super(428, data);
    }
}
