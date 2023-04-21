package quickfix.field;

import quickfix.StringField;

public class ClOrdLinkID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 583;

    public ClOrdLinkID() {
        super(583);
    }

    public ClOrdLinkID(String data) {
        super(583, data);
    }
}
