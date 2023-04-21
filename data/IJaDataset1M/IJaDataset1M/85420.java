package plugin.properties;

public class StringCallbackProperty extends CallbackProperty {

    String value;

    public StringCallbackProperty(String name, CallbackType type, int min, int max, Object init) {
        super(name, type, min, max);
        this.value = (init != null ? (String) init : "");
    }

    @Override
    public String toString() {
        return super.toString() + " value: " + this.value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
