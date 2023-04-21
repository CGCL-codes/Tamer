package quickfix.field;

import quickfix.StringField;

public class UnderlyingIssueDate extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 242;

    public UnderlyingIssueDate() {
        super(242);
    }

    public UnderlyingIssueDate(String data) {
        super(242, data);
    }
}
