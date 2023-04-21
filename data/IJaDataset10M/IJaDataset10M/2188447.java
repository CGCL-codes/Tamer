package quickfix.field;

import quickfix.StringField;

public class TradeDate extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 75;

    public TradeDate() {
        super(75);
    }

    public TradeDate(String data) {
        super(75, data);
    }
}
