package net.kano.joscar.snaccmd.loc;

import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.snaccmd.conn.SnacFamilyInfo;

/**
 * A base class for the commands in the "location" <code>0x02</code> SNAC
 * family.
 */
public abstract class LocCommand extends SnacCommand {

    /** The SNAC family code for the location family. */
    public static final int FAMILY_LOC = 0x0002;

    /** A set of SNAC family information for this family. */
    public static final SnacFamilyInfo FAMILY_INFO = new SnacFamilyInfo(FAMILY_LOC, 0x0001, 0x3110, 0x08e5);

    /** A command subtype for requesting location-related rights. */
    public static final int CMD_RIGHTS_REQ = 0x0002;

    /** A command subtype containing location-related "rights." */
    public static final int CMD_RIGHTS_RESP = 0x0003;

    /** A command subtype for setting one's "info." */
    public static final int CMD_SET_INFO = 0x0004;

    /** A command subtype formerly used for getting another user's "info." */
    public static final int CMD_OLD_GET_INFO = 0x0005;

    /** A command subtype containing a user's "info." */
    public static final int CMD_USER_INFO = 0x0006;

    /**
     * A command subtype for requesting another user's directory information.
     */
    public static final int CMD_GET_DIR = 0x000b;

    /** A command subtype containing a user's directory information. */
    public static final int CMD_DIR_INFO = 0x000c;

    /** A command subtype for setting one's chat interests. */
    public static final int CMD_SET_INTERESTS = 0x000f;

    /**
     * A command subtype for acknowledging that one's chat interests were set.
     */
    public static final int CMD_INTEREST_ACK = 0x0010;

    /** A command subtype for setting your directory information. */
    public static final int CMD_SET_DIR = 0x0009;

    /**
     * A command subtype for acknowledging the setting of one's directory
     * information.
     */
    public static final int CMD_SET_DIR_ACK = 0x000a;

    /** A command subtype used to request information about a user. */
    public static final int CMD_NEW_GET_INFO = 0x0015;

    /**
     * Creates a new command in the location family.
     *
     * @param command the SNAC command subtype
     */
    protected LocCommand(int command) {
        super(FAMILY_LOC, command);
    }
}
