package quickfix.field;

import quickfix.IntField;

public class RepoCollateralSecurityType extends IntField {

    static final long serialVersionUID = 20050617;

    public static final int FIELD = 239;

    public RepoCollateralSecurityType() {
        super(239);
    }

    public RepoCollateralSecurityType(int data) {
        super(239, data);
    }
}
