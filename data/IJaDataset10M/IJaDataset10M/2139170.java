package quickfix.field;

import quickfix.IntField;

public class LegProduct extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 607;

    public LegProduct() {
        super(607);
    }

    public LegProduct(int data) {
        super(607, data);
    }
}
