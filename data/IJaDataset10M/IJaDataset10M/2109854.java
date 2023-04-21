package quickfix.field;

import quickfix.UtcTimeStampField;
import java.util.Date;

public class TradSesCloseTime extends UtcTimeStampField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 344;

    public TradSesCloseTime() {
        super(344);
    }

    public TradSesCloseTime(Date data) {
        super(344, data, true);
    }
}
