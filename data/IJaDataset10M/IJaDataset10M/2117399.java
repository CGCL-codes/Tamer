package quickfix.field;

import quickfix.StringField;

public class NestedUserDataName extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 9001;

    public NestedUserDataName() {
        super(9001);
    }

    public NestedUserDataName(String data) {
        super(9001, data);
    }
}
