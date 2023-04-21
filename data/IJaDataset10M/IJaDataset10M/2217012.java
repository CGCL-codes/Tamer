package plugin.properties;

/**
 * reperesets an Integer Callback with given minimum and maximum values
 * 
 * @author skomp
 * 
 */
public class IntCallbackProperty extends CallbackProperty {

    private int value;

    protected IntCallbackProperty(String name, CallbackType type, int min, int max, Object init) {
        super(name, type, min, max);
        this.value = (init != null ? (Integer) init : 0);
    }

    @Override
    public String toString() {
        return super.toString() + " value: " + this.value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
