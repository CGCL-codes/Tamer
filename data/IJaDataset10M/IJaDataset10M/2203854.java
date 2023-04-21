package com.panopset.gui;

import static com.panopset.Commons.EMPTY_STRING;
import static com.panopset.Util.isPopulated;
import java.util.Properties;
import java.util.Vector;

/**
 * Map properties with GUI objects.
 * 
 * @author Karl Dinwiddie
 *
 */
public abstract class PersistentContainer {

    private final String key;

    protected abstract String getComponentValue();

    protected abstract void setComponentValue(String value);

    public PersistentContainer(Vector<PersistentContainer> containers, String key) {
        this.key = key;
        containers.add(this);
    }

    public String getDefaultValue() {
        return EMPTY_STRING;
    }

    public void load(Properties props) {
        String s = (String) props.get(this.key);
        assert (s != null);
        setComponentValue(isPopulated((String) s) ? s.toString() : getDefaultValue());
    }

    public void flush(Properties props) {
        if (getComponentValue() != null) {
            props.put(this.key, getComponentValue());
        }
    }
}
