package net.sf.bootstrap.framework.dto;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base abstract class of Data Transfer Objects (DTO) to capture new and
 * changed values for remote transfer.
 *
 * @author Mark Moloney
 */
public abstract class BaseDTO implements DTO, Serializable, PropertyChangeListener {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    protected Map newValues = new HashMap();

    private boolean initialised = false;

    private boolean changed = false;

    public void init() {
        if (!newValues.isEmpty()) {
            HashMap copy = new HashMap(newValues);
            for (Iterator i = newValues.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                if (key.charAt(0) == '_') continue;
                copy.remove(key);
            }
            newValues = copy;
        }
        initialised = true;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public abstract Long getId();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (oldValue == null && newValue == null) return;
        if (oldValue == null || newValue == null || !oldValue.equals(newValue)) {
            if (!newValues.containsKey(property)) {
                newValues.put(property, newValue);
            }
            this.changed = true;
            support.firePropertyChange(property, oldValue, newValue);
        }
    }

    public Map getNewValues() {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting new values for " + this.getClass().getName());
        }
        Map convertedValues = new HashMap();
        for (Iterator i = newValues.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (logger.isDebugEnabled()) {
                logger.debug("new value for " + key + " (" + value.getClass().getName() + "):");
            }
            if (value instanceof BaseDTO) {
                if (logger.isDebugEnabled()) {
                    logger.debug("DTO: " + value);
                }
                BaseDTO dto = (BaseDTO) value;
                convertedValues.put(key, dto.getNewValues());
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(value);
                }
                convertedValues.put(key, value);
            }
        }
        getNewListValues(convertedValues);
        return convertedValues;
    }

    protected abstract void getNewListValues(Map convertedValues);

    public boolean isChanged() {
        return changed;
    }

    /**
     * To detect changes in ListWrappers
     */
    public void propertyChange(PropertyChangeEvent ev) {
        if (logger.isDebugEnabled()) {
            logger.debug("Property change!");
            logger.debug("Source: " + ev.getSource());
        }
        if (ev.getSource() instanceof ListWrapper) {
            String property = ev.getPropertyName();
            if (logger.isDebugEnabled()) {
                logger.debug("for property: " + property);
            }
            if (!newValues.containsKey(property)) {
                newValues.put(property, ev.getSource());
            }
            this.changed = true;
            support.firePropertyChange(ev);
        }
    }
}
