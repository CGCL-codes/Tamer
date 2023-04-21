package ti.log;

public class MinimumIntegerAttributeHandler extends AttributeHandler {

    public MinimumIntegerAttributeHandler(String n) {
        super(n);
    }

    public IAttribute merge(IAttribute a, IAttribute b) {
        IAttribute result = null;
        if (a instanceof IntegerAttribute && b instanceof IntegerAttribute && (a.getName().compareTo(b.getName())) == 0) {
            long aval = ((IntegerAttribute) a).getValue();
            long bval = ((IntegerAttribute) b).getValue();
            result = new IntegerAttribute(a.getName(), (aval < bval) ? aval : bval);
        }
        return result;
    }
}
