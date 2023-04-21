package quickfix.field;

import quickfix.BooleanField;

public class ForexReq extends BooleanField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 121;

    public ForexReq() {
        super(121);
    }

    public ForexReq(boolean data) {
        super(121, data);
    }
}
