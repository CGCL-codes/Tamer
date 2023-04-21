package osdep.proxy;

/**
 * The value of a property. It can be set or not.
 * The reason we are not using a simple nullable value is
 * that a property can be set and have null value
 * @author SHZ Mar 14, 2008
 */
public class PropertyValue<V> {

    /**
	 * Tells if the property has been set
	 */
    private boolean isSet;

    /**
	 * The value of the property
	 */
    private V value;

    /**
	 * @param value
	 */
    public PropertyValue(V value) {
        this.isSet = true;
        this.value = value;
    }

    /**
	 */
    public PropertyValue() {
        this.isSet = false;
        this.value = null;
    }

    /**
	 * @return true if the property has been set
	 */
    public boolean isSet() {
        return isSet;
    }

    /**
	 * @return the value
	 */
    public V getValue() {
        return value;
    }
}
