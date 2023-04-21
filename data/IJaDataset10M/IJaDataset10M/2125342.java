package quickfix.field;

import quickfix.StringField;

public class AltMDSourceID extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 817;

    public AltMDSourceID() {
        super(817);
    }

    public AltMDSourceID(String data) {
        super(817, data);
    }
}
