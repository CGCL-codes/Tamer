package rfb;

public class StringParameter extends VoidParameter {

    public StringParameter(String name_, String desc_, String v) {
        super(name_, desc_);
        value = v;
        defValue = v;
    }

    public boolean setParam(String v) {
        value = v;
        return value != null;
    }

    public String getDefaultStr() {
        return defValue;
    }

    public String getValueStr() {
        return value;
    }

    public String getValue() {
        return value;
    }

    protected String value;

    protected String defValue;
}
