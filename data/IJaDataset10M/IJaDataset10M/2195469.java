package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the "Get Binder Info" command.
 * 
 * @author jwootton
 *
 */
public class GetBinderInfoCmd extends VibeRpcCmd {

    private String m_binderId;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public GetBinderInfoCmd() {
        super();
    }

    /**
	 * 
	 */
    public GetBinderInfoCmd(String binderId) {
        this();
        m_binderId = binderId;
    }

    /**
	 * 
	 */
    public GetBinderInfoCmd(Long binderId) {
        this(String.valueOf(binderId));
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
        return VibeRpcCmdType.GET_BINDER_INFO.ordinal();
    }
}
