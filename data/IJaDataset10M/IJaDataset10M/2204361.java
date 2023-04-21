package quickfix.field;

import quickfix.StringField;

public class OrderInputDevice extends StringField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 821;

    public OrderInputDevice() {
        super(821);
    }

    public OrderInputDevice(String data) {
        super(821, data);
    }
}
