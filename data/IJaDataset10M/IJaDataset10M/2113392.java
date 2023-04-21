package quickfix.field;

import quickfix.StringField;

public class ClientBidID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 391;

    public ClientBidID() {
        super(391);
    }

    public ClientBidID(String data) {
        super(391, data);
    }
}
