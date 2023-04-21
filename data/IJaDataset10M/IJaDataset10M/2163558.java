package net.sf.jncu.protocol.v1_0.sync;

import java.io.IOException;
import java.io.InputStream;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is sent by the Newton in response to a
 * <tt>kDReturnChangedEntry</tt> command from the desktop. It can also be sent
 * by the desktop.
 * 
 * <pre>
 * 'cent'
 * length
 * entry
 * </pre>
 */
public class DChangedEntry extends DockCommandFromNewtonScript<NSOFObject> implements IDockCommandToNewton {

    /** <tt>kDChangedEntry</tt> */
    public static final String COMMAND = "cent";

    private IDockCommandToNewton to;

    /**
	 * Creates a new command.
	 */
    public DChangedEntry() {
        super(COMMAND);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new DockCommandToNewtonScript<NSOFObject>(COMMAND) {
            };
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }
}
