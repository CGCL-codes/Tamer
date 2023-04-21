package quickfix.field;

import quickfix.DoubleField;

public class ContractMultiplier extends DoubleField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 231;

    public ContractMultiplier() {
        super(231);
    }

    public ContractMultiplier(double data) {
        super(231, data);
    }
}
