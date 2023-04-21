package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the "get document base url" command.
 * 
 * @author jwootton
 *
 */
public class GetDocBaseUrlCmd extends VibeRpcCmd {

    private String m_binderId;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public GetDocBaseUrlCmd() {
        super();
    }

    /**
	 * 
	 */
    public GetDocBaseUrlCmd(String binderId) {
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
        return VibeRpcCmdType.GET_DOCUMENT_BASE_URL.ordinal();
    }
}
