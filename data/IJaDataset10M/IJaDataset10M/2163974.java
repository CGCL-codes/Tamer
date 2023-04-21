package net.sourceforge.ondex.xten.workflow;

import net.sourceforge.ondex.xten.workflow.support.ExpieryEvent;
import net.sourceforge.ondex.xten.workflow.support.ExpieryListener;

/**
 * 
 * @author lysenkoa
 *
 */
public class Resource {

    protected Object resource;

    protected boolean isInvalid = true;

    protected Integer id;

    protected Integer useLimit = null;

    protected boolean isLocked = false;

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    public Resource(Integer id) {
        this.id = id;
    }

    public Resource(Integer id, Object resource) {
        this.id = id;
        setValue(resource);
        isInvalid = false;
    }

    public Integer getId() {
        return id;
    }

    public void clear() {
        if (isLocked) return;
        useLimit = null;
        listenerList = new javax.swing.event.EventListenerList();
        reset();
    }

    private void reset() {
        if (isLocked) return;
        resource = null;
        isInvalid = true;
    }

    public void setValue(Object resource) {
        if (isLocked) return;
        this.resource = resource;
        isInvalid = false;
    }

    public Object accessValue() {
        return resource;
    }

    public Object useValue() throws IllegalAccessException {
        if (isInvalid) {
            throw new IllegalAccessException("The resource handle is out of scope or was not initialised.");
        }
        Object temp = resource;
        if (useLimit != null) {
            useLimit--;
            if (useLimit <= 0) fireExpieryEventEvent();
        }
        return temp;
    }

    public boolean hasExpiered() {
        return isInvalid;
    }

    public void addExpieryEventListener(ExpieryListener listener, Integer useLimit) {
        if (isLocked) return;
        this.useLimit = useLimit;
        listenerList.add(ExpieryListener.class, listener);
    }

    public void removeExpieryEventListener(ExpieryListener listener) {
        listenerList.remove(ExpieryListener.class, listener);
    }

    void fireExpieryEventEvent() {
        useLimit = null;
        listenerList = new javax.swing.event.EventListenerList();
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ExpieryListener.class) {
                ((ExpieryListener) listeners[i + 1]).expieryEventOccured(new ExpieryEvent(this, this.getId()));
            }
        }
        this.reset();
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
