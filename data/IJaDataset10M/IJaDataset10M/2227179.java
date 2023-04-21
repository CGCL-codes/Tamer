package quickfix.field;

import quickfix.CharField;

public class Nested2PartyIDSource extends CharField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 758;

    public Nested2PartyIDSource() {
        super(758);
    }

    public Nested2PartyIDSource(char data) {
        super(758, data);
    }
}
