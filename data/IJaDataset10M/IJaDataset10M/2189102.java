package net.kano.joscar.snac;

/**
 * Provides an interface for processing and intercepting SNAC packets received
 * on a SNAC processor, optionally halting further processing of any
 * given packet. It is important to note that vetoable SNAC listeners are never
 * called for "SNAC responses." For details, see {@link AbstractSnacProcessor}.
 *
 * @see AbstractSnacProcessor
 */
public interface VetoableSnacPacketListener {

    /**
     * A value indicating that the SNAC processor should stop passing the packet
     * through other vetoable and non-vetoable listeners. This value indicates
     * that further internal processing may be performed on the packet, though
     * as of this writing no such processing is done either way. It is suggested
     * to use this instead of <code>STOP_PROCESSING_ALL</code>, however, to
     * allow for future expansion of the SNAC processing code internal to
     * joscar.
     */
    Object STOP_PROCESSING_LISTENERS = new Object();

    /**
     * A value indicating the SNAC processor should stop all further
     * processing of a packet immediately. As of this writing, this is
     * functionally equivalent to <code>STOP_PROCESSING_LISTENERS</code>, as
     * no further processing is done anyway.
     */
    Object STOP_PROCESSING_ALL = new Object();

    /**
     * A value indicating that the SNAC processor should continue processing
     * the given packet normally.
     */
    Object CONTINUE_PROCESSING = new Object();

    /**
     * Called when a new packet arrives on a SNAC connection. See individual
     * documentation for {@link #CONTINUE_PROCESSING}, {@link
     * #STOP_PROCESSING_LISTENERS}, and {@link #STOP_PROCESSING_ALL} for details
     * on when to return which value.
     *
     * @param event an object describing the event
     * @return one of {@link #CONTINUE_PROCESSING}, {@link
     *         #STOP_PROCESSING_LISTENERS}, and {@link #STOP_PROCESSING_ALL}
     */
    Object handlePacket(SnacPacketEvent event);
}
