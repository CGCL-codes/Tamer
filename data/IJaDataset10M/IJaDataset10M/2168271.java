package be.lassi.lanbox.commands.extra.common;

import be.lassi.lanbox.commands.LanboxCommand;
import be.lassi.util.NotImplemented;

/**
 * Lanbox command (logging support only).
 */
public class CommonSetIPConfig extends LanboxCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "B0";

    /**
     * Constructs a new command.
     */
    public CommonSetIPConfig() {
        super(ID, "*??#".length());
        throw new NotImplemented();
    }

    /**
     * Constructs a new command from a request buffer.
     * 
     * @param request the buffer to construct the command from
     */
    public CommonSetIPConfig(final byte[] request) {
        super(request);
    }
}
