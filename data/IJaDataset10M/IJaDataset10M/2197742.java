package be.lassi.lanbox.commands.extra;

import be.lassi.lanbox.commands.LanboxCommand;
import be.lassi.util.NotImplemented;

/**
 * Lanbox command (logging support only).
 */
public class NoteOff extends LanboxCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "3C";

    /**
     * Constructs a new command.
     */
    public NoteOff() {
        super(ID, "*??#".length());
        throw new NotImplemented();
    }

    /**
     * Constructs a new command from a request buffer.
     * 
     * @param request the buffer to construct the command from
     */
    public NoteOff(final byte[] request) {
        super(request);
    }
}
