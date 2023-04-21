package quickfix.field;

import quickfix.CharField;

public class LegOptAttribute extends CharField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 613;

    public LegOptAttribute() {
        super(613);
    }

    public LegOptAttribute(char data) {
        super(613, data);
    }
}
