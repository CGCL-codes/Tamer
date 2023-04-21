package quickfix.field;

import quickfix.DoubleField;

public class ParticipationRate extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 849;

    public ParticipationRate() {
        super(849);
    }

    public ParticipationRate(double data) {
        super(849, data);
    }
}
