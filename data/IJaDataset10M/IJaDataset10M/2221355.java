package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the "save user status" command.
 * 
 * @author jwootton
 *
 */
public class SaveUserStatusCmd extends VibeRpcCmd {

    private String m_status;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public SaveUserStatusCmd() {
        super();
    }

    /**
	 * 
	 */
    public SaveUserStatusCmd(String status) {
        this();
        m_status = status;
    }

    /**
	 * 
	 */
    public String getStatus() {
        return m_status;
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
        return VibeRpcCmdType.SAVE_USER_STATUS.ordinal();
    }

    /**
	 * Set'er methods.
	 * 
	 * @param status
	 */
    public void setStatus(String status) {
        m_status = status;
    }
}
