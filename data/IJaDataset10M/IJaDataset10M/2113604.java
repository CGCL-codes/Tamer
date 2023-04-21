package net.sf.logsaw.properties.type;

import net.sf.logsaw.properties.IProperty;

/**
 * This class acts as a base for implementing properties.
 * 
 * @author Philipp Nanz
 */
public abstract class AbstractProperty<T> implements IProperty<T> {

    private boolean visible;

    private String key;

    private String label;

    /**
	 * Constructor.
	 * @param key the property key
	 * @param label the property label
	 */
    protected AbstractProperty(String key, String label) {
        this(key, label, true);
    }

    /**
	 * Constructor.
	 * @param key the property key
	 * @param label the property label
	 * @param visible whether property is visible
	 */
    protected AbstractProperty(String key, String label, boolean visible) {
        this.key = key;
        this.label = label;
        this.visible = visible;
    }

    /**
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @return the label
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * @return the visible
	 */
    public boolean isVisible() {
        return visible;
    }
}
