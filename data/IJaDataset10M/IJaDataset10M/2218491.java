package quickfix.field;

import quickfix.StringField;

public class AsgnReqID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 831;

    public AsgnReqID() {
        super(831);
    }

    public AsgnReqID(String data) {
        super(831, data);
    }
}
