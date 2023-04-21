package quickfix.field;

import quickfix.StringField;

public class UnderlyingSecurityAltIDSource extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 459;

    public UnderlyingSecurityAltIDSource() {
        super(459);
    }

    public UnderlyingSecurityAltIDSource(String data) {
        super(459, data);
    }
}
