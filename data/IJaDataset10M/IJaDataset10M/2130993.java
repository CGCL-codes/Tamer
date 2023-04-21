package com.googlecode.progobots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AssertPropertyChangeListener implements PropertyChangeListener {

    private Object source;

    private String propertyName;

    private Object oldvalue;

    private Object newValue;

    private int timesCalled;

    public AssertPropertyChangeListener(Object source, String propertyName, Object oldValue, Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.oldvalue = oldValue;
        this.newValue = newValue;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        assertSame(source, evt.getSource());
        assertEquals(propertyName, evt.getPropertyName());
        assertEquals(oldvalue, evt.getOldValue());
        assertEquals(newValue, evt.getNewValue());
        timesCalled++;
    }

    public void verify(int times) {
        assertEquals(times, timesCalled);
    }

    public void verify() {
        verify(1);
    }
}
