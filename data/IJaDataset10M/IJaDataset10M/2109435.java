package quickfix.field;

import quickfix.StringField;

public class LastNetworkResponseID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 934;

    public LastNetworkResponseID() {
        super(934);
    }

    public LastNetworkResponseID(String data) {
        super(934, data);
    }
}
