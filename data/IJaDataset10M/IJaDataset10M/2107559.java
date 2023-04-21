package be.lassi.lanbox.commands.extra.layer;

import be.lassi.lanbox.commands.LanboxCommand;
import be.lassi.util.NotImplemented;

/**
 * Lanbox command (logging support only).
 */
public class LayerSetDeviceID extends LanboxCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "5E";

    /**
     * Constructs a new command.
     */
    public LayerSetDeviceID() {
        super(ID, "*??#".length());
        throw new NotImplemented();
    }

    /**
     * Constructs a new command from a request buffer.
     * 
     * @param request the buffer to construct the command from
     */
    public LayerSetDeviceID(final byte[] request) {
        super(request);
    }
}
