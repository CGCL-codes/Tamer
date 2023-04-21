package net.sf.timelogng.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.EventListenerList;

/**
 * This class contains an ordered collection of Periods that represent work for a particular activity.
 */
public class Task implements PropertyChangeListener, Comparable, Cloneable, Serializable {

    private static final long serialVersionUID = 7905710900982054401L;

    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String WORK_TIME = "workTime";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    private Project project;

    private String name = "";

    private String description = "";

    private Set periods = new TreeSet();

    private transient long workTime;

    public Task() {
    }

    public Task(String name) {
        setName(name);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        support.firePropertyChange(DESCRIPTION, oldDescription, description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange(NAME, oldName, name);
    }

    /**
     * This method is only provided for use with velocity. You should not use it to modify the
     * collection.
     */
    public Set getPeriods() {
        return periods;
    }

    /**
     * Sets the workTime for the Task and fires a workTime PropertyChange event to notify any
     * PropertyChange listeners of the Task.
     * 
     * @param workTime
     */
    private void setWorkTime(long workTime) {
        long oldWorkTime = this.workTime;
        this.workTime = workTime;
        support.firePropertyChange(WORK_TIME, oldWorkTime, workTime);
    }

    public long getWorkTime() {
        return workTime;
    }

    /**
     * Adds the specified Period to the collection of Period. The Task will also be added as a
     * listener to PropertyChange events of the newly added Period.
     * <p>
     * After the addition, the workTime is updated.
     * 
     * @param period
     */
    public void addPeriod(Period period) {
        period.setTask(this);
        period.addPropertyChangeListener(this);
        periods.add(period);
        setWorkTime(this.workTime + period.getWorkTime());
    }

    /**
     * Removes the specified Period from the collection of Periods. The Task will also be removed as
     * a listener to PropertyChange events of the Period.
     * <p>
     * After the removal, the workTime is updated.
     * 
     * @param period
     */
    public void removePeriod(Period period) {
        period.setTask(null);
        period.removePropertyChangeListener(this);
        periods.remove(period);
        setWorkTime(this.workTime - period.getWorkTime());
    }

    /**
     * @return the number of Periods in the collection
     */
    public int getPeriodCount() {
        return periods.size();
    }

    /**
     * Returns the Period at the specified index in the collection.
     * 
     * @param index
     * @return a Period
     */
    public Period getPeriod(int index) {
        return (Period) periods.toArray()[index];
    }

    /**
     * Returns the index of the Period in the collection.
     * 
     * @param period
     * @return the index of the Period or -1 if not found
     */
    public int indexOf(Period period) {
        int i = 0;
        for (Iterator iter = periods.iterator(); iter.hasNext(); i++) {
            Period element = (Period) iter.next();
            if (element.equals(period)) return i;
        }
        return -1;
    }

    public Iterator periodIterator() {
        return periods.iterator();
    }

    /**
     * Utility method to return the earliest start time for all Periods under the Task.
     * 
     * @return the start time
     */
    public Date getStartTime() {
        long startTimeInMillis = Long.MAX_VALUE;
        Iterator periodIter = periods.iterator();
        while (periodIter.hasNext()) {
            Period element = (Period) periodIter.next();
            if (element.getStartTimeInMillis() < startTimeInMillis) startTimeInMillis = element.getStartTimeInMillis();
        }
        return new Date(startTimeInMillis);
    }

    /**
     * Utility method to return the latest end time for all Periods under the Task.
     * 
     * @return the end time
     */
    public Date getEndTime() {
        long endTimeInMillis = Long.MIN_VALUE;
        Iterator periodIter = periods.iterator();
        while (periodIter.hasNext()) {
            Period element = (Period) periodIter.next();
            if (element.getEndTimeInMillis() > endTimeInMillis) endTimeInMillis = element.getEndTimeInMillis();
        }
        return new Date(endTimeInMillis);
    }

    public String toString() {
        return name;
    }

    private Object readResolve() {
        support = new PropertyChangeSupport(this);
        return this;
    }

    public void rebuildListenerLists() {
        Iterator periodIter = periods.iterator();
        while (periodIter.hasNext()) {
            Period period = (Period) periodIter.next();
            period.addPropertyChangeListener(this);
        }
    }

    /**
     * Recalculates the total workTime for the Task. Does not fire a PropertyChange event.
     */
    public void recalculateWorkTime() {
        workTime = 0;
        Iterator periodIter = periods.iterator();
        while (periodIter.hasNext()) {
            Period period = (Period) periodIter.next();
            workTime += period.getWorkTime();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return support.getPropertyChangeListeners();
    }

    public int compareTo(Object o) {
        Task task = (Task) o;
        return name.compareTo(task.getName());
    }

    /**
     * Returns a shallow clone of the Task. All collection fields will be recreated with no
     * elements.
     */
    public Object clone() throws CloneNotSupportedException {
        Task clone = (Task) super.clone();
        clone.support = new PropertyChangeSupport(clone);
        clone.periods = new TreeSet();
        clone.project = null;
        clone.workTime = 0;
        return clone;
    }

    /**
     * Returns a deep clone of the Task. Each Period element will also be cloned.
     */
    public Object deepClone() throws CloneNotSupportedException {
        Task clone = (Task) clone();
        Iterator periodIter = periodIterator();
        while (periodIter.hasNext()) {
            Period period = (Period) periodIter.next();
            Period periodClone = (Period) period.clone();
            clone.addPeriod(periodClone);
        }
        return clone;
    }

    /**
     * This method will be called when the property of a Period has changed. We are only interested
     * in changes to the startTime or endTime of a Period in order to update our own workTime
     * property.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
        if (evt.getSource() instanceof Period) {
            Period period = (Period) evt.getSource();
            if (evt.getPropertyName().equals(Period.START_TIME)) {
                long oldStartTimeInMillis = (Long) evt.getOldValue();
                long newStartTimeInMillis = (Long) evt.getNewValue();
                long delta = newStartTimeInMillis - oldStartTimeInMillis;
                setWorkTime(this.workTime - delta);
            } else if (evt.getPropertyName().equals(Period.END_TIME)) {
                long oldEndTimeInMillis = (Long) evt.getOldValue();
                long newEndTimeInMillis = (Long) evt.getNewValue();
                long delta = newEndTimeInMillis - oldEndTimeInMillis;
                setWorkTime(this.workTime + delta);
            }
        }
    }
}
