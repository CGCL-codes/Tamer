package quickfix.field;

import quickfix.IntField;

public class MDEntryPositionNo extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 290;

    public MDEntryPositionNo() {
        super(290);
    }

    public MDEntryPositionNo(int data) {
        super(290, data);
    }
}
