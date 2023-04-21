package quickfix.field;

import quickfix.StringField;

public class CardStartDate extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 503;

    public CardStartDate() {
        super(503);
    }

    public CardStartDate(String data) {
        super(503, data);
    }
}
