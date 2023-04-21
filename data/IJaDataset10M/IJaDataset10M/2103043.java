package quickfix.field;

import quickfix.BooleanField;

public class ReportToExch extends BooleanField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 113;

    public ReportToExch() {
        super(113);
    }

    public ReportToExch(boolean data) {
        super(113, data);
    }
}
