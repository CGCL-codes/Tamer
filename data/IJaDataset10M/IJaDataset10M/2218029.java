package quickfix.field;

import quickfix.StringField;

public class DeskID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 284;

    public DeskID() {
        super(284);
    }

    public DeskID(String data) {
        super(284, data);
    }
}
