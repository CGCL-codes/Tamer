package carassius.BLL;

import carassius.BLL.Events.Object.Added.ObjectAddedListener;
import carassius.BLL.Events.Object.Added.ObjectAddedPublisher;
import carassius.BLL.Events.Object.ObjectEvent;
import carassius.BLL.Events.Object.Removed.ObjectRemovedListener;
import carassius.BLL.Events.Object.Removed.ObjectRemovedPublisher;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * @deprecated splitted in GregorianCalendarWithEvents and ArrayListWithEvents
 * @author siebz0r
 */
public class MarkableCalendar extends GregorianCalendar implements ObjectAddedPublisher, ObjectRemovedPublisher {

    private PropertyChangeSupport _changeSupport;

    private ArrayList<ObjectRemovedListener> _objectRemovedListeners;

    private ArrayList<ObjectAddedListener> _objectAddedListeners;

    private ArrayList<Long> _markedDates;

    public MarkableCalendar() {
        this(new GregorianCalendar());
    }

    public MarkableCalendar(GregorianCalendar date) {
        this(date.get(GregorianCalendar.YEAR), date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DATE));
    }

    public MarkableCalendar(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
        _markedDates = new ArrayList<Long>();
        _objectAddedListeners = new ArrayList<ObjectAddedListener>();
        _objectRemovedListeners = new ArrayList<ObjectRemovedListener>();
        _changeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public void set(int field, int value) {
        final String property;
        switch(field) {
            case YEAR:
                property = "year";
                break;
            case MONTH:
                property = "month";
                break;
            case DATE:
                property = "day";
            default:
                return;
        }
        final int oldVal = get(field);
        super.set(field, value);
        _changeSupport.firePropertyChange(property, oldVal, value);
    }

    public ArrayList<Long> getDates() {
        return _markedDates;
    }

    public boolean isMarked(Long date) {
        return _markedDates.contains(DateTimeHelper.stripTime(date));
    }

    public boolean addMarkedDate(Long date) {
        if (_markedDates.add(DateTimeHelper.stripTime(date))) {
            fireMarkedDateAddedEvent(new ObjectEvent(date, this));
            return true;
        }
        return false;
    }

    public boolean removeMarkedDate(Long date) {
        if (_markedDates.remove(DateTimeHelper.stripTime(date))) {
            fireMarkedDateRemovedEvent(new ObjectEvent(date, this));
            return true;
        }
        return false;
    }

    public void clearAllMarkedDates() {
        _markedDates.clear();
    }

    @Override
    public Object clone() {
        MarkableCalendar other = (MarkableCalendar) super.clone();
        other._markedDates = (ArrayList<Long>) _markedDates.clone();
        other._objectAddedListeners = (ArrayList<ObjectAddedListener>) _objectAddedListeners.clone();
        other._objectRemovedListeners = (ArrayList<ObjectRemovedListener>) _objectRemovedListeners.clone();
        return other;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property. The specified property may be user-defined, or one of the
     * following defaults:
     * <ul>
     *    <li>this calendar's year ("year")</li>
	 *    <li>this calendar's month ("month")</li>
	 *    <li>this calendar's day ("day")</li>
     * </ul>
	 * @param property one of the properties listed above
	 * @param listener listener to be added
	 */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        _changeSupport.addPropertyChangeListener(property, listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list for a specific
     * property. The specified property may be user-defined, or one of the
     * following defaults:
     * <ul>
     *    <li>this calendar's year ("year")</li>
	 *    <li>this calendar's month ("month")</li>
	 *    <li>this calendar's day ("day")</li>
     * </ul>
	 * @param property one of the properties listed above
	 * @param listener listener to be removed
	 */
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        _changeSupport.removePropertyChangeListener(property, listener);
    }

    @Override
    public void addObjectAddedListener(ObjectAddedListener listener) {
        _objectAddedListeners.add(listener);
    }

    @Override
    public void removeObjectAddedListener(ObjectAddedListener listener) {
        _objectAddedListeners.remove(listener);
    }

    @Override
    public void addObjectRemovedListener(ObjectRemovedListener listener) {
        _objectRemovedListeners.add(listener);
    }

    @Override
    public void removeObjectRemovedListener(ObjectRemovedListener listener) {
        _objectRemovedListeners.remove(listener);
    }

    private void fireMarkedDateAddedEvent(ObjectEvent objectEvent) {
        for (ObjectAddedListener listener : _objectAddedListeners) {
            listener.objectAdded(objectEvent);
        }
    }

    private void fireMarkedDateRemovedEvent(ObjectEvent objectEvent) {
        for (ObjectRemovedListener listener : _objectRemovedListeners) {
            listener.objectRemoved(objectEvent);
        }
    }
}
