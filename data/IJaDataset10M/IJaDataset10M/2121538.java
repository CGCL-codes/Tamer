package elapse.domain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * THIS CLASS IS OBSOLETE. IT WAS REPLACED BY EVENTHANDLER
 * 
 * Helper class that forwards events from an EventSender to a client.
 * Useful when a client must connect to different event senders over time.
 * The client can connect to the proxy (once), and the proxy can easily be connected to
 * any event sender using setEventSender.
 * @author mnieber
 *
 * @param <EventSender> The type of the original event sender. 
 */
public class EventSenderProxy<EventSender> implements PropertyChangeListener {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private EventSender eventSender;

    private Method removeListenerFromEventSender;

    private Method addListenerToEventSender;

    public EventSenderProxy() {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (PropertyChangeListener listener : propertyChangeSupport.getPropertyChangeListeners()) {
            listener.propertyChange(evt);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public EventSender getEventSender() {
        return eventSender;
    }

    public void setEventSender(EventSender sender) {
        boolean mustThrow = false;
        try {
            if (this.eventSender != null) {
                removeListenerFromEventSender.invoke(this.eventSender, this);
            }
            this.eventSender = sender;
            if (sender != null) {
                removeListenerFromEventSender = sender.getClass().getMethod("removePropertyChangeListener", PropertyChangeListener.class);
                addListenerToEventSender = sender.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class);
                addListenerToEventSender.invoke(sender, this);
            }
        } catch (IllegalArgumentException e) {
            mustThrow = true;
        } catch (IllegalAccessException e) {
            mustThrow = true;
        } catch (InvocationTargetException e) {
            mustThrow = true;
        } catch (SecurityException e) {
            mustThrow = true;
        } catch (NoSuchMethodException e) {
            mustThrow = true;
        }
        if (mustThrow) {
            throw new NoSuchMethodRuntimeException("removePropertyChangeListener and/or addPropertyChangeListener");
        }
    }
}
