package com.sun.midp.push.reservation;

import java.io.IOException;

/**
 * Protocol-dependent representation of connection which could become
 * a reservation later.
 *
 * <p>
 * The instance of this class are expected to be just data holders (see
 * probable additional requirements below).
 * </p>
 *
 * @see ConnectionReservation
 * @see ReservationDescriptorFactory
 */
public interface ReservationDescriptor {

    /**
     * Reserves the connection.
     *
     * <p>
     * The very moment this method returns, the correspondong connection cannot
     * be opened by any other application (including native ones) until
     * reservation is cancelled.  The connection cannot be reserved by
     * <em>any</em> application (including one for which it has been reserved).
     * <code>IOException</code> should be thrown to report such a situation.
     * </p>
     *
     * <p>
     * Pair <code>midletSuiteId</code> and <code>midletClassName</code>
     * should refer to valid <code>MIDlet</code>
     * </p>
     *
     * @param midletSuiteId <code>MIDlet</code> suite ID
     *
     * @param midletClassName name of <code>MIDlet</code> class
     *
     * @param dataAvailableListener data availability listener
     *
     * @return connection reservation
     *
     * @throws IOException if connection cannot be reserved
     *  for the given application
     */
    ConnectionReservation reserve(int midletSuiteId, String midletClassName, DataAvailableListener dataAvailableListener) throws IOException;

    /**
     * Gets connection name of descriptor.
     *
     * <p>
     * Should be identical to one passed into
     * {@link ReservationDescriptorFactory#getDescriptor}.
     * </p>
     *
     * @return connection name
     */
    String getConnectionName();

    /**
     * Gets filter of descriptor.
     *
     * <p>
     * Should be identical to one passed into
     * {@link ReservationDescriptorFactory#getDescriptor}.
     * </p>
     *
     * @return connection filter
     */
    String getFilter();
}
