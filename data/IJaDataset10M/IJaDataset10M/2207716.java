package org.progeeks.util.beans;

import java.beans.PropertyChangeEvent;

/**
 * Tests of the property holder utility class.  Extends the tests for
 * the BeanChangeSupport class to test the additional functionality of
 * the subclass.
 *
 * @version		$Revision: 1.6 $
 * @author		Paul Wisneskey
 */
public class PropertyHolderTests extends BeanChangeSupportTests {

    protected static final String UNKNOWN_PROPERTY = "unknownProperty";

    public void testSetGetObjectPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        Object object = new Object();
        holder.setObjectProperty(TEST_PROPERTY_NAME, object);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(object, event.getNewValue());
        assertEquals(object, holder.getObjectProperty(TEST_PROPERTY_NAME));
    }

    public void testSetGetObjectPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        Object object = new Object();
        holder.setObjectProperty(TEST_PROPERTY_NAME, object);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(object, event.getNewValue());
        assertEquals(object, holder.getObjectProperty(TEST_PROPERTY_NAME));
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(object, delegateEvent.getNewValue());
    }

    public void testGetObjectPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        Object object = new Object();
        Object defaultObject = new Object();
        holder.setObjectProperty(TEST_PROPERTY_NAME, object);
        assertEquals(object, holder.getObjectProperty(TEST_PROPERTY_NAME, defaultObject));
        assertEquals(defaultObject, holder.getObjectProperty(UNKNOWN_PROPERTY, defaultObject));
    }

    public void testSetObjectPropertyNoChange() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        Integer value = new Integer(34);
        holder.setObjectProperty(TEST_PROPERTY_NAME, value);
        listener.clear();
        holder.setObjectProperty(TEST_PROPERTY_NAME, value);
        assertFalse(listener.hasEvents());
        holder.setObjectProperty(TEST_PROPERTY_NAME, new Integer(34));
        assertFalse(listener.hasEvents());
    }

    public void testSetGetStringPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        String value = "Wombat";
        holder.setStringProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(value, event.getNewValue());
        assertEquals(value, holder.getStringProperty(TEST_PROPERTY_NAME, "Default"));
    }

    public void testSetGetStringPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        String value = "Wombat";
        holder.setStringProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(value, event.getNewValue());
        assertEquals(value, holder.getStringProperty(TEST_PROPERTY_NAME, "Default"));
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(value, delegateEvent.getNewValue());
    }

    public void testGetStringPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        String value = "Wombat";
        String defaultValue = "Default";
        holder.setStringProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getStringProperty(TEST_PROPERTY_NAME, defaultValue));
        assertEquals(defaultValue, holder.getStringProperty(UNKNOWN_PROPERTY, defaultValue));
    }

    public void testSetGetIntPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        int value = 8123;
        holder.setIntProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Integer(value), (Integer) event.getNewValue());
        assertEquals(value, holder.getIntProperty(TEST_PROPERTY_NAME, 313));
    }

    public void testSetGetIntPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        int value = 8123;
        holder.setIntProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Integer(value), (Integer) event.getNewValue());
        assertEquals(value, holder.getIntProperty(TEST_PROPERTY_NAME, 313));
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(new Integer(value), delegateEvent.getNewValue());
    }

    public void testGetIntPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        int value = 8213;
        int defaultValue = 313;
        holder.setIntProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getIntProperty(TEST_PROPERTY_NAME, defaultValue));
        assertEquals(defaultValue, holder.getIntProperty(UNKNOWN_PROPERTY, defaultValue));
    }

    public void testSetGetLongPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        long value = 8123;
        holder.setLongProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Long(value), (Long) event.getNewValue());
        assertEquals(value, holder.getLongProperty(TEST_PROPERTY_NAME, 313));
    }

    public void testSetGetLongPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        long value = 8123;
        holder.setLongProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Long(value), (Long) event.getNewValue());
        assertEquals(value, holder.getLongProperty(TEST_PROPERTY_NAME, 313));
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(new Long(value), delegateEvent.getNewValue());
    }

    public void testGetLongPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        long value = 8213;
        long defaultValue = 313;
        holder.setLongProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getLongProperty(TEST_PROPERTY_NAME, defaultValue));
        assertEquals(defaultValue, holder.getLongProperty(UNKNOWN_PROPERTY, defaultValue));
    }

    public void testSetGetDoublePropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        double value = 123.456;
        holder.setDoubleProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Double(value), (Double) event.getNewValue());
        assertEquals(value, holder.getDoubleProperty(TEST_PROPERTY_NAME, 313.414), 0.0);
    }

    public void testSetGetDoublePropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        double value = 123.456;
        holder.setDoubleProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Double(value), (Double) event.getNewValue());
        assertEquals(value, holder.getDoubleProperty(TEST_PROPERTY_NAME, 313.414), 0.0);
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(new Double(value), delegateEvent.getNewValue());
    }

    public void testGetDoublePropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        double value = 123.456;
        double defaultValue = 313.414;
        holder.setDoubleProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getDoubleProperty(TEST_PROPERTY_NAME, defaultValue), 0.0);
        assertEquals(defaultValue, holder.getDoubleProperty(UNKNOWN_PROPERTY, defaultValue), 0.0);
    }

    public void testSetGetFloatPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        float value = 123.456f;
        holder.setFloatProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Float(value), (Float) event.getNewValue());
        assertEquals(value, holder.getFloatProperty(TEST_PROPERTY_NAME, 313.414f), 0.0);
    }

    public void testSetGetFloatPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        float value = 123.456f;
        holder.setFloatProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(new Float(value), (Float) event.getNewValue());
        assertEquals(value, holder.getFloatProperty(TEST_PROPERTY_NAME, 313.414f), 0.0);
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(new Float(value), delegateEvent.getNewValue());
    }

    public void testGetFloatPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        float value = 123.456f;
        float defaultValue = 313.414f;
        holder.setFloatProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getFloatProperty(TEST_PROPERTY_NAME, defaultValue), 0.0);
        assertEquals(defaultValue, holder.getFloatProperty(UNKNOWN_PROPERTY, defaultValue), 0.0);
    }

    public void testSetGetBooleanPropertyNoDelegate() {
        PropertyHolder holder = new PropertyHolder();
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        boolean value = true;
        holder.setBooleanProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(Boolean.valueOf(value), (Boolean) event.getNewValue());
        assertEquals(value, holder.getBooleanProperty(TEST_PROPERTY_NAME, false));
    }

    public void testSetGetBooleanPropertyDelegate() {
        BeanChangeSupport delegate = new BeanChangeSupport();
        RecordingPropertyChangeListener delegateListener = new RecordingPropertyChangeListener();
        delegate.addPropertyChangeListener(delegateListener);
        PropertyHolder holder = new PropertyHolder(delegate);
        RecordingPropertyChangeListener listener = new RecordingPropertyChangeListener();
        holder.addPropertyChangeListener(listener);
        boolean value = true;
        holder.setBooleanProperty(TEST_PROPERTY_NAME, value);
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull(event);
        assertEquals(TEST_PROPERTY_NAME, event.getPropertyName());
        assertEquals(Boolean.valueOf(value), (Boolean) event.getNewValue());
        assertEquals(value, holder.getBooleanProperty(TEST_PROPERTY_NAME, false));
        PropertyChangeEvent delegateEvent = delegateListener.getLastEvent();
        assertNotNull(delegateEvent);
        assertEquals(TEST_PROPERTY_NAME, delegateEvent.getPropertyName());
        assertEquals(Boolean.valueOf(value), delegateEvent.getNewValue());
    }

    public void testGetBooleanPropertyDefaultValue() {
        PropertyHolder holder = new PropertyHolder();
        boolean value = true;
        boolean defaultValue = false;
        holder.setBooleanProperty(TEST_PROPERTY_NAME, value);
        assertEquals(value, holder.getBooleanProperty(TEST_PROPERTY_NAME, defaultValue));
        assertEquals(defaultValue, holder.getBooleanProperty(UNKNOWN_PROPERTY, defaultValue));
    }
}
