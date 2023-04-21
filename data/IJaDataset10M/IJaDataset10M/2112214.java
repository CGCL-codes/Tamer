package quickfix.field;

import quickfix.StringField;

public class LastMkt extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 30;

    public LastMkt() {
        super(30);
    }

    public LastMkt(String data) {
        super(30, data);
    }
}
