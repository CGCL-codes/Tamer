package quickfix.field;

import quickfix.StringField;

public class LegIssueDate extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 249;

    public LegIssueDate() {
        super(249);
    }

    public LegIssueDate(String data) {
        super(249, data);
    }
}
