package be.lassi.lanbox.commands.extra;

import be.lassi.lanbox.commands.Command;
import be.lassi.util.NotImplemented;

/**
 * Lanbox command (logging support only).
 */
public class LayerSetCueListWait extends Command {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "61";

    /**
     * Constructs a new command.
     */
    public LayerSetCueListWait() {
        super(ID, "*??#".length());
        throw new NotImplemented();
    }

    /**
     * Constructs a new command from a request buffer.
     * 
     * @param request the buffer to construct the command from
     */
    public LayerSetCueListWait(final byte[] request) {
        super(request);
    }
}
