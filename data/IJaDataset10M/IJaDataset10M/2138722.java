package quickfix.field;

import quickfix.StringField;

public class EmailThreadID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 164;

    public EmailThreadID() {
        super(164);
    }

    public EmailThreadID(String data) {
        super(164, data);
    }
}
