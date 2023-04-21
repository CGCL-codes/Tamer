package quickfix.field;

import quickfix.UtcTimeStampField;
import java.util.Date;

public class TransactTime extends UtcTimeStampField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 60;

    public TransactTime() {
        super(60);
    }

    public TransactTime(Date data) {
        super(60, data, true);
    }
}
