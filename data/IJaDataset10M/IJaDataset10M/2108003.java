package org.ddevil.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author Rich O'Connell
 */
public class BeanData extends AbstractData {

    private static final long serialVersionUID = -2126857167294086264L;

    private final BeanWrapper wrapper;

    protected List<String> properties;

    public BeanData() {
        wrapper = createWrapper();
        configureWrapper(wrapper);
        properties = new ArrayList<String>();
        for (Field f : getClass().getDeclaredFields()) {
            int m = f.getModifiers();
            if (!Modifier.isFinal(m) && !Modifier.isStatic(m) && !isInternal(f)) {
                properties.add(f.getName());
            }
        }
    }

    private boolean isInternal(Field f) {
        for (Annotation a : f.getAnnotations()) {
            if (a instanceof Internal) {
                return true;
            }
        }
        return false;
    }

    protected BeanWrapper createWrapper() {
        return new BeanWrapperImpl(this);
    }

    protected void configureWrapper(BeanWrapper wrapper) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void setDataItem(String id, Object newValue) {
        if (contains(id)) {
            wrapper.setPropertyValue(id, newValue);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean contains(String id) {
        return properties.contains(id);
    }

    /**
	 * {@inheritDoc}
	 */
    public Object getValue(String id) {
        return wrapper.getPropertyValue(id);
    }

    /**
	 * {@inheritDoc}
	 */
    public int size() {
        return properties.size();
    }

    /**
	 * {@inheritDoc}
	 */
    public Iterator<Attribute> iterator() {
        List<Attribute> rv = new ArrayList<Attribute>(size());
        for (String prop : properties) {
            rv.add(new Attribute(prop, getValue(prop)));
        }
        return rv.iterator();
    }
}
