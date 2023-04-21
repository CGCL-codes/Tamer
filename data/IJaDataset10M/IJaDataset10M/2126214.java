package fuzzy.membershipfunctions;

/**
 * @author balzarot
 */
public class ConstantFunction extends FloatMembershipFunction {

    protected float _value;

    public ConstantFunction(float value) {
        if (value < 0) throw new IllegalArgumentException("value < 0");
        if (value > 1) throw new IllegalArgumentException("value > 1");
        _value = value;
    }

    public float getMembershipValue(float crisp) {
        return _value;
    }

    public float getValue() {
        return _value;
    }

    public void translate(float delta) {
    }
}
