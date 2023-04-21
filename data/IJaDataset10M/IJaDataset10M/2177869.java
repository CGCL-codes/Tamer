package net.kano.joscar.flap;

/**
 * Provides an interface for deriving high-level <code>FlapCommand</code>s from
 * low-level <code>FlapPacket</code>s.
 */
public interface FlapCommandFactory {

    /**
     * Returns a <code>FlapCommand</code> representing the given packet, or
     * <code>null</code> if there is no <code>FlapCommand</code> which can
     * represent the given packet (that is, if the packet type or format is
     * unrecognized by this factory).
     *
     * @param packet the packet from which a <code>FlapCommand</code> should
     *        be created
     * @return a <code>FlapCommand</code> to represent the given packet, or
     *         <code>null</code> if this factory has no <code>FlapCommand</code>
     *         counterpart for this packet type or format
     */
    FlapCommand genFlapCommand(FlapPacket packet);
}
