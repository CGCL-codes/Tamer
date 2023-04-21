package org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets;

import org.ufacekit.ui.swing.jface.viewers.internal.swt.SWT;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.SWTException;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.events.DisposeListener;

/**
 * Internal
 * @since 1.0
 *
 */
public class Widget {

    /**
	 * Internal
	 */
    int state;

    /**
	 * Internal
	 */
    Object data;

    /**
	 * Internal
	 */
    EventTable eventTable;

    /**
	 * Internal
	 */
    static final int DISPOSED = 1 << 0;

    /**
	 * Internal
	 */
    static final int KEYED_DATA = 1 << 2;

    /**
	 * Returns the application defined widget data associated
	 * with the receiver, or null if it has not been set. The
	 * <em>widget data</em> is a single, unnamed field that is
	 * stored with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If
	 * the object stored in the widget data needs to be notified
	 * when the widget is disposed of, it is the application's
	 * responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @return the widget data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
	 * </ul>
	 *
	 * @see #setData(Object)
	 */
    public Object getData() {
        checkWidget();
        return (state & KEYED_DATA) != 0 ? ((Object[]) data)[0] : data;
    }

    /**
	 * Returns the application defined property of the receiver
	 * with the specified name, or null if it has not been set.
	 * <p>
	 * Applications may have associated arbitrary objects with the
	 * receiver in this fashion. If the objects stored in the
	 * properties need to be notified when the widget is disposed
	 * of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 * @param <O> object type
	 *
	 * @param	key the name of the property
	 * @return the value of the property or null if it has not been set
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setData(String, Object)
	 */
    @SuppressWarnings("unchecked")
    public <O> O getData(String key) {
        checkWidget();
        if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
        if ((state & KEYED_DATA) != 0) {
            Object[] table = (Object[]) data;
            for (int i = 1; i < table.length; i += 2) {
                if (key.equals(table[i])) return (O) table[i + 1];
            }
        }
        return null;
    }

    /**
	 * Sets the application defined widget data associated
	 * with the receiver to be the argument. The <em>widget
	 * data</em> is a single, unnamed field that is stored
	 * with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If
	 * the object stored in the widget data needs to be notified
	 * when the widget is disposed of, it is the application's
	 * responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @param data the widget data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
	 * </ul>
	 *
	 * @see #getData()
	 */
    public void setData(Object data) {
        checkWidget();
        if ((state & KEYED_DATA) != 0) {
            ((Object[]) this.data)[0] = data;
        } else {
            this.data = data;
        }
    }

    /**
	 * Sets the application defined property of the receiver
	 * with the specified name to the given value.
	 * <p>
	 * Applications may associate arbitrary objects with the
	 * receiver in this fashion. If the objects stored in the
	 * properties need to be notified when the widget is disposed
	 * of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 *
	 * @param key the name of the property
	 * @param value the new value for the property
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getData(String)
	 */
    public void setData(String key, Object value) {
        checkWidget();
        if (key == null) error(SWT.ERROR_NULL_ARGUMENT);
        int index = 1;
        Object[] table = null;
        if ((state & KEYED_DATA) != 0) {
            table = (Object[]) data;
            while (index < table.length) {
                if (key.equals(table[index])) break;
                index += 2;
            }
        }
        if (value != null) {
            if ((state & KEYED_DATA) != 0) {
                if (index == table.length) {
                    Object[] newTable = new Object[table.length + 2];
                    System.arraycopy(table, 0, newTable, 0, table.length);
                    data = table = newTable;
                }
            } else {
                table = new Object[3];
                table[0] = data;
                data = table;
                state |= KEYED_DATA;
            }
            table[index] = key;
            table[index + 1] = value;
        } else {
            if ((state & KEYED_DATA) != 0) {
                if (index != table.length) {
                    int length = table.length - 2;
                    if (length == 1) {
                        data = table[0];
                        state &= ~KEYED_DATA;
                    } else {
                        Object[] newTable = new Object[length];
                        System.arraycopy(table, 0, newTable, 0, index);
                        System.arraycopy(table, index + 2, newTable, index, length - index);
                        data = newTable;
                    }
                }
            }
        }
    }

    /**
	 * Throws an <code>SWTException</code> if the receiver can not
	 * be accessed by the caller. This may include both checks on
	 * the state of the receiver and more generally on the entire
	 * execution context. This method <em>should</em> be called by
	 * widget implementors to enforce the standard SWT invariants.
	 * <p>
	 * Currently, it is an error to invoke any method (other than
	 * <code>isDisposed()</code>) on a widget that has had its
	 * <code>dispose()</code> method called. It is also an error
	 * to call widget methods from any thread that is different
	 * from the thread that created the widget.
	 * </p><p>
	 * In future releases of SWT, there may be more or fewer error
	 * checks and exceptions may be thrown for different reasons.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
    protected void checkWidget() {
        if ((state & DISPOSED) != 0) error(SWT.ERROR_WIDGET_DISPOSED);
    }

    /**
	 * Internal
	 * @param code
	 * @since 1.0
	 */
    void error(int code) {
        SWT.error(code);
    }

    /**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an event of the given type occurs. When the
	 * event does occur in the widget, the listener is notified by
	 * sending it the <code>handleEvent()</code> message. The event
	 * type is one of the event constants defined in class <code>SWT</code>.
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should be notified when the event occurs
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Listener
	 * @see SWT
	 */
    public void addListener(int eventType, Listener listener) {
        checkWidget();
        if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
        _addListener(eventType, listener);
    }

    /**
	 * Internal
	 * @param eventType
	 * @param listener
	 * @since 1.0
	 */
    void _addListener(int eventType, Listener listener) {
        if (eventTable == null) eventTable = new EventTable();
        eventTable.hook(eventType, listener);
    }

    /**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the widget is disposed. When the widget is
	 * disposed, the listener is notified by sending it the
	 * <code>widgetDisposed()</code> message.
	 *
	 * @param listener the listener which should be notified when the receiver is disposed
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see DisposeListener
	 * @see #removeDisposeListener
	 */
    public void addDisposeListener(DisposeListener listener) {
        checkWidget();
        if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
        TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Dispose, typedListener);
    }

    /**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the widget is disposed.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see DisposeListener
	 * @see #addDisposeListener
	 */
    public void removeDisposeListener(DisposeListener listener) {
        checkWidget();
        if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
        if (eventTable == null) return;
        eventTable.unhook(SWT.Dispose, listener);
    }

    /**
	 * Internal
	 * @return Internal
	 * @since 1.0
	 */
    public boolean isDisposed() {
        return false;
    }

    /**
	 * Internal
	 * @param event
	 * @since 1.0
	 */
    void sendEvent(Event event) {
        event.widget = this;
        eventTable.sendEvent(event);
    }

    /**
	 * Internal
	 * @since 1.0
	 */
    public void dispose() {
    }
}
