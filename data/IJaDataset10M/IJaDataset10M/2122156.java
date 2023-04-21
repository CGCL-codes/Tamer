package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the "get tag rights for entry" command.
 * 
 * @author jwootton
 *
 */
public class GetTagRightsForEntryCmd extends VibeRpcCmd {

    private String m_entryId;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public GetTagRightsForEntryCmd() {
        super();
    }

    /**
	 * 
	 */
    public GetTagRightsForEntryCmd(String entryId) {
        this();
        m_entryId = entryId;
    }

    /**
	 * 
	 */
    public String getEntryId() {
        return m_entryId;
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
        return VibeRpcCmdType.GET_TAG_RIGHTS_FOR_ENTRY.ordinal();
    }
}
