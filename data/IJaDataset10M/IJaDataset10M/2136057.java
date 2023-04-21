package quickfix.field;

import quickfix.StringField;

public class TradingSessionID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 336;

    public TradingSessionID() {
        super(336);
    }

    public TradingSessionID(String data) {
        super(336, data);
    }
}
