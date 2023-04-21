package quickfix.field;

import quickfix.StringField;

public class AsgnRptID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 833;

    public AsgnRptID() {
        super(833);
    }

    public AsgnRptID(String data) {
        super(833, data);
    }
}
