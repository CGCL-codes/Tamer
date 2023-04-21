package quickfix.field;

import quickfix.IntField;

public class TradeReportRejectReason extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 751;

    public static final int SUCCESSFUL = 0;

    public static final int INVALID_PARTY_INFORMATION = 1;

    public static final int UNKNOWN_INSTRUMENT = 2;

    public static final int UNAUTHORIZED_TO_REPORT_TRADES = 3;

    public static final int INVALID_TRADE_TYPE = 4;

    public static final int OTHER = 99;

    public TradeReportRejectReason() {
        super(751);
    }

    public TradeReportRejectReason(int data) {
        super(751, data);
    }
}
