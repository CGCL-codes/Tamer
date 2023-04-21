package org.kablink.teaming.gwt.client.rpc.shared;

/**
 * This class holds all of the information necessary to execute the
 * 'get task bundle' command.
 * 
 * @author drfoster@novell.com
 */
public class GetTaskBundleCmd extends VibeRpcCmd {

    private boolean m_embeddedInJSP;

    private Long m_binderId;

    private String m_filterType;

    private String m_modeType;

    /**
	 * Class constructor.
	 * 
	 * For GWT serialization, must have a zero parameter
	 * constructor.
	 */
    public GetTaskBundleCmd() {
        super();
    }

    /**
	 * Class constructor.
	 *
	 * @param embeddedInJSP
	 * @param binderId
	 * @param filterType
	 * @param modeType
	 */
    public GetTaskBundleCmd(final boolean embeddedInJSP, final Long binderId, final String filterType, final String modeType) {
        this();
        setEmbeddedInJSP(embeddedInJSP);
        setBinderId(binderId);
        setFilterType(filterType);
        setModeType(modeType);
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public boolean isEmbeddedInJSP() {
        return m_embeddedInJSP;
    }

    public Long getBinderId() {
        return m_binderId;
    }

    public String getFilterType() {
        return m_filterType;
    }

    public String getModeType() {
        return m_modeType;
    }

    /**
	 * Set'er methods.
	 * 
	 * @param
	 */
    public void setEmbeddedInJSP(final boolean embeddedInJSP) {
        m_embeddedInJSP = embeddedInJSP;
    }

    public void setBinderId(final Long binderId) {
        m_binderId = binderId;
    }

    public void setFilterType(final String filterType) {
        m_filterType = filterType;
    }

    public void setModeType(final String modeType) {
        m_modeType = modeType;
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
        return VibeRpcCmdType.GET_TASK_BUNDLE.ordinal();
    }
}
