package org.openorb.orb.io;

import java.util.LinkedList;

/**
 * A StorageBuffer list. As fragments arrive their buffers are appended to
 * the list. Consumers of the buffer source repeditivly call {@link #next}
 * to read storage buffers, when the last buffer is received {@link #next} will
 * return null. To terminate reading early call {@link #setException} followed
 * by {@link #next} to notify any listeners of the final read.
 *
 * @author Chris Wood
 * @version $Revision: 1.9 $ $Date: 2004/08/11 10:26:51 $
 */
public class BufferSource {

    /** This is the number of bytes from all StorageBuffer fragments. */
    private int m_available = 0;

    /** This list contains all fragments received. */
    private LinkedList m_buffers = new LinkedList();

    /** This exception will be thrown when an error occurs. */
    private org.omg.CORBA.SystemException m_exception = null;

    /** Flag that indicates whether the last fragment is already in the list. */
    private boolean m_last_received = false;

    /** Flag that indicates whether the last listener has been notified. */
    private boolean m_last_notified = false;

    /** Byte number on which the mark has been set or -1 if unmarked. */
    private int m_mark_available = -1;

    /** The previous StorageBuffer. */
    private LinkedList m_previous;

    /** The current StorageBuffer. */
    private StorageBuffer m_current;

    /** Utility field holding the WaitingForBufferListener. */
    private transient WaitingForBufferListener m_waiting_for_buffer_listener = null;

    /** Utility field holding the LastMessageProcessedListener. */
    private transient LastMessageProcessedListener m_last_msg_processed_listener = null;

    /**
     * Default constructor.
     */
    public BufferSource() {
    }

    /**
     * Constructor.
     *
     * @param buffer The first buffer fragment.
     * @param isLast Indicates whether the first parameter is already the last fragment.
     */
    public BufferSource(StorageBuffer buffer, boolean isLast) {
        m_buffers.addLast(buffer);
        m_last_received = isLast;
        m_available += buffer.available();
    }

    /**
     * Total size of remaining fragments. Used by InputStream.
     */
    public synchronized int available() {
        return m_available;
    }

    /**
     * Gets next buffer in message. Will block until buffer is available.
     * Returns null for end of message or throws exception if one has arrived.
     * All consumers will always complete their use of the source with a call
     * to next which either throws a system exception or returns null.
     */
    public StorageBuffer next() {
        final LastMessageProcessedListener listenerObj;
        final org.omg.CORBA.SystemException exceptionObj;
        synchronized (this) {
            while (true) {
                if (m_exception != null) {
                    break;
                }
                if (m_buffers.size() > 0) {
                    StorageBuffer ret = (StorageBuffer) m_buffers.removeFirst();
                    m_available -= ret.available();
                    if (m_mark_available >= 0) {
                        m_previous.addLast(m_current);
                        ret.mark();
                    }
                    m_current = ret;
                    return ret;
                }
                if (m_last_received) {
                    break;
                }
                waitingForBuffer();
            }
            if (m_last_notified || m_last_msg_processed_listener == null) {
                if (m_exception != null) {
                    throw m_exception;
                }
                return null;
            }
            m_last_notified = true;
            listenerObj = m_last_msg_processed_listener;
            exceptionObj = (m_exception != null) ? m_exception : null;
        }
        if (listenerObj != null) {
            listenerObj.lastMessageProcessed(this);
        }
        if (exceptionObj != null) {
            throw exceptionObj;
        }
        return null;
    }

    /**
     * Mark buffer sequence. Can be reset at later time.
     */
    public synchronized void mark() {
        if (m_previous == null) {
            m_previous = new LinkedList();
        } else {
            m_previous.clear();
        }
        m_current.mark();
        m_mark_available = m_available;
    }

    /**
     * Reset to previously marked position
     */
    public synchronized StorageBuffer reset() {
        if (m_mark_available == -1) {
            return null;
        }
        while (!m_previous.isEmpty()) {
            if (!m_current.reset()) {
                org.openorb.orb.util.Trace.signalIllegalCondition(null, "Unable to reset body buffer.");
            }
            m_buffers.addFirst(m_current);
            m_current = (StorageBuffer) m_previous.removeLast();
        }
        if (!m_current.reset()) {
            org.openorb.orb.util.Trace.signalIllegalCondition(null, "Unable to reset body buffer.");
        }
        m_available = m_mark_available;
        m_mark_available = -1;
        return m_current;
    }

    /**
     * Called whenever we need to wait for the next buffer. Calls listener
     * if one is registered, otherwise waits on condition.
     */
    private void waitingForBuffer() {
        if (m_waiting_for_buffer_listener != null && !m_waiting_for_buffer_listener.waitForBuffer(this)) {
            return;
        }
        try {
            wait();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Set exceptional result. It is invalid to set a null exception.
     */
    public synchronized void setException(org.omg.CORBA.SystemException exception) {
        if (exception == null) {
            throw new NullPointerException();
        }
        m_exception = exception;
        m_buffers = null;
        notify();
    }

    /**
     * Get exception set with setException.
     */
    public synchronized org.omg.CORBA.SystemException getException() {
        return m_exception;
    }

    /**
     * Add buffer to end of chain. If an exception has been set the buffer
     * will be silently discarded.
     */
    public synchronized void addLast(StorageBuffer buffer, boolean isLast) {
        if (m_exception != null) {
            return;
        }
        m_buffers.addLast(buffer);
        m_last_received = isLast;
        m_available += buffer.available();
        if (m_mark_available >= 0) {
            m_mark_available += buffer.available();
        }
        notify();
    }

    /**
     * Registers WaitingForBufferListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addWaitingForBufferListener(WaitingForBufferListener listener) throws java.util.TooManyListenersException {
        if (m_waiting_for_buffer_listener != null) {
            throw new java.util.TooManyListenersException();
        }
        m_waiting_for_buffer_listener = listener;
    }

    /**
     * Removes WaitingForBufferListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeWaitingForBufferListener(WaitingForBufferListener listener) {
        m_waiting_for_buffer_listener = null;
    }

    /**
     * Registers LastMessageProcessedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addLastMessageProcessedListener(LastMessageProcessedListener listener) throws java.util.TooManyListenersException {
        if (m_last_msg_processed_listener != null) {
            throw new java.util.TooManyListenersException();
        }
        m_last_msg_processed_listener = listener;
    }

    /**
     * Removes LastMessageProcessedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeLastMessageProcessedListener(LastMessageProcessedListener listener) {
        m_last_msg_processed_listener = null;
    }

    /**
     * Called whenever a buffer is needed to continue processing.
     */
    public interface WaitingForBufferListener extends java.util.EventListener {

        /**
         * Called whenever a buffer is needed to continue processing.
         * @param source The buffer source.
         * @return false to use the default method, wait until an exception
         *       is set or a buffer is added.
         */
        boolean waitForBuffer(BufferSource source);
    }

    /**
     * Called when the last message has been processed or an exception is to be
     * returned from {@link #next}. An different exception can be set using
     * {@link #setException}.
     */
    public interface LastMessageProcessedListener extends java.util.EventListener {

        void lastMessageProcessed(BufferSource source);
    }
}
