package com.apelon.dts.client.events;

import java.util.EventObject;

/**
 * Defines basic data change event.
 *
 * @author Apelon Inc.
 * @version DTS 3.4
 * @since DTS 3.4
 */
public class DataChangeEvent extends EventObject {

    /**
   * Starts the ID sequence at 1000 for Data Change events.
   *
   * @see #EVENT_TYPE_NEW
   * @see #EVENT_TYPE_MODIFY
   * @see #EVENT_TYPE_DELETE
   * @see #EVENT_TYPE_LAST
   */
    public static final int EVENT_TYPE_FIRST = 1000;

    /**
   * Reflects the current end of the ID sequence at EVENT_TYPE_FIRST + 2
   * for Data change events.
   *
   * @see #EVENT_TYPE_NEW
   * @see #EVENT_TYPE_MODIFY
   * @see #EVENT_TYPE_DELETE
   * @see #EVENT_TYPE_FIRST
   */
    public static final int EVENT_TYPE_LAST = EVENT_TYPE_FIRST + 2;

    /**
   * New EVENT Type Event ID = 1000
   *
   * @see #EVENT_TYPE_DELETE
   * @see #EVENT_TYPE_MODIFY
   */
    public static final int EVENT_TYPE_NEW = EVENT_TYPE_FIRST;

    /**
   * EVENT Type Delete Event ID = 1001
   *
   * @see #EVENT_TYPE_NEW
   * @see #EVENT_TYPE_MODIFY
   */
    public static final int EVENT_TYPE_DELETE = EVENT_TYPE_FIRST + 1;

    /**
   * EVENT Type Modify Event ID = 1002
   *
   * @see #EVENT_TYPE_NEW
   * @see #EVENT_TYPE_DELETE
   */
    public static final int EVENT_TYPE_MODIFY = EVENT_TYPE_LAST;

    /**
   * Value of -1 to flag an unitialized or invalid setting for {@link #eventType}
   *
   * @see #EVENT_TYPE_NEW
   * @see #EVENT_TYPE_DELETE
   * @see #EVENT_TYPE_MODIFY
   */
    public static final int INVALID_TYPE_EVENT = -1;

    /**
   * The eventType can be set to <code>EVENT_TYPE_NEW</code>,
   * <code>EVENT_TYPE_MODIFY</code>, or <code>EVENT_TYPE_DELETE</code>.
   * By default it is set to <code>INVALID_TYPE_EVENT</code>.
   */
    private int eventType = INVALID_TYPE_EVENT;

    private Object newData = null;

    private Object oldData = null;

    private Object changedData = null;

    /**
   * Constructs a DataChangeEvent object. Initializes the event type based on the value passed.
   *
   * @param source Should be an object that is a valid source of Data Change events.
   * @param oldData For adding a new data, the oldData should null;
   *                For updating a data, the oldData should be the data before being updated;
   *                For deleting a data, the oldData should be the data that is deleted.
   * @param newData For adding a new data, the newData should be the data that is added;
   *                For updating a data, the newData should be the data after being updated;
   *                For deleting a data, the newData should be null.
   */
    public DataChangeEvent(Object source, Object oldData, Object newData) {
        super(source);
        if (oldData == null) {
            if (newData != null) {
                eventType = EVENT_TYPE_NEW;
            }
        } else {
            if (newData != null) {
                eventType = EVENT_TYPE_MODIFY;
            } else {
                eventType = EVENT_TYPE_DELETE;
            }
        }
        this.oldData = oldData;
        this.newData = newData;
    }

    /**
   * Constructs a DataChangeEvent object. Initializes the changedData as the data which has changed.
   *
   * @param source Should be an object that is a valid source of Data Change events.
   * @param data The data which has changed. In the case of modifiy, this should be the data after change.
   * @param eventType If adding a new data, the eventType should be {@link #EVENT_TYPE_NEW}
   *                  If modifying a data, the eventType should be {@link #EVENT_TYPE_MODIFY}
   *                  If deleting a data, the eventType should be {@link #EVENT_TYPE_DELETE}
   */
    public DataChangeEvent(Object source, Object data, int eventType) {
        super(source);
        this.changedData = data;
        if (eventType >= EVENT_TYPE_FIRST && eventType <= EVENT_TYPE_LAST) {
            this.eventType = eventType;
        }
    }

    /**
   * Get Event Type. The Event Type can be <code>EVENT_TYPE_NEW</code>,
   * <code>EVENT_TYPE_MODIFY</code>, or <code>EVENT_TYPE_DELETE</code>.
   *
   * @return int If the data is added, return <code>EVENT_TYPE_NEW</code>;
   *             If the data is updated, return <code>EVENT_TYPE_MODIFY</code>;
   *             If the data is deleted, return <code>EVENT_TYPE_DELETE</code>.
   */
    public int getEventType() {
        return this.eventType;
    }

    /**
   * Get the New Data.
   *
   * @return Object If a data is added, return the new data that is added;
   *                If a data is updated, return the new data after updating;
   *                If a data is deleted, return null.
   */
    protected Object getNewData() {
        return newData;
    }

    /**
   * Get the Old Data.
   *
   * @return Object If a data is added, return null;
   *                If a data is updated, return the old data before updating;
   *                If a data is deleted, return the data that is deleted.
   */
    protected Object getOldData() {
        return oldData;
    }

    /**
   * Get the data which has changed.
   *
   * @return Object If a data is added, return the data that is added;
   *                If a data is updated, return the data after updating;
   *                If a data is deleted, return the data is deleted.
   */
    protected Object getChangedData() {
        return changedData;
    }
}
