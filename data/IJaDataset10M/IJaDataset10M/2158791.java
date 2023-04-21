package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the "get project info" command.
 * 
 * @author jwootton
 *
 */
public class GetProjectInfoCmd extends VibeRpcCmd {

    private String m_binderId;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public GetProjectInfoCmd() {
        super();
    }

    /**
	 * 
	 */
    public GetProjectInfoCmd(String binderId) {
        this();
        m_binderId = binderId;
    }

    /**
	 * 
	 */
    public String getBinderId() {
        return m_binderId;
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
        return VibeRpcCmdType.GET_PROJECT_INFO.ordinal();
    }
}
