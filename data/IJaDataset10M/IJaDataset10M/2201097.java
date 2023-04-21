package org.kablink.teaming.gwt.client.rpc.shared;

import java.util.ArrayList;
import java.util.List;
import org.kablink.teaming.gwt.client.util.EntryId;

/**
 * This class holds all of the information necessary to execute the
 * 'save email notification information' command.
 * 
 * @author drfoster@novell.com
 */
public class SaveEmailNotificationInfoCmd extends VibeRpcCmd {

    private boolean m_overridePresets;

    private List<EntryId> m_entryIds;

    private List<String> m_digestAddressTypes;

    private List<String> m_msgAddressTypes;

    private List<String> m_msgNoAttAddressTypes;

    private List<String> m_textAddressTypes;

    private Long m_binderId;

    /**
	 * Constructor method.
	 * 
	 * For GWT serialization, must have a zero parameter constructor.
	 */
    public SaveEmailNotificationInfoCmd() {
        super();
        m_digestAddressTypes = new ArrayList<String>();
        m_msgAddressTypes = new ArrayList<String>();
        m_msgNoAttAddressTypes = new ArrayList<String>();
        m_textAddressTypes = new ArrayList<String>();
    }

    /**
	 * Constructor method.
	 * 
	 * @param binderId
	 */
    public SaveEmailNotificationInfoCmd(Long binderId) {
        this();
        setBinderId(binderId);
    }

    /**
	 * Constructor method.
	 * 
	 * @param entryIds
	 */
    public SaveEmailNotificationInfoCmd(List<EntryId> entryIds) {
        this();
        setEntryIds(entryIds);
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public boolean getOverridePresets() {
        return m_overridePresets;
    }

    public List<EntryId> getEntryIds() {
        return m_entryIds;
    }

    public List<String> getDigestAddressTypes() {
        return m_digestAddressTypes;
    }

    public List<String> getMsgAddressTypes() {
        return m_msgAddressTypes;
    }

    public List<String> getMsgNoAttAddressTypes() {
        return m_msgNoAttAddressTypes;
    }

    public List<String> getTextAddressTypes() {
        return m_textAddressTypes;
    }

    public Long getBinderId() {
        return m_binderId;
    }

    /**
	 * Set'er methods.
	 * 
	 * @param binderId
	 */
    public void setEntryIds(List<EntryId> entryIds) {
        m_entryIds = entryIds;
    }

    public void setOverridePresets(boolean overridePresets) {
        m_overridePresets = overridePresets;
    }

    public void setDigestAddressTypes(List<String> digestAddressTypes) {
        m_digestAddressTypes = digestAddressTypes;
    }

    public void setMsgAddressTypes(List<String> msgAddressTypes) {
        m_msgAddressTypes = msgAddressTypes;
    }

    public void setMsgNoAttAddressTypes(List<String> msgNoAttAddressTypes) {
        m_msgNoAttAddressTypes = msgNoAttAddressTypes;
    }

    public void setTextAddressTypes(List<String> textAddressTypes) {
        m_textAddressTypes = textAddressTypes;
    }

    public void setBinderId(Long binderId) {
        m_binderId = binderId;
    }

    /**
	 * Adds an email address to a list of email addresses.
	 * 
	 * @param
	 */
    public void addDigestAddressType(String type) {
        m_digestAddressTypes.add(type);
    }

    public void addMsgAddressType(String type) {
        m_msgAddressTypes.add(type);
    }

    public void addMsgNoAttAddressType(String type) {
        m_msgNoAttAddressTypes.add(type);
    }

    public void addTextAddressType(String type) {
        m_textAddressTypes.add(type);
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
        return VibeRpcCmdType.SAVE_EMAIL_NOTIFICATION_INFORMATION.ordinal();
    }
}
