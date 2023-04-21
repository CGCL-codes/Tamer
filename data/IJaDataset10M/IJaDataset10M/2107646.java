package be.lassi.lanbox.commands.extra.common;

import be.lassi.lanbox.commands.LanboxCommand;
import be.lassi.util.NotImplemented;

/**
 * Lanbox command (logging support only).
 */
public class CommonSetMIDI extends LanboxCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "68";

    /**
     * Constructs a new command.
     */
    public CommonSetMIDI() {
        super(ID, "*??#".length());
        throw new NotImplemented();
    }

    /**
     * Constructs a new command from a request buffer.
     * 
     * @param request the buffer to construct the command from
     */
    public CommonSetMIDI(final byte[] request) {
        super(request);
    }
}
