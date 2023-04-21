package org.sourceforge.espro.elicitation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Because of too many fires we proxy them and just put them forward after
 * a short time, this should make the app faster.
 *
 * @author martin
 */
public class PropertyChangeSupportProxy extends PropertyChangeSupport {

    private final HashMap<String, Object[]> objectProxy = new HashMap<String, Object[]>();

    private final HashMap<String, TimerTask> timerProxy = new HashMap<String, TimerTask>();

    private final Timer timer = new Timer();

    private final long delay = 2;

    private DateFormat f = DateFormat.getTimeInstance(DateFormat.FULL);

    private final Object sourceBean;

    /**
    * Creates a new PropertyChangeSupportProxy object.
    *
    * @param sourceBean DOCUMENT ME!
    */
    public PropertyChangeSupportProxy(Object sourceBean) {
        super(sourceBean);
        this.sourceBean = sourceBean;
    }

    /**
    * DOCUMENT ME!
    *
    * @param evt DOCUMENT ME!
    */
    public void firePropertyChange(PropertyChangeEvent evt) {
        if (evt.getOldValue() == evt.getNewValue()) {
            return;
        }
        proxy(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    /**
    * DOCUMENT ME!
    *
    * @param propertyName DOCUMENT ME!
    * @param oldValue DOCUMENT ME!
    * @param newValue DOCUMENT ME!
    */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if (oldValue == newValue) {
            return;
        }
        proxy(propertyName, oldValue, newValue);
    }

    /**
    * DOCUMENT ME!
    *
    * @param propertyName DOCUMENT ME!
    * @param oldValue DOCUMENT ME!
    * @param newValue DOCUMENT ME!
    */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue == newValue) {
            return;
        }
        proxy(propertyName, oldValue, newValue);
    }

    /**
    * DOCUMENT ME!
    *
    * @param propertyName DOCUMENT ME!
    * @param oldValue DOCUMENT ME!
    * @param newValue DOCUMENT ME!
    */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if (oldValue == newValue) {
            return;
        }
        proxy(propertyName, oldValue, newValue);
    }

    private void fireProxy(String key) {
        Object object[] = objectProxy.get(key);
        super.firePropertyChange(new PropertyChangeEvent(sourceBean, key, object[0], object[1]));
        objectProxy.remove(key);
        try {
            timerProxy.get(key).cancel();
            timerProxy.remove(key);
        } catch (NullPointerException e) {
        }
        timer.purge();
    }

    private void proxy(final String key, final Object oldValue, final Object newValue) {
        if (timerProxy.containsKey(key)) {
            TimerTask task = timerProxy.get(key);
            try {
                task.cancel();
                timerProxy.remove(key);
                timer.purge();
            } catch (NullPointerException e) {
            }
        }
        if (objectProxy.containsKey(key)) {
            objectProxy.get(key)[1] = newValue;
        } else {
            Object object[] = { oldValue, newValue };
            objectProxy.put(key, object);
        }
        TimerTask task = new TimerTask() {

            public void run() {
                fireProxy(key);
            }
        };
        timerProxy.put(key, task);
        try {
            timer.schedule(task, delay);
        } catch (IllegalStateException e) {
            task.cancel();
            timerProxy.remove(key);
            timer.purge();
        }
    }
}
