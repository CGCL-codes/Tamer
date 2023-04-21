package quickfix.field;

import quickfix.StringField;

public class MailingDtls extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 474;

    public MailingDtls() {
        super(474);
    }

    public MailingDtls(String data) {
        super(474, data);
    }
}
