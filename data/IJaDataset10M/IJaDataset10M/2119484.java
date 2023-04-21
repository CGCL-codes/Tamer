package quickfix.field;

import quickfix.StringField;

public class RelatdSym extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 46;

    public RelatdSym() {
        super(46);
    }

    public RelatdSym(String data) {
        super(46, data);
    }
}
