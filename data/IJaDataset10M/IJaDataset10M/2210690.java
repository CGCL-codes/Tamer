package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the
 * 'get clipboard users' command.
 * 
 * @author drfoster@novell.com
 */
public class GetClipboardUsersCmd extends VibeRpcCmd {

    /**
	 * Constructor method.
	 * 
	 * For GWT serialization, must have a zero parameter constructor.
	 */
    public GetClipboardUsersCmd() {
        super();
    }

    /**
	 * Returns the command's enumeration value.
	 * 
	 * Implements VibeRpcCmd.getCmdType()
	 * 
	 * @return
	 */
    @Override
    public int getCmdType() {
        return VibeRpcCmdType.GET_CLIPBOARD_USERS.ordinal();
    }
}
