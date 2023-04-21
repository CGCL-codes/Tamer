package quickfix.field;

import quickfix.StringField;

public class IssueDate extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 225;

    public IssueDate() {
        super(225);
    }

    public IssueDate(String data) {
        super(225, data);
    }
}
