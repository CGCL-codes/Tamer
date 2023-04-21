package org.base.apps.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import static org.base.apps.beans.BoundEvent.*;
import org.base.apps.beans.events.PropertyChangeEmitter;
import org.base.apps.beans.expr.Evaluator;
import org.base.apps.beans.util.BeanUtil;

/**
 * Represents an instance of something, with mutable bound properties, and
 * acts as an object container (to hold <code>null</code> values, for example).
 * 
 * @author Kevan Simpson
 */
public class Thing implements BeanContainer<Object>, PropertyChangeListener {

    private Object mBean;

    private Map<String, BoundProperty> mProperties;

    private Evaluator mEvaluator;

    /**
     * 
     */
    public Thing(Object bean) {
        setProperties(new HashMap<String, BoundProperty>());
        setBean(bean);
    }

    /**
     * 
     */
    public Thing(Class<?> type) {
        setProperties(new HashMap<String, BoundProperty>());
        setType(type);
    }

    protected void setType(Class<?> type) {
        Map<String, PropertyDescriptor> map = BeanUtil.loadBoundDescriptors(type, false);
        if (getProperties().isEmpty()) {
            for (String key : map.keySet()) {
                PropertyDescriptor desc = map.get(key);
                if (desc != null) {
                    BoundProperty bp = addProperty(key);
                    bp.setDescriptor(desc);
                }
            }
        }
    }

    public synchronized BoundProperty addProperty(String name) {
        BoundProperty bp = getProperty(name);
        if (bp == null && StringUtils.isNotBlank(name)) {
            bp = new BoundProperty(this, name);
            getProperties().put(name, bp);
        }
        return bp;
    }

    public BoundProperty getProperty(String name) {
        return getProperties().get(name);
    }

    public synchronized BoundProperty removeProperty(String name) {
        BoundProperty bp = getProperties().remove(name);
        if (bp != null) {
            bp.setThing(null);
            bp.setDescriptor(null);
        }
        return bp;
    }

    /** @see org.base.apps.beans.BeanContainer#getBean() */
    @Override
    public Object getBean() {
        return mBean;
    }

    /** @see org.base.apps.beans.BeanContainer#setBean(java.lang.Object) */
    @Override
    public void setBean(Object bean) {
        if (bean != null && getProperties().isEmpty()) {
            setType(bean.getClass());
        }
        Object oldBean = mBean;
        if (oldBean instanceof PropertyChangeEmitter) {
            PropertyChangeEmitter emit = (PropertyChangeEmitter) oldBean;
            emit.removePropertyChangeListener(this);
        }
        mBean = bean;
        setEvaluator(new Evaluator(mBean));
        if (mBean instanceof PropertyChangeEmitter) {
            PropertyChangeEmitter emit = (PropertyChangeEmitter) mBean;
            emit.addPropertyChangeListener(this);
        }
        for (String name : getProperties().keySet()) {
            BoundProperty prop = getProperties().get(name);
            prop.firePropertyChange(valueChanged.toKey(), oldBean, mBean);
        }
    }

    /** @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent) */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (String name : getProperties().keySet()) {
            BoundProperty prop = getProperties().get(name);
            if (StringUtils.equals(prop.getName(), evt.getPropertyName())) {
                prop.firePropertyChange(valueChanged.toKey(), null, getBean());
                return;
            }
        }
    }

    /**
     * @return the evaluator
     */
    protected Evaluator getEvaluator() {
        return mEvaluator;
    }

    /**
     * @return the properties
     */
    protected Map<String, BoundProperty> getProperties() {
        return mProperties;
    }

    /**
     * @param evaluator the evaluator to set
     */
    protected void setEvaluator(Evaluator evaluator) {
        mEvaluator = evaluator;
    }

    /**
     * @param properties the properties to set
     */
    protected void setProperties(Map<String, BoundProperty> properties) {
        mProperties = properties;
    }
}
