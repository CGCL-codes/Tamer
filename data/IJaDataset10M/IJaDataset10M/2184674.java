package jreceiver.common.rpc;

import jreceiver.common.callback.rpc.SettingListener;

/**
 * General settings queries and updates for a (possibly-remote) JRec server
 *
 * @author Reed Esau
 * @version $Revision: 1.7 $ $Date: 2002/12/29 00:44:08 $
*/
public interface GeneralSettings extends SettingListener {

    public static final int GENERAL_SETTING_ID = -100;

    public static final String GENERAL_SETTING_DESC = "general_settings.title";

    public static final String HANDLER_NAME = "GeneralSettings";
}
