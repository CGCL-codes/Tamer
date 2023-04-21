package org.kablink.teaming.gwt.client.rpc.shared;

import org.kablink.teaming.gwt.client.GwtFileSyncAppConfiguration;

/**
 * This class holds all of the information necessary to execute the "Save File Sync App Configuration" command.
 * 
 * @author jwootton
 *
 */
public class SaveFileSyncAppConfigurationCmd extends VibeRpcCmd {

    private GwtFileSyncAppConfiguration m_fileSyncAppConfiguration;

    /**
	 * For GWT serialization, must have a zero param contructor
	 */
    public SaveFileSyncAppConfigurationCmd() {
        super();
    }

    /**
	 * 
	 */
    public SaveFileSyncAppConfigurationCmd(GwtFileSyncAppConfiguration fileSyncAppConfiguration) {
        this();
        m_fileSyncAppConfiguration = fileSyncAppConfiguration;
    }

    /**
	 * 
	 */
    public GwtFileSyncAppConfiguration getFileSyncAppConfiguration() {
        return m_fileSyncAppConfiguration;
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
        return VibeRpcCmdType.SAVE_FILE_SYNC_APP_CONFIGURATION.ordinal();
    }
}
