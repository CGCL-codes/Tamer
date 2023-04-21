package quickfix.field;

import quickfix.StringField;

public class RegistDtls extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 509;

    public RegistDtls() {
        super(509);
    }

    public RegistDtls(String data) {
        super(509, data);
    }
}
