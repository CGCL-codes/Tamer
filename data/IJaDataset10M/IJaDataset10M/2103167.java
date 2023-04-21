package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * This commands requests the desktop system to return the default path. This is
 * the list that goes in the folder pop-up for the Macintosh and in the
 * directories list for Windows.
 * 
 * <pre>
 * 'dpth'
 * length = 0
 * </pre>
 * 
 * @author moshew
 */
public class DGetDefaultPath extends DockCommandFromNewtonBlank {

    /** <tt>kDGetDefaultPath</tt> */
    public static final String COMMAND = "dpth";

    public DGetDefaultPath() {
        super(COMMAND);
    }
}
