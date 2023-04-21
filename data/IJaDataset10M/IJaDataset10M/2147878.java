package com.googlecode.beauti4j.gwt.databinding.client.driver;

import java.io.Serializable;
import java.util.List;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.beauti4j.gwt.databinding.client.DataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.DataEventCode;
import com.googlecode.beauti4j.gwt.databinding.client.DataEventListener;
import com.googlecode.beauti4j.gwt.databinding.client.DataSource;
import com.googlecode.beauti4j.gwt.databinding.client.DataStore;
import com.googlecode.beauti4j.gwt.databinding.client.event.ObjectAddedDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.ObjectGotDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.ObjectRemovedDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.ObjectsGotDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.ObjectsSavedDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.OperationFailedDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.PropertyUpdatedDataEvent;
import com.googlecode.beauti4j.gwt.databinding.client.event.ValidationFailedDataEvent;

/**
 * The basic DataDriver for widget of Text.
 * 
 * @author Hang Yuan (anthony.yuan@gmail.com)
 */
public abstract class AbstractTextDataDriver extends AbstractDataDriver {

    /** DataEventListener. */
    private DataEventListener dataEventListener;

    /** The property used for binding with DataStore. */
    private String bindingProperty;

    /**
     * Constructor.
     * 
     * @param widget
     *            the widget for binding
     * @param dataSource
     *            the binded DataSource
     * @param bindingProperty
     *            the property used for binding with DataStore
     */
    public AbstractTextDataDriver(final Widget widget, final DataSource dataSource, final String bindingProperty) {
        super(widget, dataSource);
        assert (bindingProperty != null) : "bindingProperty is required";
        this.bindingProperty = bindingProperty;
    }

    /** @return The property used for binding with DataStore */
    public final String getBindingProperty() {
        return bindingProperty;
    }

    /**
     * Obtain dataEventListener. If it's null, then create a new one.
     * 
     * @return dataEventListener
     */
    private DataEventListener getDataEventListener() {
        if (dataEventListener == null) {
            dataEventListener = new DataEventListener() {

                @SuppressWarnings("unchecked")
                public void process(final DataEvent dataEvent) {
                    if (dataEvent.getSource() == getBindedObject() || getDataSource().getDataStores().getCurrentDataStore() == null) {
                        return;
                    }
                    if (dataEvent instanceof PropertyUpdatedDataEvent) {
                        PropertyUpdatedDataEvent propertyUpdatedDataEvent = (PropertyUpdatedDataEvent) dataEvent;
                        if (bindingProperty.equals(propertyUpdatedDataEvent.getProperty())) {
                            updateWidgetValue();
                        }
                        return;
                    }
                    if (dataEvent instanceof ObjectAddedDataEvent || dataEvent instanceof ObjectsGotDataEvent || dataEvent instanceof ObjectGotDataEvent || dataEvent instanceof ObjectsSavedDataEvent || dataEvent instanceof ObjectRemovedDataEvent) {
                        updateWidgetValue();
                        return;
                    }
                    if (dataEvent instanceof OperationFailedDataEvent) {
                        OperationFailedDataEvent theEvent = (OperationFailedDataEvent) dataEvent;
                        if (!(theEvent.getException() instanceof HasInvalidValues)) {
                            return;
                        }
                        HasInvalidValues theExp = (HasInvalidValues) theEvent.getException();
                        GwtInvalidValue[] invalidValues = theExp.getInvalidValues();
                        if (invalidValues == null) {
                            return;
                        }
                        for (int i = 0; i < invalidValues.length; i++) {
                            if (getBindingProperty().equals(invalidValues[i].getPropertyName())) {
                                markInvalid(invalidValues[i].getMessage());
                            }
                        }
                        return;
                    }
                }
            };
        }
        return dataEventListener;
    }

    /**
     * Add DataEventListener for: ObjectAddedDataEvent, ObjectsGotDataEvent,
     * ObjectGotDataEvent, PropertyUpdatedDataEvent, ObjectsSavedDataEvent,
     * ObjectRemovedDataEvent.
     */
    public final void registerDataEventListeners() {
        final DataSource dataSource = getDataSource();
        dataEventListener = getDataEventListener();
        dataSource.addListener(DataEventCode.OBJECT_ADDED_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.OBJECTS_GOT_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.OBJECT_GOT_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.PROPERTY_UPDATED_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.OBJECTS_SAVED_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.OBJECT_REMOVED_DATA_EVENT, dataEventListener);
        dataSource.addListener(DataEventCode.OPERATION_FAILED_DATA_EVENT, dataEventListener);
    }

    /**
     * Unregister DataEventListener.
     */
    protected final void unregisterDataEventListener() {
        if (dataEventListener != null) {
            getDataSource().removeListener(dataEventListener);
            dataEventListener = null;
        }
    }

    /**
     * Mark the binded field as invalid.
     * 
     * @param message
     *            the validation message
     */
    public void markInvalid(final String message) {
    }

    /**
     * Fire when data of the binded widget is changed.
     * 
     * @param sender
     *            the widget caused the change event
     */
    protected final void onDataChange(final Widget sender) {
        DataSource dataSource = getDataSource();
        DataStore<? extends Serializable> currentDataStore = dataSource.getDataStores().getCurrentDataStore();
        if (currentDataStore == null) {
            return;
        }
        String newValue = (String) getWidgetValue();
        List<String> errors = validateData(newValue, currentDataStore);
        if (!errors.isEmpty()) {
            dataSource.dispatchEvent(new ValidationFailedDataEvent<DataStore<? extends Serializable>>(this, errors, sender, currentDataStore));
            return;
        }
        updateDataStoreValue(newValue);
        dataSource.dispatchEvent(new PropertyUpdatedDataEvent<DataStore<? extends Serializable>>(sender, bindingProperty, currentDataStore));
    }

    /**
     * Update the given DataStore with the String value of Widget.
     * 
     * @param newValue
     *            the String value of property
     */
    private void updateDataStoreValue(final String newValue) {
        DataStore<? extends Serializable> currentDataStore = getDataSource().getDataStores().getCurrentDataStore();
        currentDataStore.setDirty(true);
        currentDataStore.setProperty(bindingProperty, newValue);
    }
}
